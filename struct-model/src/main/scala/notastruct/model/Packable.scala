package notastruct.model

trait Packable[T, @specialized(Long, Float, Int, Short, Byte) V, @specialized(Long, Int, Short, Byte) P] {
  type DispatchType = T
  type ValueType = V
  type PackedType = P
  def minValue(implicit attributes: PackableAttributes[T]): V
  def maxValue(implicit attributes: PackableAttributes[T]): V
}
