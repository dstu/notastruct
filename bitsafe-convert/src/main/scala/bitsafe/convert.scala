package bitsafe

package object convert {
  def bit[T](e: Byte): T   = macro bitsafe.convert.util.ConvertBundle.bit[Byte, T]
  def bit[T](e: Short): T  = macro bitsafe.convert.util.ConvertBundle.bit[Short, T]
  def bit[T](e: Int): T    = macro bitsafe.convert.util.ConvertBundle.bit[Int, T]
  def bit[T](e: Float): T  = macro bitsafe.convert.util.ConvertBundle.bit[Float, T]
  def bit[T](e: Long): T   = macro bitsafe.convert.util.ConvertBundle.bit[Long, T]
  def bit[T](e: Double): T = macro bitsafe.convert.util.ConvertBundle.bit[Double, T]
}
