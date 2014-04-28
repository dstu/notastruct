package notastruct.model

trait Packable[T, @specialized(Long, Float, Int, Short, Byte) V, @specialized(Long, Int, Short, Byte) P, @specialized(Long, Int) Q] {
  type ValueType = V
  type PackedType = P
  type PromotedType = Q
  def intMask(implicit attributes: PackableAttributes[T]): Int = (1 << attributes.width) - 1
  def longMask(implicit attributes: PackableAttributes[T]): Long = (1L << attributes.width) - 1
  def minValue(implicit attributes: PackableAttributes[T]): ValueType
  def maxValue(implicit attributes: PackableAttributes[T]): ValueType
  def pack(x: ValueType)(implicit attributes: PackableAttributes[T]) : PackedType
  def unpack(x: PackedType)(implicit attributes: PackableAttributes[T]): ValueType
  def promote(x: PackedType)(implicit attributes: PackableAttributes[T]): PromotedType
  def demote(x: PromotedType)(implicit attributes: PackableAttributes[T]): PackedType
}
