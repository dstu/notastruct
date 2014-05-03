package bitsafe.expression.util

import scala.reflect.macros.blackbox.Context

class NotBundle(val c: Context) {
  import c.universe._

  def notL[A: c.WeakTypeTag](e: c.Expr[A]) =
    q"~_root_.bitsafe.convert.bit[Long]($e)"
  def notI[A: c.WeakTypeTag](e: c.Expr[A]) =
    q"~_root_.bitsafe.convert.bit[Int]($e)"
}
