package notastruct.model.util

trait BitConverter[@specialized(Long, Int, Short, Byte) F, @specialized(Long, Int, Short, Byte) T] {
  def convert(x: F): T
}

object BitConverters {
  implicit object Long2LongConverter extends BitConverter[Long, Long] {
    override def convert(x: Long): Long = x
  }

  implicit object Int2LongConverter extends BitConverter[Int, Long] {
    override def convert(x: Int): Long = java.lang.Integer.toUnsignedLong(x)
  }

  implicit object Short2LongConverter extends BitConverter[Short, Long] {
    override def convert(x: Short): Long = java.lang.Short.toUnsignedLong(x)
  }

  implicit object Short2IntConverter extends BitConverter[Short, Int] {
    override def convert(x: Short): Int = java.lang.Short.toUnsignedInt(x)
  }

  implicit object Byte2LongConverter extends BitConverter[Byte, Long] {
    override def convert(x: Byte): Long = java.lang.Byte.toUnsignedLong(x)
  }

  implicit object Byte2IntConverter extends BitConverter[Byte, Int] {
    override def convert(x: Byte): Int = java.lang.Byte.toUnsignedInt(x)
  }
}

trait BitManipulator[@specialized(Long, Int, Short, Byte) F, @specialized(Long, Int) T] {
  def bits(x: F, offset: Int, width: Int): T
  def leftShift(x: F, width: Int): T
  def rightShiftWithSign(x: F, width: Int): T
  def rightShiftWithoutSign(x: F, width: Int): T
}

object BitManipulators {
  import BitConverters._

  implicit object Long2Long extends BitManipulator[Long, Long] {
    override def bits(x: Long, offset: Int, width: Int): Long =
      x & (((1L << width) - 1) << offset)
    override def leftShift(x: Long, width: Int): Long =
      x << width
    override def rightShiftWithSign(x: Long, width: Int): Long =
      x >> width
    override def rightShiftWithoutSign(x: Long, width: Int): Long =
      x >>> width
  }

  implicit object Int2Int extends BitManipulator[Int, Int] {
    override def bits(x: Int, offset: Int, width: Int): Int =
      x & (((1 << width) - 1) << offset)
    override def leftShift(x: Int, width: Int): Int = x << width
    override def rightShiftWithSign(x: Int, width: Int): Int = x >> width
    override def rightShiftWithoutSign(x: Int, width: Int): Int = x >>> width
  }

  implicit object Short2Int extends BitManipulator[Short, Int] {
    override def bits(x: Short, offset: Int, width: Int): Int =
      implicitly[BitConverter[Short, Int]].convert(x) & (((1 << width) - 1) << offset)
    override def leftShift(x: Short, width: Int): Int =
      implicitly[BitConverter[Short, Int]].convert(x) << width
    override def rightShiftWithSign(x: Short, width: Int): Int =
      implicitly[BitConverter[Short, Int]].convert(x) >> width
    override def rightShiftWithoutSign(x: Short, width: Int): Int =
      implicitly[BitConverter[Short, Int]].convert(x) >>> width
  }

  implicit object Byte2Int extends BitManipulator[Byte, Int] {
    override def bits(x: Byte, offset: Int, width: Int): Int =
      implicitly[BitConverter[Byte, Int]].convert(x) & (((1 << width) - 1) << offset)
    override def leftShift(x: Byte, width: Int): Int =
      implicitly[BitConverter[Byte, Int]].convert(x) << width
    override def rightShiftWithSign(x: Byte, width: Int): Int =
      implicitly[BitConverter[Byte, Int]].convert(x) >> width
    override def rightShiftWithoutSign(x: Byte, width: Int): Int =
      implicitly[BitConverter[Byte, Int]].convert(x) >>> width
  }
}
