package notastruct.primitive.annotation.util

import scala.reflect.macros.blackbox.Context

class UnsignedBundle(val c: Context) {
  import c.universe._

  val NAME_PATTERN = raw"u([1-9][0-9]*)".r

  case class TypeFields[T: c.TypeTag, C: c.TypeTag](max: c.Expr[T],
                                                    pack: c.Tree,
                                                    unpack: c.Tree) {
    lazy val valueType = typeTag[T]
    lazy val containingType = typeTag[C]
  }

  def typeFields(width: Int): Option[TypeFields[_, _]] = {
    val max = (1L << width) - 1
    val mask = ~((-1L >>> width) << width)
    if (width > 63 || width < 1) {
      None
    } else {
      Some(if (width > 32) {
             TypeFields[Long, Long](max=c.Expr[Long](q"$max"),
                                    pack = ???,//q"""
//override def pack(c: _root_.scala.reflect.macros.blackbox.Context)(x: c.Expr[Long]) = c.Expr[Long](q"$$x && $mask")
//""",
                                    unpack = ???)//q"""
//override def unpack(c: _root_.scala.reflect.macros.blackbox.Context)(x: c.Expr[Long]) = x
//""")
           } else if (width == 32) {
             TypeFields[Long, Int](max=c.Expr[Long](q"$max"),
                                   pack = ???,//q"""
////override def pack(c: _root_.scala.reflect.macros.blackbox.Context)(x: c.Expr[Long]) = c.Expr[Long](q"_root_.bitsafe.convert.bit[Int]($$x)")
//""",
                                   unpack = ???)//q"""
// override def unpack(c: _root_.scala.reflect.macros.blackbox.Context)(x: c.Expr[Int]) = c.Expr[Long](q"_root_.bitsafe.convert.bit[Long]($$x)")
// """)
           } else if (width > 16) {
             TypeFields[Int, Int](max=c.Expr[Int](q"${max.toInt}"),
                                  pack = ???,//q"""
////override def pack(c: _root_.scala.reflect.macros.blackbox.Context)(x: c.Expr[Int]) = c.Expr[Int](q"_root_.bitsafe.convert.bit[Int]($$x && ${mask.toInt})")
//""",
                                  unpack = ???)//q"""
// override def unpack(c: _root_.scala.reflect.macros.blackbox.Context)(x: c.Expr[Int]) = x
// """)
           } else if (width == 16) {
             TypeFields[Int, Short](max=c.Expr[Int](q"${max.toInt}"),
                                    pack = ???,//q"""
////override def pack(c: _root_.scala.reflect.macros.blackbox.Context)(x: c.Expr[Int]) = c.Expr[Short](q"_root_.bitsafe.convert.bit[Short]($$x && ${mask.toInt})")
//""",
                                    unpack = ???)//q"""
// override def unpack(c: _root_.scala.reflect.macros.blackbox.Context)(x: c.Expr[Short]) = c.Expr[Int](q"_root_.bitsafe.convert.bit[Int]($$x)")
// """)
           } else if (width > 8) {
             TypeFields[Short, Short](max=c.Expr[Short](q"${max.toShort}"),
                                      pack = ???,//q"""
////override def pack(x: c.Expr[Short]) = c.Expr[Short](q"import _root_.bitsafe.expression._; _root_.bitsafe.convert.bit[Short]($$x and ${mask.toInt})")
//""",
                                      unpack = ???)//q"""
// override def unpack(c: _root_.scala.reflect.macros.blackbox.Context)(x: c.Expr[Short]) = x
// """)
           } else if (width == 8) {
             TypeFields[Short, Byte](max=c.Expr[Short](q"${max.toShort}"),
                                     pack = ???,//q"""
////override def pack(c: _root_.scala.reflect.macros.blackbox.Context)(x: c.Expr[Short]) = c.Expr[Byte](q"_root_.bitsafe.convert.bit[Byte]($$x)")
//""",
                                     unpack = ???)//q"""
// override def unpack(x: c.Expr[Byte]) = c.Expr[Short](q"_root_.bitsafe.convert.bit[Short]($$x)")
// """)
           } else {
             TypeFields(max=c.Expr[Byte](q"${max.toByte}"),
                        pack = ???,//q"""
////override def pack(c: _root_.scala.reflect.macros.blackbox.Content)(x: c.Expr[Byte]) = c.Expr[Byte](q"import _root_.bitsafe.expression._; _root_.bitsafe.convert.bit[Byte]($$x and ${mask.toInt})")
//""",
                        unpack = ???)//q"""
// override def unpack(c: _root_.scala.reflect.macros.blackbox.Context)(x: c.Expr[Byte]) = x
// """)
           })
    }
  }

  def unsigned(annottees: c.Expr[Any]*): c.Expr[Any] = {
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

  final val Width = $width
  final val MinValue: ${fields.valueType} = 0
  final val MaxValue = ${fields.max}

  def apply(value: ${fields.valueType}): $className =
    new $className(bit[${fields.containingType}](value and $mask))

  implicit object ${TermName(className.toString + "IsPackable")} extends _root_.notastruct.model.Packable[$className] {
    override type ValueType = ${fields.valueType}
    override type ContainingType = ${fields.valueType}

    override def width: Int = $width
    override def minValue = 0.asInstanceOf[ValueType]
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
              c.abort(c.enclosingPosition, s"Unrecognized unsigned type name $className")
          }
        } else {
          c.abort(c.enclosingPosition, "Macro target should not explicitly define any elements")
        }
      }
    }
}
