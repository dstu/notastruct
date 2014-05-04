package notastruct

import notastruct.model._
import scala.reflect.macros.blackbox.Context

package object primitive {
  implicit object LongIsPackable extends Packable[Long] {
    override type ValueType = Long
    override type PackedType = Long

    override def width = 64
    override def minValue = java.lang.Long.MIN_VALUE
    override def maxValue = java.lang.Long.MAX_VALUE

    override def packedType(c: Context) = c.typeTag[Long]
    override def valueType(c: Context) = c.typeTag[Long]

    override def wrap(c: Context)(x: c.Expr[Long]) = x
    override def unwrap(c: Context)(x: c.Expr[Long]) = x

    override def pack(c: Context)(x: c.Expr[Long]) = x
    override def unpack(c: Context)(x: c.Expr[Long]) = x

    override def packValue(c: Context)(x: c.Expr[Long]) = x
    override def unpackValue(c: Context)(x: c.Expr[Long]) = x
  }

  implicit object IntIsPackable extends Packable[Int] {
    override type ValueType = Int
    override type PackedType = Int

    override def width = 32
    override def minValue = java.lang.Integer.MIN_VALUE
    override def maxValue = java.lang.Integer.MAX_VALUE

    override def packedType(c: Context) = c.typeTag[Int]
    override def valueType(c: Context) = c.typeTag[Int]

    override def wrap(c: Context)(x: c.Expr[Int]) = x
    override def unwrap(c: Context)(x: c.Expr[Int]) = x

    override def pack(c: Context)(x: c.Expr[Int]) = x
    override def unpack(c: Context)(x: c.Expr[Int]) = x

    override def packValue(c: Context)(x: c.Expr[Int]) = x
    override def unpackValue(c: Context)(x: c.Expr[Int]) = x
  }

  implicit object FloatIsPackable extends Packable[Float] {
    override type ValueType = Float
    override type PackedType = Int

    override def width = 32
    override def minValue = java.lang.Float.MIN_VALUE
    override def maxValue = java.lang.Float.MAX_VALUE

    override def packedType(c: Context) = c.typeTag[Int]
    override def valueType(c: Context) = c.typeTag[Float]

    override def wrap(c: Context)(x: c.Expr[Float]) = x
    override def unwrap(c: Context)(x: c.Expr[Float]) = x

    override def pack(c: Context)(x: c.Expr[Float]) = {
      import c.universe._
      c.Expr[Int](q"_root_.bitsafe.convert.bit[Int]($x)")
    }
    override def unpack(c: Context)(x: c.Expr[Int]) = {
      import c.universe._
      c.Expr[Int](q"_root_.bitsafe.convert.bit[Float]($x)")

    override def packValue(c: Context)(x: c.Expr[Float]) = pack
    override def unpackValue(c: Context)(x: c.Expr[Long]) = unpack
    }
  }

  implicit object ShortIsPackable extends Packable[Short] {
    override type ValueType = Short
    override type PackedType = Short

    override def width = 16
    override def minValue = java.lang.Short.MIN_VALUE
    override def maxValue = java.lang.Short.MAX_VALUE

    override def packedType(c: Context) = c.typeTag[Short]
    override def valueType(c: Context) = c.typeTag[Short]

    override def wrap(c: Context)(x: c.Expr[Short]) = x
    override def unwrap(c: Context)(x: c.Expr[Short]) = x

    override def pack(c: Context)(x: c.Expr[Short]) = x
    override def unpack(c: Context)(x: c.Expr[Short]) = x

    override def packValue(c: Context)(x: c.Expr[Short]) = x
    override def unpackValue(c: Context)(x: c.Expr[Short]) = x
  }

  implicit object ByteIsPackable extends Packable[Byte] {
    override type ValueType = Byte
    override type PackedType = Byte
    override def width = 8

    override def minValue = java.lang.Byte.MIN_VALUE
    override def maxValue = java.lang.Byte.MAX_VALUE

    override def packedType(c: Context) = c.typeTag[Byte]
    override def valueType(c: Context) = c.typeTag[Byte]

    override def wrap(c: Context)(x: c.Expr[Byte]) = x
    override def unwrap(c: Context)(x: c.Expr[Byte]) = x

    override def pack(c: Context)(x: c.Expr[Byte]) = x
    override def unpack(c: Context)(x: c.Expr[Byte]) = x

    override def packValue(c: Context)(x: c.Expr[Byte]) = x
    override def unpackValue(c: Context)(x: c.Expr[Byte]) = x
  }
}
