package notastruct.model

trait Packable[T, @specialized(Long, Float, Int, Short, Byte) V, @specialized(Long, Int, Short, Byte) P] {
  type ValueType = V
  type PackedType = P
  def intMask(implicit attributes: PackableAttributes[T]): Int = (1 << attributes.width) - 1
  def longMask(implicit attributes: PackableAttributes[T]): Long = (1L << attributes.width) - 1
  def minValue(implicit attributes: PackableAttributes[T]): V
  def maxValue(implicit attributes: PackableAttributes[T]): P
}
