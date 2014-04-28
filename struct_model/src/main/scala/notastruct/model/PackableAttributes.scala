package notastruct.model

trait PackableAttributes[@specialized(Long, Float, Int, Short, Byte) T] {
  type FormatType = T
  def width: Int
}
