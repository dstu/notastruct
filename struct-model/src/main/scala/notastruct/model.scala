package notastruct

package object model {
  def width[T](implicit attributes: PackableAttributes[T]): Int = attributes.width
}
