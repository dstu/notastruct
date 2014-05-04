package notastruct

import scala.reflect.macros.blackbox.Context

package object model {
  def width[T](implicit packable: Packable[T]): Int = packable.width
  def minValue[T](implicit packable: Packable[T]): packable.ValueType = packable.minValue
  def maxValue[T](implicit packable: Packable[T]): packable.ValueType = packable.maxValue
}
