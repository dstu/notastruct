package notastruct

import notastruct.model._

package object primitive {
  implicit object LongHasAttributes extends PackableAttributes[Long] {
    override def width = 64
  }

  implicit object LongIsLongPackable extends Packable[Long, Long, Long] {
    override def minValue(implicit attributes: PackableAttributes[Long]): Long = java.lang.Long.MIN_VALUE
    override def maxValue(implicit attributes: PackableAttributes[Long]): Long = java.lang.Long.MAX_VALUE
  }

  implicit object FloatHasAttributes extends PackableAttributes[Float] {
    override def width = 32
  }

  implicit object FloatIsLongPackable extends Packable[Float, Float, Long] {
    override def minValue(implicit attributes: PackableAttributes[Float]): Float = java.lang.Float.MIN_VALUE
    override def maxValue(implicit attributes: PackableAttributes[Float]): Float = java.lang.Float.MAX_VALUE
  }

  implicit object FloatIsIntPackable extends Packable[Float, Float, Int] {
    override def minValue(implicit attributes: PackableAttributes[Float]): Float = java.lang.Float.MIN_VALUE
    override def maxValue(implicit attributes: PackableAttributes[Float]): Float = java.lang.Float.MAX_VALUE
  }

  implicit object IntHasAttributes extends PackableAttributes[Int] {
    override def width = 32
  }

  implicit object IntIsLongPackable extends Packable[Int, Int, Long] {
    override def minValue(implicit attributes: PackableAttributes[Int]): Int = java.lang.Integer.MIN_VALUE
    override def maxValue(implicit attributes: PackableAttributes[Int]): Int = java.lang.Integer.MAX_VALUE
  }

  implicit object IntIsIntPackable extends Packable[Int, Int, Int] {
    override def minValue(implicit attributes: PackableAttributes[Int]): Int = java.lang.Integer.MIN_VALUE
    override def maxValue(implicit attributes: PackableAttributes[Int]): Int = java.lang.Integer.MAX_VALUE
  }

  implicit object ShortAttributes extends PackableAttributes[Short] {
    override def width = 16
  }

  implicit object ShortIsIntPackable extends Packable[Short, Short, Int] {
    override def minValue(implicit attributes: PackableAttributes[Short]): Short = java.lang.Short.MIN_VALUE
    override def maxValue(implicit attributes: PackableAttributes[Short]): Short = java.lang.Short.MAX_VALUE
  }

  implicit object ShortIsLongPackable extends Packable[Short, Short, Long] {
    override def minValue(implicit attributes: PackableAttributes[Short]): Short = java.lang.Short.MIN_VALUE
    override def maxValue(implicit attributes: PackableAttributes[Short]): Short = java.lang.Short.MAX_VALUE
  }

  implicit object ByteAttributes extends PackableAttributes[Byte] {
    override def width = 8
  }

  implicit object ByteIsIntPackable extends Packable[Byte, Byte, Int] {
    override def minValue(implicit attributes: PackableAttributes[Byte]): Byte = java.lang.Byte.MIN_VALUE
    override def maxValue(implicit attributes: PackableAttributes[Byte]): Byte = java.lang.Byte.MAX_VALUE
  }

  implicit object ByteIsLongPackable extends Packable[Byte, Byte, Long] {
    override def minValue(implicit attributes: PackableAttributes[Byte]): Byte = java.lang.Byte.MIN_VALUE
    override def maxValue(implicit attributes: PackableAttributes[Byte]): Byte = java.lang.Byte.MAX_VALUE
  }

  def width[T](implicit attributes: PackableAttributes[T]): Int = attributes.width
}
