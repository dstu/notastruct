package notastruct.util

import bitsafe.convert._
import bitsafe.expression._
import notastruct.model._
import notastruct.primitive._
import scala.annotation.tailrec
import scala.reflect.macros.blackbox.Context
import scala.reflect.macros.TypecheckException

class StructBundle(val c: Context) {
  import c.universe._

  def struct(annottees: c.Expr[Any]*): c.Expr[Any] = {
    annottees.map(_.tree).toList match {
      case (classDef: ClassDef) :: Nil => {
        val declaration = buildDeclarations(classDef)
        println(declaration)
        c.Expr(declaration)
      }
      case (classDef: ClassDef) :: (companionDef: ModuleDef) :: Nil =>
        c.abort(c.enclosingPosition, "Macro target should not have an explicit companion object")
      case _ =>
        c.abort(c.enclosingPosition, "Invalid macro target")
    }
  }

  def buildDeclarations(classDef: ClassDef): c.Tree =
    classDef match {
      case q"class $className[..$tparams](..$paramsHead)(...$paramsTail) { ..$classBody }" =>
        if (!tparams.isEmpty) {
          c.abort(c.enclosingPosition, s"Struct class $className should not have type parameters")
        } else if (paramsHead.isEmpty) {
          c.abort(c.enclosingPosition, s"Struct class $className should have at least one parameter")
        } else if (!paramsTail.isEmpty) {
          c.abort(c.enclosingPosition, s"Struct class $className should have only one parameter list")
        } else {
          val wrapped = TermName("primitiveBits")
          val fields = buildFields(paramsHead)
          val model = StructType(className.toString, fields)
          widthToPrimitiveType(model.width) match {
            case None =>
              c.abort(c.enclosingPosition, s"Struct width of ${model.width} bits exceeds maximum of 64 bits")
            case Some(primitiveType) => {
              val accessors = buildAccessors(wrapped, paramsHead, model.fields)
              val toStringFormat = paramsHead.map {
                  case ValDef(mods, fieldName, _, _) => fieldName.toString.replace("%", "%%") + "=%s"
                } mkString(", ")
              val toStringValues = paramsHead.map {
                  case ValDef(mods, fieldName, _, _) => q"$fieldName.toValue"
                }

              val companionName = TermName(className.toString)
              val constructor = buildConstructor(className, primitiveType, paramsHead zip fields)
              q"""
class $className(val $wrapped: $primitiveType) extends AnyVal {
  ..$accessors
  override def toString = ${className.toString} + "(" + $toStringFormat.format(..$toStringValues) + ")"
  ..$classBody
}

object $companionName {
  $constructor
}
"""
            }
          }
        }
    }


  def widthToPrimitiveType(width: Int): Option[TypeTag[_]] =
    if (width > 64) {
      None
    } else {
      Some(if (width > 32) {
             typeTag[Long]
           } else if (width > 16) {
             typeTag[Int]
           } else if (width > 8) {
             typeTag[Short]
           } else {
             typeTag[Byte]
           })
    }

  def packable(wrappedType: c.Tree): Option[Packable[_]] =
    try {
      val packableTypeExpression = tq"_root_.notastruct.model.Packable[$wrappedType]"
      // println(s"Looking for packable type $packableTypeExpression")
      val packableValueExpression = q"??? : $packableTypeExpression"
      // println(s"Deriving type from expression $packableValueExpression")
      val packableType = c.typecheck(packableValueExpression).tpe
      // println(s"Resolved $packableType")
      val packableExpression = c.inferImplicitValue(packableType, silent = false)
      // println(s"Got packable: $packableExpression")
      val packable = c.eval(c.Expr[Packable[_]](c.untypecheck(packableExpression)))
      Some(packable)
    } catch {
      case te: TypecheckException => None
    }

  def buildFields(declarations: Seq[ValDef]): Seq[FieldType] = {
    @tailrec def computeOffsets(declarations: Seq[ValDef],
                                offsets: Seq[(String, (Int, Int))],
                                cumulativeOffset: Int): Seq[FieldType] =
      declarations.headOption match {
        case None =>
          if (offsets.isEmpty) {
            c.abort(c.enclosingPosition, "No valid field types found")
          } else {
            offsets.map {
              case (name, (offset, width)) => FieldType(name=name,
                                                        width=width,
                                                        offset=cumulativeOffset - offset - width)
            }
          }
        case Some(declaration) =>
          declaration match {
            case ValDef(mods, fieldName, typeName, _) => {
              packableAttributes(typeName).flatMap(_.width) match {
                case None => {
                  c.error(declaration.pos, s"Unable to infer an implicit instance of notastruct.model.Packable[$typeName]")
                  computeOffsets(declarations.tail, offsets, cumulativeOffset)
                }
                case Some(width) => {
                  if (cumulativeOffset > 64) {
                    c.error(declaration.pos, s"Field $fieldName starts beyond 64-bit maximum struct width")
                  } else if (cumulativeOffset + width > 64) {
                    c.error(declaration.pos, s"Field $fieldName extends beyond 64-bit maximum struct width")
                  }
                  computeOffsets(declarations.tail,
                                 offsets :+ (fieldName.toString, (cumulativeOffset, width)),
                                 cumulativeOffset + width)
                }
              }
            }
            case _ => {
              c.error(declaration.pos, "Unrecognized field declaration (expected: ValDef)")
                computeOffsets(declarations.tail, offsets, cumulativeOffset)
            }
          }
      }
    computeOffsets(declarations, Seq.empty, 0)
  }

  def buildAccessors(wrapped: TermName, declarations: Seq[ValDef], fields: Seq[FieldType]): Seq[c.Tree] = {
    @tailrec def build(fields: Seq[(ValDef, FieldType)], accessors: Seq[c.Tree]): Seq[c.Tree] =
        fields.headOption match {
          case None => accessors
          case Some((declaration @ ValDef(_, _, typeName, _), FieldType(fieldName, width, offset))) => {
            val mask = (-1L >> offset) << offset
            widthToPrimitiveType(width) match {
              case None =>
                c.abort(declaration.pos, s"Unable to discern underlying storage type for field of type $typeName")
              case Some(containingType) => {
                val accessor = q"""
def ${TermName(fieldName)}: $typeName = {
  import _root_.bitsafe.convert._
  import _root_.bitsafe.expression._

  new $typeName(bit[$containingType](($wrapped and $mask) rshift $offset))
}
"""
                build(fields.tail, accessors :+ accessor)
              }
            }
          }
        }

    build(declarations zip fields, Seq.empty)
  }

  def buildConstructor(className: TypeName, primitiveType: TypeTag[_], fields: Seq[(ValDef, FieldType)]): c.Tree = {
    val declarations = fields.map(_._1)
    val fieldValues = fields.map({
                                   case (ValDef(_, fieldName, typeName, _), FieldType(_, width, offset)) => {
                                     val companionName = TermName(typeName.toString)
                                       q"bit[$primitiveType]($fieldName.toValue) lshift $offset"
                                   }
                                 }).reduce((l, r) => q"$l or $r")
    q"""
def apply(..$declarations): $className = {
  import _root_.bitsafe.convert._
  import _root_.bitsafe.expression._

  new $className(bit[$primitiveType]($fieldValues))
}
"""
  }
}
