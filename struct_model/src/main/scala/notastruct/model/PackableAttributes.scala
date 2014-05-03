package notastruct.model

trait PackableAttributes[T] {
  type FormatType = T
  def width: Int
}
