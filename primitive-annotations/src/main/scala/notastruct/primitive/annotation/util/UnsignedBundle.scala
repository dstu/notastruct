package notastruct.primitive.annotation.util

import scala.reflect.macros.blackbox.Context

class UnsignedBundle(val c: Context) {
  import c.universe._

  val NAME_PATTERN = raw"u([1-9][0-9]*)".r

  case class TypeFields(max: c.Tree,
                        valueType: c.Tree,
                        containingType: c.Tree)

  def typeFields(width: Int): Option[TypeFields] = {
    val max = (1L << width) - 1
    val maskLong = (1L << width) - 1
    val maskInt = (1 << width) - 1
    if (width > 63) {
      None
    } else if (width > 32) {
      Some(TypeFields(max=q"$max",
                      valueType=tq"Long",
                      containingType=tq"Long"))
    } else if (width == 32) {
      Some(TypeFields(max=q"$max",
                      valueType=tq"Long",
                      containingType=tq"Int"))
    } else if (width > 16) {
      Some(TypeFields(max=q"${max.toInt}",
                      valueType=tq"Int",
                      containingType=tq"Int"))
    } else if (width == 16) {
      Some(TypeFields(max=q"${max.toInt}",
                      valueType=tq"Int",
                      containingType=tq"Short"))
    } else if (width > 8) {
      Some(TypeFields(max=q"${max.toShort}",
                      valueType=tq"Short",
                      containingType=tq"Short"))
    } else if (width == 8) {
      Some(TypeFields(max=q"${max.toShort}",
                      valueType=tq"Short",
                      containingType=tq"Byte"))
    } else if (width > 1) {
      Some(TypeFields(max=q"${max.toByte}",
                      valueType=tq"Byte",
                      containingType=tq"Byte"))
    } else {
      None
    }
  }

  def unsigned(annottees: c.Expr[Any]*): c.Expr[Any] = {
    annottees.map(_.tree).toList match {
      case (classDef: ClassDef) :: Nil => {
        val d = buildDeclarations(classDef)
        // println(d)
        c.Expr(d)
      }
      case (classDef: ClassDef) :: (companionDef: ModuleDef) :: Nil =>
        c.abort(c.enclosingPosition, "Macro target should not have an explicit companion object")
      case _ =>
        c.abort(c.enclosingPosition, "Invalid macro target")
    }
  }

  def buildDeclarations(classDef: ClassDef): c.Tree =
    classDef match {
      case q"class $className { ..$body }" => {
        if (body.isEmpty) {
          className.toString match {
            case NAME_PATTERN(widthString) => {
              val width = widthString.toInt
              val companionName = TermName(className.toString)
              val mask =
                if (width > 32) {
                  q"${-1L >>> (64 - width)}"
                } else {
                  q"${-1 >>> (32 - width)}"
                }
              typeFields(width) match {
                case Some(fields) => {
                  q"""
class $className(val primitiveBits: ${fields.containingType}) extends AnyVal {
  def toValue: ${fields.valueType} =
    _root_.bitsafe.ops.bit[${fields.valueType}](primitiveBits)
  override def toString = toValue.toString
}

object $companionName {
  import bitsafe.ops._
  import bitsafe.expression._

  def apply(value: ${fields.valueType}): $className =
    new $className(bit[${fields.containingType}](value and $mask))

  implicit object ${TermName(className.toString + "HasAttributes")} extends _root_.notastruct.model.PackableAttributes[$className] {
    override def width: Int = $width
  }
  implicit object ${TermName(className.toString + "IsPackable")} extends _root_.notastruct.model.Packable[$className, ${fields.valueType}, ${fields.containingType}] {
    import notastruct.model._
    override def minValue(implicit attributes: PackableAttributes[$className]) = 0
    override def maxValue(implicit attributes: PackableAttributes[$className]) = ${fields.max}
  }
}
"""
                }
                case None =>
                  c.abort(c.enclosingPosition, s"Invalid type width $width")
                }
            }
            case _ =>
              c.abort(c.enclosingPosition, s"Unrecognized unsigned type name $className")
          }
        } else {
          c.abort(c.enclosingPosition, "Macro target should not explicitly define any elements")
        }
      }
    }
}
