package notastruct.primitive

class f32(val f: Float) extends AnyVal {
  def toValue: Float = f
  def toPackedInt: Int = java.lang.Float.floatToRawIntBits(f)
  def toPackedLong: Long = java.lang.Integer.toUnsignedLong(toPackedInt)
  override def toString = toValue.toString
}

object f32 {
  private[this] val mask = -1L >>> 32
  val width = 32
  val MIN_VALUE = java.lang.Float.MIN_VALUE
  val MAX_VALUE = java.lang.Float.MAX_VALUE
  def apply(x: Float): f32 = new f32(x)
  def fromPacked(x: Long): f32 = fromPacked((x & mask).asInstanceOf[Int])
  def fromPacked(x: Int): f32 = new f32(java.lang.Float.intBitsToFloat(x))
}
