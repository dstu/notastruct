package notastruct.model

import scala.reflect.macros.blackbox.Context

trait Packable[T] {
  type ValueType
  type ContainingType
  def width: Int
  def minValue: ValueType
  def maxValue: ValueType
  def pack(c: Context)(value: c.Expr[ValueType]): c.Expr[ContainingType]
  def unpack(c: Context)(packed: c.Expr[ContainingType]): c.Expr[ValueType]
}
