package notastruct.model

trait BitConverter[@specialized(Long, Int, Short, Byte) F, @specialized(Long, Int, Short, Byte) T] {
  def convert(x: F): T
}

object BitConverters {
  implicit object Long2LongConverter extends BitConverter[Long, Long] {
    override def convert(x: Long): Long = x
  }

  implicit object Long2IntConverter extends BitConverter[Long, Int] {
    override def convert(x: Long): Int = x.asInstanceOf[Int]
  }

  implicit object Long2ShortConverter extends BitConverter[Long, Short] {
    override def convert(x: Long): Short = x.asInstanceOf[Short]
  }

  implicit object Long2BitConverter extends BitConverter[Long, Byte] {
    override def convert(x: Long): Byte = x.asInstanceOf[Byte]
  }

  implicit object Int2LongConverter extends BitConverter[Int, Long] {
    override def convert(x: Int): Long = java.lang.Integer.toUnsignedLong(x)
  }

  implicit object Int2IntConverter extends BitConverter[Int, Int] {
    override def convert(x: Int): Int = x
  }

  implicit object Int2ShortConverter extends BitConverter[Int, Short] {
    override def convert(x: Int): Short = x.asInstanceOf[Short]
  }

  implicit object Int2BitConverter extends BitConverter[Int, Byte] {
    override def convert(x: Int): Byte = x.asInstanceOf[Byte]
  }

  implicit object Short2LongConverter extends BitConverter[Short, Long] {
    override def convert(x: Short): Long = java.lang.Short.toUnsignedLong(x)
  }

  implicit object Short2IntConverter extends BitConverter[Short, Int] {
    override def convert(x: Short): Int = java.lang.Short.toUnsignedInt(x)
  }

  implicit object Short2ShortConverter extends BitConverter[Short, Short] {
    override def convert(x: Short): Short = x
  }

  implicit object Short2BitConverter extends BitConverter[Short, Byte] {
    override def convert(x: Short): Byte = x.asInstanceOf[Byte]
  }

  implicit object Byte2LongConverter extends BitConverter[Byte, Long] {
    override def convert(x: Byte): Long = java.lang.Byte.toUnsignedLong(x)
  }

  implicit object Byte2IntConverter extends BitConverter[Byte, Int] {
    override def convert(x: Byte): Int = java.lang.Byte.toUnsignedInt(x)
  }

  implicit object Byte2ShortConverter extends BitConverter[Byte, Short] {
    override def convert(x: Byte): Short = implicitly[BitConverter[Int, Short]].convert(implicitly[BitConverter[Byte, Int]].convert(x))
  }

  implicit object Byte2BitConverter extends BitConverter[Byte, Byte] {
    override def convert(x: Byte): Byte = x
  }
}
