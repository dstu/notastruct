package notastruct.util

import bitsafe.convert._
import bitsafe.expression._
import notastruct.model._
import scala.reflect.macros.blackbox.Context

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
        } else if (!classBody.isEmpty) {
          c.abort(c.enclosingPosition, s"Struct class $className should not explicitly define any elements")
        } else {
          val wrapped = TermName("x")
          val primitiveType = tq"Long"
          val newClassBody = paramsHead.map(buildAccessor(wrapped, 0, _))
          val companionName = TermName(className.toString)
          val newCompanionBody = Seq(q"")
          q"""
class $className(val $wrapped: $primitiveType) extends AnyVal {
  ..$newClassBody
}

object $companionName {
  ..$newCompanionBody
}
"""
        }
    }

  def buildAccessor(wrapped: TermName, offset: Int, declaration: ValDef): c.Tree =
      declaration match {
        case ValDef(mods, fieldName, typeName, _) => {
          val companion = TermName(typeName.toString)
          q"""
def $fieldName: $typeName = $companion.fromPacked($wrapped >>> $offset)
"""
        }
        case _ =>
          c.abort(declaration.pos, "Unrecognized declaration")
      }

  def buildConstructor(className: TypeName, offsets: Map[String, Int], declarations: Seq[ValDef]): c.Tree = {
    val fieldValues = declarations.map({
                                         case ValDef(mods, argumentName, typeName, _) => {
                                           val companionName = TermName(typeName.toString)
                                             q"$companionName($argumentName).toPackedLong << ${offsets[argumentName]}"
                                         }
                                       }).reduce((l, r) => q"$l | $r")
    q"""
def apply(..$declarations): $className = new $className($fieldValues)
"""
  }
}
