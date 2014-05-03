package notastruct

import scala.annotation.StaticAnnotation

class struct extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro notastruct.util.StructBundle.struct
}
