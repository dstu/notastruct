package notastruct.model

import scala.reflect.macros.blackbox.Context

trait Packable[T] {
  type ValueType
  type PackedType

  def width: Int
  def minValue: ValueType
  def maxValue: ValueType

  def valueType(c: Context): c.TypeTag[ValueType]
  def packedType(c: Context): c.TypeTag[PackedType]

  def wrap(c: Context)(t: c.Expr[ValueType]): c.Expr[T]
  def unwrap(c: Context)(t: c.Expr[T]): c.Expr[ValueType]

  def pack(c: Context)(t: c.Expr[T]): c.Expr[PackedType]
  def unpack(c: Context)(packed: c.Expr[PackedType]): c.Expr[T]

  def packValue(c: Context)(t: c.Expr[ValueType]): c.Expr[PackedType]
  def unpackValue(c: Context)(t: c.Expr[PackedType]): c.Expr[ValueType]
}
