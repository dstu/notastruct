package notastruct.model

case class StructType(name: String, fields: Seq[FieldType]) {
  lazy val width = fields.map((field: FieldType) => field.offset + field.width).max
}


