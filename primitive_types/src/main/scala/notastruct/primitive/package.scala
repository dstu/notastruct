package notastruct

import notastruct.model._

package object primitive {
  implicit object LongHasAttributes extends PackableAttributes[Long] {
    override def width = 64
  }

  implicit object LongIsLongPackable extends Packable[Long, Long, Long, Long] {
    override def pack(x: Long)(implicit attributes: PackableAttributes[Long]): Long = x
    override def unpack(x: Long)(implicit attributes: PackableAttributes[Long]): Long = x
    override def promote(x: Long)(implicit attributes: PackableAttributes[Long]): Long = x
    override def demote(x: Long)(implicit attributes: PackableAttributes[Long]): Long = x
    override def minValue(implicit attributes: PackableAttributes[Long]): Long = java.lang.Long.MIN_VALUE
    override def maxValue(implicit attributes: PackableAttributes[Long]): Long = java.lang.Long.MAX_VALUE
  }

  implicit object FloatHasAttributes extends PackableAttributes[Float] {
    override def width = 32
  }

  implicit object FloatIsLongPackable extends Packable[Float, Float, Int, Long] {
    override def pack(x: Float)(implicit attributes: PackableAttributes[Float]): Int = java.lang.Float.floatToRawIntBits(x)
    override def unpack(x: Int)(implicit attributes: PackableAttributes[Float]): Float = java.lang.Float.intBitsToFloat(x)
    override def promote(x: Int)(implicit attributes: PackableAttributes[Float]): Long = java.lang.Integer.toUnsignedLong(x)
    override def demote(x: Long)(implicit attributes: PackableAttributes[Float]): Int = x.asInstanceOf[Int]
    override def minValue(implicit attributes: PackableAttributes[Float]): Float = java.lang.Float.MIN_VALUE
    override def maxValue(implicit attributes: PackableAttributes[Float]): Float = java.lang.Float.MAX_VALUE
  }

  implicit object FloatIsIntPackable extends Packable[Float, Float, Int, Int] {
    override def pack(x: Float)(implicit attributes: PackableAttributes[Float]): Int = java.lang.Float.floatToRawIntBits(x)
    override def unpack(x: Int)(implicit attributes: PackableAttributes[Float]): Float = java.lang.Float.intBitsToFloat(x)
    override def promote(x: Int)(implicit attributes: PackableAttributes[Float]): Int = x
    override def demote(x: Int)(implicit attributes: PackableAttributes[Float]): Int = x
    override def minValue(implicit attributes: PackableAttributes[Float]): Float = java.lang.Float.MIN_VALUE
    override def maxValue(implicit attributes: PackableAttributes[Float]): Float = java.lang.Float.MAX_VALUE
  }

  implicit object IntHasAttributes extends PackableAttributes[Int] {
    override def width = 32
  }

  implicit object IntIsLongPackable extends Packable[Int, Int, Int, Long] {
    override def pack(x: Int)(implicit attributes: PackableAttributes[Int]): Int = x
    override def unpack(x: Int)(implicit attributes: PackableAttributes[Int]): Int = x
    override def promote(x: Int)(implicit attributes: PackableAttributes[Int]): Long = java.lang.Integer.toUnsignedLong(x)
    override def demote(x: Long)(implicit attributes: PackableAttributes[Int]): Int = x.asInstanceOf[Int]
    override def minValue(implicit attributes: PackableAttributes[Int]): Int = java.lang.Integer.MIN_VALUE
    override def maxValue(implicit attributes: PackableAttributes[Int]): Int = java.lang.Integer.MAX_VALUE
  }

  implicit object IntIsIntPackable extends Packable[Int, Int, Int, Int] {
    override def pack(x: Int)(implicit attributes: PackableAttributes[Int]): Int = x
    override def unpack(x: Int)(implicit attributes: PackableAttributes[Int]): Int = x
    override def promote(x: Int)(implicit attributes: PackableAttributes[Int]): Int = x
    override def demote(x: Int)(implicit attributes: PackableAttributes[Int]): Int = x
    override def minValue(implicit attributes: PackableAttributes[Int]): Int = java.lang.Integer.MIN_VALUE
    override def maxValue(implicit attributes: PackableAttributes[Int]): Int = java.lang.Integer.MAX_VALUE
  }

  implicit object ShortAttributes extends PackableAttributes[Short] {
    override def width = 16
  }

  implicit object ShortIsIntPackable extends Packable[Short, Short, Short, Int] {
    override def pack(x: Short)(implicit attributes: PackableAttributes[Short]): Short = x
    override def unpack(x: Short)(implicit attributes: PackableAttributes[Short]): Short = x
    override def promote(x: Short)(implicit attributes: PackableAttributes[Short]): Int = java.lang.Short.toUnsignedInt(x)
    override def demote(x: Int)(implicit attributes: PackableAttributes[Short]): Short = x.asInstanceOf[Short]
    override def minValue(implicit attributes: PackableAttributes[Short]): Short = java.lang.Short.MIN_VALUE
    override def maxValue(implicit attributes: PackableAttributes[Short]): Short = java.lang.Short.MAX_VALUE
  }

  implicit object ShortIsLongPackable extends Packable[Short, Short, Short, Long] {
    override def pack(x: Short)(implicit attributes: PackableAttributes[Short]): Short = x
    override def unpack(x: Short)(implicit attributes: PackableAttributes[Short]): Short = x
    override def promote(x: Short)(implicit attributes: PackableAttributes[Short]): Long = java.lang.Short.toUnsignedLong(x)
    override def demote(x: Long)(implicit attributes: PackableAttributes[Short]): Short = x.asInstanceOf[Short]
    override def minValue(implicit attributes: PackableAttributes[Short]): Short = java.lang.Short.MIN_VALUE
    override def maxValue(implicit attributes: PackableAttributes[Short]): Short = java.lang.Short.MAX_VALUE
  }

  implicit object ByteAttributes extends PackableAttributes[Byte] {
    override def width = 8
  }

  implicit object ByteIsIntPackable extends Packable[Byte, Byte, Byte, Int] {
    override def pack(x: Byte)(implicit attributes: PackableAttributes[Byte]): Byte = x
    override def unpack(x: Byte)(implicit attributes: PackableAttributes[Byte]): Byte = x
    override def promote(x: Byte)(implicit attributes: PackableAttributes[Byte]): Int = java.lang.Byte.toUnsignedInt(x)
    override def demote(x: Int)(implicit attributes: PackableAttributes[Byte]): Byte = x.asInstanceOf[Byte]
    override def minValue(implicit attributes: PackableAttributes[Byte]): Byte = java.lang.Byte.MIN_VALUE
    override def maxValue(implicit attributes: PackableAttributes[Byte]): Byte = java.lang.Byte.MAX_VALUE
  }

  implicit object ByteIsLongPackable extends Packable[Byte, Byte, Byte, Long] {
    override def pack(x: Byte)(implicit attributes: PackableAttributes[Byte]): Byte = x
    override def unpack(x: Byte)(implicit attributes: PackableAttributes[Byte]): Byte = x
    override def promote(x: Byte)(implicit attributes: PackableAttributes[Byte]): Long = java.lang.Byte.toUnsignedLong(x)
    override def demote(x: Long)(implicit attributes: PackableAttributes[Byte]): Byte = x.asInstanceOf[Byte]
    override def minValue(implicit attributes: PackableAttributes[Byte]): Byte = java.lang.Byte.MIN_VALUE
    override def maxValue(implicit attributes: PackableAttributes[Byte]): Byte = java.lang.Byte.MAX_VALUE
  }

  def width[T](implicit attributes: PackableAttributes[T]): Int = attributes.width
}
