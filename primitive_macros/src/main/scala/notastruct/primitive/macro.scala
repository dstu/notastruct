package notastruct.primitive

import scala.annotation.StaticAnnotation
import scala.reflect.macros.whitebox.Context

class unsigned extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro UnsignedImpl.macroImpl
}

class UnsignedImpl(val c: Context) {
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

  def macroImpl(annottees: c.Expr[Any]*): c.Expr[Any] = {
    annottees.map(_.tree).toList match {
      case (classDef: ClassDef) :: Nil =>
        c.Expr(buildDeclarations(classDef))
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
              typeFields(width) match {
                case Some(fields) => {
                  q"""
class $className(val primitiveBits: ${fields.containingType}) extends AnyVal {
  def toValue: ${fields.valueType} =
    _root_.notastruct.model.BitConverters.convert[${fields.containingType}, ${fields.valueType}](primitiveBits)
  override def toString = toValue.toString
}

object $companionName {
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

class signed extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro SignedImpl.macroImpl
}

class SignedImpl(val c: Context) {
  import c.universe._

  val NAME_PATTERN = raw"s([1-9][0-9]*)".r

  case class TypeFields(min: c.Tree,
                        max: c.Tree,
                        containingType: c.Tree)

  def typeFields(width: Int): Option[TypeFields] = {
    val min = -(1L << (width - 1))
    val max = (1L << (width - 1)) - 1
    val maskLong = (1L << width) - 1
    val maskInt = (1 << width) - 1
    if (width > 64) {
      None
    } else if (width == 64) {
      Some(TypeFields(min=q"$min",
                      max=q"$max",
                      containingType=tq"Long"))
    } else if (width > 32) {
      Some(TypeFields(min=q"$min",
                      max=q"$max",
                      containingType=tq"Long"))
    } else if (width == 32) {
      Some(TypeFields(min=q"$min.toInt",
                      max=q"$max.toInt",
                      containingType=tq"Int"))
    } else if (width > 16) {
      Some(TypeFields(min=q"${min.toInt}",
                      max=q"${max.toInt}",
                      containingType=tq"Int"))
    } else if (width == 16) {
      Some(TypeFields(min=q"${min.toShort}",
                      max=q"${max.toShort}",
                      containingType=tq"Short"))
    } else if (width > 8) {
      Some(TypeFields(min=q"${min.toShort}",
                      max=q"${max.toShort}",
                      containingType=tq"Short"))
    } else if (width == 8) {
      Some(TypeFields(min=q"${min.toByte}",
                      max=q"${max.toByte}",
                      containingType=tq"Byte"))
    } else if (width > 1) {
      Some(TypeFields(max=q"${max.toByte}",
                      min=q"${min.toByte}",
                      containingType=tq"Byte"))
    } else {
      None
    }
  }

  def macroImpl(annottees: c.Expr[Any]*): c.Expr[Any] = {
    annottees.map(_.tree).toList match {
      case (classDef: ClassDef) :: Nil =>
        c.Expr(buildDeclarations(classDef))
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
              typeFields(width) match {
                case Some(fields) => {
                  q"""
class $className(val primitiveBits: ${fields.containingType}) extends AnyVal {
  def toValue: ${fields.containingType} = primitiveBits
  override def toString = toValue.toString
}

object $companionName {
  implicit object ${TermName(className.toString + "HasAttributes")} extends _root_.notastruct.model.PackableAttributes[$className] {
    override def width: Int = $width
  }

  implicit object ${TermName(className.toString + "As" + fields.containingType + "Is" + fields.containingType + "Packable")} extends _root_.notastruct.model.Packable[$className, ${fields.containingType}, ${fields.containingType}] {
    override def minValue(implicit attributes: _root_.notastruct.model.PackableAttributes[$className]): ValueType = ${fields.min}
    override def maxValue(implicit attributes: _root_.notastruct.model.PackableAttributes[$className]): ValueType = ${fields.max}
  }
}
"""
                }
                case None =>
                  c.abort(c.enclosingPosition, s"Invalid type width $width")
              }
            }
            case _ =>
              c.abort(c.enclosingPosition, s"Unrecognized signed type name $className")
          }
        } else {
          c.abort(c.enclosingPosition, "Macro target should not explicitly define any elements")
        }
      }
    }
}
