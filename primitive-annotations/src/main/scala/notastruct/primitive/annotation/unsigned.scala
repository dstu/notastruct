package notastruct.primitive.annotation

import scala.annotation.StaticAnnotation

class unsigned extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro notastruct.primitive.annotation.util.UnsignedBundle.unsigned
}
