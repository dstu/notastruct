package notastruct.primitive.annotation

import scala.annotation.StaticAnnotation

class signed extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro notastruct.primitive.annotation.util.SignedBundle.signed
}
