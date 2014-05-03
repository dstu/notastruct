package bitsafe

import scala.reflect.macros.blackbox.Context

class BitwiseOpsBundle(val c: Context) {
  import c.universe._

  val DoubleType = typeTag[Double].tpe
  val LongType = typeTag[Long].tpe
  val FloatType = typeTag[Float].tpe
  val IntType = typeTag[Int].tpe
  val ShortType = typeTag[Short].tpe
  val ByteType = typeTag[Byte].tpe

  val validFromTypes = Set(DoubleType, LongType, FloatType, IntType, ShortType, ByteType)
  val validToTypes = Set(LongType, IntType, ShortType, ByteType)

  def checkFromType(tpe: Type) =
    if (!validFromTypes.contains(tpe)) {
      c.abort(c.enclosingPosition, s"Unrecognized expression type $tpe")
    } else {
      tpe
    }

  def checkToType(tpe: Type) =
    if (!validToTypes.contains(tpe)) {
      c.abort(c.enclosingPosition, s"Non-primitive target type $tpe")
    } else {
      tpe
    }

  def bit[F: c.WeakTypeTag, T: c.WeakTypeTag](e: c.Expr[F]): c.Expr[T] = {
    val fromType = checkFromType(weakTypeTag[F].tpe)
    val toType = checkToType(weakTypeTag[T].tpe)
    if (fromType == toType) {
      c.Expr(q"$e")
    } else {
      (fromType, toType) match {
        case (DoubleType, LongType) => c.Expr(q"_root_.java.lang.Double.doubleToRawLongBits($e)")
        case (LongType, FloatType)  => c.Expr(q"_root_.java.lang.Float.intBitsToFloat($e.asInstanceOf[Int])")
        case (LongType, IntType)    => c.Expr(q"$e.asInstanceOf[Int]")
        case (LongType, ShortType)  => c.Expr(q"$e.asInstanceOf[Short]")
        case (LongType, ByteType)   => c.Expr(q"$e.asInstanceOf[Byte]")
        case (FloatType, LongType)  => c.Expr(q"_root_.java.lang.Integer.toUnsignedLong(_root_.java.lang.Float.floatToRawIntBits($e))")
        case (FloatType, IntType)   => c.Expr(q"_root_.java.lang.Float.floatToRawIntBits($e)")
        case (IntType, LongType)    => c.Expr(q"_root_.java.lang.Integer.toUnsignedLong($e)")
        case (IntType, ShortType)   => c.Expr(q"$e.asInstanceOf[Short]")
        case (IntType, ByteType)    => c.Expr(q"$e.asInstanceOf[Byte]")
        case (ShortType, LongType)  => c.Expr(q"_root_.java.lang.Short.toUnsignedLong($e)")
        case (ShortType, IntType)   => c.Expr(q"_root_.java.lang.Short.toUnsignedInt($e)")
        case (ShortType, ByteType)  => c.Expr(q"$e.asInstanceOf[Byte]")
        case (ByteType, LongType)   => c.Expr(q"_root_.java.lang.Byte.toUnsignedLong($e)")
        case (ByteType, IntType)    => c.Expr(q"_root_.java.lang.Byte.toUnsignedInt($e)")
        case (ByteType, ShortType)  => c.Expr(q"_root_.java.lang.Byte.toUnsignedInt($e).asInstanceOf[Short]")
        case _ => c.abort(c.enclosingPosition, s"Conversion from $fromType to $toType not supported")
      }
    }
  }

  def notL[A: c.WeakTypeTag](e: c.Expr[A]) =
    q"~_root_.bitsafe.ops.bit[Long]($e)"
  def notI[A: c.WeakTypeTag](e: c.Expr[A]) =
    q"~_root_.bitsafe.ops.bit[Int]($e)"

  // def andL[A: c.WeakTypeTag, B: c.WeakTypeTag](a: c.Expr[A], b: c.Expr[B]) =
  //   q"_root_.bitsafe.BitwiseOps.bit[Long]($a) & _root_.bitsafe.BitwiseOps.bit[Long]($b)"
  // def andI[A: c.WeakTypeTag, B: c.WeakTypeTag](a: c.Expr[A], b: c.Expr[B]) =
  //   q"_root_.bitsafe.BitwiseOps.bit[Int]($a) & _root_.bitsafe.BitwiseOps.bit[Int]($b)"

  // def orL[A: c.WeakTypeTag, B: c.WeakTypeTag](a: c.Expr[A], b: c.Expr[B]) =
  //   q"_root_.bitsafe.BitwiseOps.bit[Long]($a) | _root_.bitsafe.BitwiseOps.bit[Long]($b)"
  // def orI[A: c.WeakTypeTag, B: c.WeakTypeTag](a: c.Expr[A], b: c.Expr[B]) =
  //   q"_root_.bitsafe.BitwiseOps.bit[Int]($a) | _root_.bitsafe.BitwiseOps.bit[Int]($b)"

  // def xorL[A: c.WeakTypeTag, B: c.WeakTypeTag](a: c.Expr[A], b: c.Expr[B]) =
  //   q"_root_.bitsafe.BitwiseOps.bit[Long]($a) ^ _root_.bitsafe.BitwiseOps.bit[Long]($b)"
  // def xorI[A: c.WeakTypeTag, B: c.WeakTypeTag](a: c.Expr[A], b: c.Expr[B]) =
  //   q"_root_.bitsafe.BitwiseOps.bit[Int]($a) ^ _root_.bitsafe.BitwiseOps.bit[Int]($b)"

  // def lshiftL[A: c.WeakTypeTag](a: c.Expr[A], places: c.Expr[Int]) =
  //   q"_root_.bitsafe.BitwiseOps.bit[Long]($a) << $places"
  // def lshiftI[A: c.WeakTypeTag](a: c.Expr[A], places: c.Expr[Int]) =
  //   q"_root_.bitsafe.BitwiseOps.bit[Int]($a) << $places"

  // def rshiftL[A: c.WeakTypeTag](a: c.Expr[A], places: c.Expr[Int]) =
  //   q"_root_.bitsafe.BitwiseOps.bit[Long]($a) >>> $places"
  // def rshiftI[A: c.WeakTypeTag](a: c.Expr[A], places: c.Expr[Int]) =
  //   q"_root_.bitsafe.BitwiseOps.bit[Int]($a) >>> $places"

  // def rshiftWithSignL[A: c.WeakTypeTag](a: c.Expr[A], places: c.Expr[Int]) =
  //   q"_root_.bitsafe.BitwiseOps.bit[Long]($a) >>> $places"
  // def rshiftWithSignI[A: c.WeakTypeTag](a: c.Expr[A], places: c.Expr[Int]) =
  //   q"_root_.bitsafe.BitwiseOps.bit[Int]($a) >>> $places"
}
