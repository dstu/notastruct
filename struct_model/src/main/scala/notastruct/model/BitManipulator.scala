package notastruct.model

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

  implicit object Int2Long extends BitManipulator[Int, Long] {
    override def bits(x: Int, offset: Int, width: Int): Long =
      implicitly[BitConverter[Int, Long]].convert(implicitly[BitManipulator[Int, Int]].bits(x, offset, width))
    override def leftShift(x: Int, width: Int): Long =
      implicitly[BitConverter[Int, Long]].convert(implicitly[BitManipulator[Int, Int]].leftShift(x, width))
    override def rightShiftWithSign(x: Int, width: Int): Long =
      implicitly[BitConverter[Int, Long]].convert(implicitly[BitManipulator[Int, Int]].rightShiftWithSign(x, width))
    override def rightShiftWithoutSign(x: Int, width: Int): Long =
      implicitly[BitConverter[Int, Long]].convert(implicitly[BitManipulator[Int, Int]].rightShiftWithoutSign(x, width))
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

  implicit object Short2Long extends BitManipulator[Short, Long] {
    override def bits(x: Short, offset: Int, width: Int): Long =
      implicitly[BitConverter[Int, Long]].convert(implicitly[BitManipulator[Short, Int]].bits(x, offset, width))
    override def leftShift(x: Short, width: Int): Long =
      implicitly[BitConverter[Int, Long]].convert(implicitly[BitManipulator[Short, Int]].leftShift(x, width))
    override def rightShiftWithSign(x: Short, width: Int): Long =
      implicitly[BitConverter[Int, Long]].convert(implicitly[BitManipulator[Short, Int]].rightShiftWithSign(x, width))
    override def rightShiftWithoutSign(x: Short, width: Int): Long =
      implicitly[BitConverter[Int, Long]].convert(implicitly[BitManipulator[Short, Int]].rightShiftWithoutSign(x, width))
  }

  implicit object Short2Short extends BitManipulator[Short, Short] {
    override def bits(x: Short, offset: Int, width: Int): Short =
      implicitly[BitConverter[Int, Short]].convert(implicitly[BitManipulator[Short, Int]].bits(x, offset, width))
    override def leftShift(x: Short, width: Int): Short =
      implicitly[BitConverter[Int, Short]].convert(implicitly[BitManipulator[Short, Int]].leftShift(x, width))
    override def rightShiftWithSign(x: Short, width: Int): Short =
      implicitly[BitConverter[Int, Short]].convert(implicitly[BitManipulator[Short, Int]].rightShiftWithSign(x, width))
    override def rightShiftWithoutSign(x: Short, width: Int): Short =
      implicitly[BitConverter[Int, Short]].convert(implicitly[BitManipulator[Short, Int]].rightShiftWithoutSign(x, width))
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

  implicit object Byte2Long extends BitManipulator[Byte, Long] {
    override def bits(x: Byte, offset: Int, width: Int): Long =
      implicitly[BitConverter[Int, Long]].convert(implicitly[BitManipulator[Byte, Int]].bits(x, offset, width))
    override def leftShift(x: Byte, width: Int): Long =
      implicitly[BitConverter[Int, Long]].convert(implicitly[BitManipulator[Byte, Int]].leftShift(x, width))
    override def rightShiftWithSign(x: Byte, width: Int): Long =
      implicitly[BitConverter[Int, Long]].convert(implicitly[BitManipulator[Byte, Int]].rightShiftWithSign(x, width))
    override def rightShiftWithoutSign(x: Byte, width: Int): Long =
      implicitly[BitConverter[Int, Long]].convert(implicitly[BitManipulator[Byte, Int]].rightShiftWithoutSign(x, width))
  }

  implicit object Byte2Byte extends BitManipulator[Byte, Byte] {
    override def bits(x: Byte, offset: Int, width: Int): Byte =
      implicitly[BitConverter[Int, Byte]].convert(implicitly[BitManipulator[Byte, Int]].bits(x, offset, width))
    override def leftShift(x: Byte, width: Int): Byte =
      implicitly[BitConverter[Int, Byte]].convert(implicitly[BitManipulator[Byte, Int]].leftShift(x, width))
    override def rightShiftWithSign(x: Byte, width: Int): Byte =
      implicitly[BitConverter[Int, Byte]].convert(implicitly[BitManipulator[Byte, Int]].rightShiftWithSign(x, width))
    override def rightShiftWithoutSign(x: Byte, width: Int): Byte =
      implicitly[BitConverter[Int, Byte]].convert(implicitly[BitManipulator[Byte, Int]].rightShiftWithoutSign(x, width))
  }
}
