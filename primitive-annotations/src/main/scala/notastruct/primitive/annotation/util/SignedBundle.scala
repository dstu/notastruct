package notastruct.primitive.annotation.util

import bitsafe.convert._
import scala.reflect.macros.blackbox.Context

class SignedBundle(val c: Context) {
  import c.universe._

  val NAME_PATTERN = raw"s([1-9][0-9]*)".r

  case class TypeFields[T: c.TypeTag](min: c.Expr[T], max: c.Expr[T], mask: c.Expr[T]) {
    lazy val valueType = typeTag[T]
    lazy val pack = q"""
override def pack(c: _root_.scala.reflect.macros.blackbox.Context)(value: c.Expr[ValueType]) = {
  import c.universe._
  c.Expr[ValueType](reify {
    import bitsafe.convert._
    import bitsafe.expression._
    bit[$valueType]((value - $min) and $mask)
  })
}
"""
    lazy val unpack = q"""
override def unpack(c: _root_.scala.reflect.macros.blackbox.Context)(value: c.Expr[ValueType]) = {
  import c.universe._
  c.Expr[ValueType](reify {
    import bitsafe.convert._
    import bitsafe.expression._
    bit[$valueType](value - min)
  })
}
"""
  }

  def typeFields(width: Int): Option[TypeFields[_]] = {
    val min = -(1L << (width - 1))
    val max = (1L << (width - 1)) - 1
    val mask =
      if (width == 64) {
        -1L
      } else {
        ~((-1L >>> width) << width)
      }
    if (width > 64 || width < 1) {
      None
    } else {
      Some(if (width > 32) {
             TypeFields[Long](c.Expr[Long](q"$min"), c.Expr[Long](q"$max"), c.Expr[Long](q"$mask"))
           } else if (width > 16) {
             TypeFields[Int](c.Expr[Int](q"${min.toInt}"), c.Expr[Int](q"${max.toInt}"), c.Expr[Int](q"${bit[Int](mask)}"))
           } else if (width > 8) {
             TypeFields[Short](c.Expr[Short](q"${min.toShort}"), c.Expr[Short](q"${max.toShort}"), c.Expr[Short](q"${bit[Short](mask)}"))
           } else {
             TypeFields[Byte](c.Expr[Byte](q"${min.toByte}"), c.Expr[Byte](q"${max.toByte}"), c.Expr[Byte](q"${bit[Byte](mask)}"))
           })
    }
  }

  def signed(annottees: c.Expr[Any]*): c.Expr[Any] = {
    annottees.map(_.tree).toList match {
      case (classDef: ClassDef) :: Nil => {
        val d = buildDeclarations(classDef)
        println(d)
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
              typeFields(width) match {
                case Some(fields) => {
                  q"""
class $className(val primitiveBits: ${fields.valueType}) extends AnyVal {
  def toValue: ${fields.valueType} =
    (primitiveBits + ${fields.min}).asInstanceOf[${fields.valueType}]

  override def toString = toValue.toString
}

object $companionName {
  import bitsafe.convert._
  import bitsafe.expression._

  def apply(value: ${fields.valueType}): $className =   
    new $className(bit[${fields.valueType}]((value - ${fields.min}) and ${fields.mask}))

  implicit object ${TermName(className.toString + "IsPackable")} extends _root_.notastruct.model.Packable[$className] {
    override type ValueType = ${fields.valueType}
    override type ContainingType = ${fields.valueType}

    override def width = $width
    override def minValue = ${fields.min}
    override def maxValue = ${fields.max}

    ${fields.pack}
    ${fields.unpack}
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
