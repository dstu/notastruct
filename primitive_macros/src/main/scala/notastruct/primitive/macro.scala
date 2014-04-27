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
                        containingType: c.Tree,
                        valueToContaining: TermName => c.Tree,
                        containingToValue: TermName => c.Tree,
                        packedIntToContaining: Option[TermName => c.Tree],
                        packedLongToContaining: TermName => c.Tree,
                        containingToPackedInt: Option[TermName => c.Tree],
                        containingToPackedLong: TermName => c.Tree)

  def typeFields(width: Int): Option[TypeFields] = {
    val max = (1L << width) - 1
    val maskLong = (1L << width) - 1
    val maskInt = (1 << width) - 1
    if (width > 63) {
      None
    } else if (width > 32) {
      Some(TypeFields(max=q"$max",
                      valueType=tq"Long",
                      containingType=tq"Long",
                      valueToContaining=(t: TermName) => q"$t & $maskLong",
                      containingToValue=(t: TermName) => q"$t",
                      packedIntToContaining=None,
                      packedLongToContaining=(t: TermName) => q"$t & $maskLong",
                      containingToPackedInt=None,
                      containingToPackedLong=(t: TermName) => q"$t"))
    } else if (width == 32) {
      Some(TypeFields(max=q"$max",
                      valueType=tq"Long",
                      containingType=tq"Int",
                      valueToContaining=(t: TermName) => q"($t & $maskLong).asInstanceOf[Int]",
                      containingToValue=(t: TermName) => q"_root_.java.lang.Integer.toUnsignedLong($t)",
                      packedIntToContaining=Some((t: TermName) => q"$t & $maskInt"),
                      packedLongToContaining=(t: TermName) => q"($t & $maskLong).asInstanceOf[Int]",
                      containingToPackedInt=Some((t: TermName) => q"$t"),
                      containingToPackedLong=(t: TermName) => q"$t.asInstanceOf[Long]"))
    } else if (width > 16) {
      Some(TypeFields(max=q"${max.toInt}",
                      valueType=tq"Int",
                      containingType=tq"Int",
                      valueToContaining=(t: TermName) => q"$t & $maskInt",
                      containingToValue=(t: TermName) => q"$t",
                      packedIntToContaining=Some((t: TermName) => q"$t & $maskInt"),
                      packedLongToContaining=(t: TermName) => q"($t & $maskLong).asInstanceOf[Int]",
                      containingToPackedInt=Some((t: TermName) => q"$t"),
                      containingToPackedLong=(t: TermName) => q"$t.asInstanceOf[Long]"))
    } else if (width == 16) {
      Some(TypeFields(max=q"${max.toInt}",
                      valueType=tq"Int",
                      containingType=tq"Short",
                      valueToContaining=(t: TermName) => q"$t.asInstanceOf[Short]",
                      containingToValue=(t: TermName) => q"_root_.java.lang.Short.toUnsignedInt($t)",
                      packedIntToContaining=Some((t: TermName) => q"($t & $maskInt).asInstanceOf[Short]"),
                      packedLongToContaining=(t: TermName) => q"($t & $maskLong).asInstanceOf[Short]",
                      containingToPackedInt=Some((t: TermName) => q"_root_.java.lang.Short.toUnsignedInt($t)"),
                      containingToPackedLong=(t: TermName) => q"_root_.java.lang.Short.toUnsignedLong($t)"))
    } else if (width > 8) {
      Some(TypeFields(max=q"${max.toShort}",
                      valueType=tq"Short",
                      containingType=tq"Short",
                      valueToContaining=(t: TermName) => q"($t & $maskInt).asInstanceOf[Short]",
                      containingToValue=(t: TermName) => q"$t",
                      packedIntToContaining=Some((t: TermName) => q"($t & $maskInt).asInstanceOf[Short]"),
                      packedLongToContaining=(t: TermName) => q"($t & $maskLong).asInstanceOf[Short]",
                      containingToPackedInt=Some((t: TermName) => q"$t.asInstanceOf[Int]"),
                      containingToPackedLong=(t: TermName) => q"$t.asInstanceOf[Long]"))
    } else if (width == 8) {
      Some(TypeFields(max=q"${max.toShort}",
                      valueType=tq"Short",
                      containingType=tq"Byte",
                      valueToContaining=(t: TermName) => q"($t & $maskInt).asInstanceOf[Byte]",
                      containingToValue=(t: TermName) => q"_root_.java.lang.Byte.toUnsignedInt($t).asInstanceOf[Short]",
                      packedIntToContaining=Some((t: TermName) => q"($t & $maskInt).asInstanceOf[Byte]"),
                      packedLongToContaining=(t: TermName) => q"($t & $maskLong).asInstanceOf[Byte]",
                      containingToPackedInt=Some((t: TermName) => q"_root_.java.lang.Byte.toUnsignedInt($t)"),
                      containingToPackedLong=(t: TermName) => q"_root_.java.lang.Byte.toUnsignedLong($t)"))
    } else if (width > 1) {
      Some(TypeFields(max=q"${max.toByte}",
                      valueType=tq"Byte",
                      containingType=tq"Byte",
                      valueToContaining=(t: TermName) => q"($t & $maskInt).asInstanceOf[Byte]",
                      containingToValue=(t: TermName) => q"$t",
                      packedIntToContaining=Some((t: TermName) => q"($t & $maskInt).asInstanceOf[Byte]"),
                      packedLongToContaining=(t: TermName) => q"($t & $maskLong).asInstanceOf[Byte]",
                      containingToPackedInt=Some((t: TermName) => q"$t & $maskInt"),
                      containingToPackedLong=(t: TermName) => q"$t & $maskLong"))
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
                  val classBody = Seq(
                      q"""def toValue: ${fields.valueType} = ${fields.containingToValue(TermName("primitiveBits"))}""",
                      q"""def toPackedLong: Long = ${fields.containingToPackedLong(TermName("primitiveBits"))}""",
                      q"""override def toString = toValue.toString""") ++ fields.containingToPackedInt.map(f =>
                      q"""def toPackedInt: Int = ${f(TermName("primitiveBits"))}""").toSeq
                  val companionBody = Seq(
                      q"""val WIDTH: Int = $width""",
                      q"""val MIN_VALUE: ${fields.valueType} = 0.asInstanceOf[${fields.valueType}]""",
                      q"""val MAX_VALUE: ${fields.valueType} = ${fields.max}""",
                      q"""def apply(x: ${fields.valueType}) = new $className(${fields.valueToContaining(TermName("x"))})""",
                      q"""def fromPacked(x: Long) = new $className(${fields.packedLongToContaining(TermName("x"))})""") ++ fields.packedIntToContaining.map(f =>
                      q"""def fromPacked(x: Int) = new $className(${f(TermName("x"))})""").toSeq
                  q"""
class $className(val primitiveBits: ${fields.containingType}) extends AnyVal {
  ..$classBody
}

object $companionName {
  ..$companionBody
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
                        containingType: c.Tree,
                        valueToContaining: TermName => c.Tree,
                        containingToValue: TermName => c.Tree,
                        packedIntToContaining: Option[TermName => c.Tree],
                        packedLongToContaining: TermName => c.Tree,
                        containingToPackedInt: Option[TermName => c.Tree],
                        containingToPackedLong: TermName => c.Tree)

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
                      containingType=tq"Long",
                      valueToContaining=(t: TermName) => q"$t",
                      containingToValue=(t: TermName) => q"$t",
                      packedIntToContaining=None,
                      packedLongToContaining=(t: TermName) => q"$t",
                      containingToPackedInt=None,
                      containingToPackedLong=(t: TermName) => q"$t"))
    } else if (width > 32) {
      Some(TypeFields(min=q"$min",
                      max=q"$max",
                      containingType=tq"Long",
                      valueToContaining=(t: TermName) => q"($t - $min) & $maskLong",
                      containingToValue=(t: TermName) => q"$t + $min",
                      packedIntToContaining=None,
                      packedLongToContaining=(t: TermName) => q"$t & $maskLong",
                      containingToPackedInt=None,
                      containingToPackedLong=(t: TermName) => q"$t"))
    } else if (width == 32) {
      Some(TypeFields(min=q"$min.toInt",
                      max=q"$max.toInt",
                      containingType=tq"Int",
                      valueToContaining=(t: TermName) => q"$t",
                      containingToValue=(t: TermName) => q"$t",
                      packedIntToContaining=Some((t: TermName) => q"$t"),
                      packedLongToContaining=(t: TermName) => q"$t.asInstanceOf[Int]",
                      containingToPackedInt=Some((t: TermName) => q"$t"),
                      containingToPackedLong=(t: TermName) => q"$t.asInstanceOf[Long]"))
    } else if (width > 16) {
      Some(TypeFields(min=q"${min.toInt}",
                      max=q"${max.toInt}",
                      containingType=tq"Int",
                      valueToContaining=(t: TermName) => q"($t - ${min.toInt}) & $maskInt",
                      containingToValue=(t: TermName) => q"$t + ${min.toInt}",
                      packedIntToContaining=Some((t: TermName) => q"$t & $maskInt"),
                      packedLongToContaining=(t: TermName) => q"($t & $maskLong).asInstanceOf[Int]",
                      containingToPackedInt=Some((t: TermName) => q"$t"),
                      containingToPackedLong=(t: TermName) => q"$t.asInstanceOf[Long]"))
    } else if (width == 16) {
      Some(TypeFields(min=q"${min.toShort}",
                      max=q"${max.toShort}",
                      containingType=tq"Short",
                      valueToContaining=(t: TermName) => q"$t",
                      containingToValue=(t: TermName) => q"$t",
                      packedIntToContaining=Some((t: TermName) => q"$t.asInstanceOf[Short]"),
                      packedLongToContaining=(t: TermName) => q"$t.asInstanceOf[Short]",
                      containingToPackedInt=Some((t: TermName) => q"_root_.java.lang.Short.toUnsignedInt($t)"),
                      containingToPackedLong=(t: TermName) => q"_root_.java.lang.Short.toUnsignedLong($t)"))
    } else if (width > 8) {
      Some(TypeFields(min=q"${min.toShort}",
                      max=q"${max.toShort}",
                      containingType=tq"Short",
                      valueToContaining=(t: TermName) => q"(($t - ${min.toInt}) & $maskInt).asInstanceOf[Short]",
                      containingToValue=(t: TermName) => q"($t + ${min.toInt}).asInstanceOf[Short]",
                      packedIntToContaining=Some((t: TermName) => q"($t & $maskInt).asInstanceOf[Short]"),
                      packedLongToContaining=(t: TermName) => q"($t & $maskLong).asInstanceOf[Short]",
                      containingToPackedInt=Some((t: TermName) => q"_root_.java.lang.Short.toUnsignedInt($t)"),
                      containingToPackedLong=(t: TermName) => q"_root_.java.lang.Short.toUnsignedLong($t)"))
    } else if (width == 8) {
      Some(TypeFields(min=q"${min.toByte}",
                      max=q"${max.toByte}",
                      containingType=tq"Byte",
                      valueToContaining=(t: TermName) => q"$t",
                      containingToValue=(t: TermName) => q"$t",
                      packedIntToContaining=Some((t: TermName) => q"($t & $maskInt).asInstanceOf[Byte]"),
                      packedLongToContaining=(t: TermName) => q"($t & $maskLong).asInstanceOf[Byte]",
                      containingToPackedInt=Some((t: TermName) => q"_root_.java.lang.Byte.toUnsignedInt($t)"),
                      containingToPackedLong=(t: TermName) => q"_root_.java.lang.Byte.toUnsignedLong($t)"))
    } else if (width > 1) {
      Some(TypeFields(max=q"${max.toByte}",
                      min=q"${min.toByte}",
                      containingType=tq"Byte",
                      valueToContaining=(t: TermName) => q"(($t - ${min.toInt}) & $maskInt).asInstanceOf[Byte]",
                      containingToValue=(t: TermName) => q"($t + ${min.toInt}).asInstanceOf[Byte]",
                      packedIntToContaining=Some((t: TermName) => q"($t & $maskInt).asInstanceOf[Byte]"),
                      packedLongToContaining=(t: TermName) => q"($t & $maskLong).asInstanceOf[Byte]",
                      containingToPackedInt=Some((t: TermName) => q"_root_.java.lang.Byte.toUnsignedInt($t)"),
                      containingToPackedLong=(t: TermName) => q"_root_.java.lang.Byte.toUnsignedLong($t)"))
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
                  val classBody = Seq(
                      q"""def toValue: ${fields.containingType} = ${fields.containingToValue(TermName("primitiveBits"))}""",
                      q"""def toPackedLong: Long = ${fields.containingToPackedLong(TermName("primitiveBits"))}""",
                      q"""override def toString = toValue.toString""") ++ fields.containingToPackedInt.map(f =>
                      q"""def toPackedInt: Int = ${f(TermName("primitiveBits"))}""").toSeq
                  val companionBody = Seq(
                      q"""val WIDTH: Int = $width""",
                      q"""val MIN_VALUE: ${fields.containingType} = ${fields.min}""",
                      q"""val MAX_VALUE: ${fields.containingType} = ${fields.max}""",
                      q"""def apply(x: ${fields.containingType}) = new $className(${fields.valueToContaining(TermName("x"))})""",
                      q"""def fromPackedLong(x: Long) = new $className(${fields.packedLongToContaining(TermName("x"))})""") ++ fields.packedIntToContaining.map(f =>
                      q"""def fromPackedInt(x: Int) = new $className(${f(TermName("x"))})""").toSeq
                  q"""
class $className(val primitiveBits: ${fields.containingType}) extends AnyVal {
  ..$classBody
}

object $companionName {
  ..$companionBody
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
