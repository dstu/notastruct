package notastruct.model

import scala.reflect.macros.blackbox.Context

object BitwiseOps {
  def bit[T](e: Byte): T   = macro BitwiseOpsBundle.bit[Byte, T]
  def bit[T](e: Short): T  = macro BitwiseOpsBundle.bit[Short, T]
  def bit[T](e: Int): T    = macro BitwiseOpsBundle.bit[Int, T]
  def bit[T](e: Float): T  = macro BitwiseOpsBundle.bit[Float, T]
  def bit[T](e: Long): T   = macro BitwiseOpsBundle.bit[Long, T]
  def bit[T](e: Double): T = macro BitwiseOpsBundle.bit[Double, T]

  def not(e: Long): Long   = macro BitwiseOpsBundle.notL[Long]
  def not(e: Double): Long = macro BitwiseOpsBundle.notL[Double]
  def not(e: Float): Int   = macro BitwiseOpsBundle.notI[Float]
  def not(e: Int): Int     = macro BitwiseOpsBundle.notI[Int]
  def not(e: Short): Int   = macro BitwiseOpsBundle.notI[Short]
  def not(e: Byte): Int    = macro BitwiseOpsBundle.notI[Byte]

  def and(a: Long, b: Long): Long   = macro BitwiseOpsBundle.andL[Long, Long]

  def and(a: Long, b: Double): Long = macro BitwiseOpsBundle.andL[Long, Double]
  def and(a: Long, b: Int): Long    = macro BitwiseOpsBundle.andL[Long, Int]
  def and(a: Long, b: Float): Long  = macro BitwiseOpsBundle.andL[Long, Float]
  def and(a: Long, b: Short): Long  = macro BitwiseOpsBundle.andL[Long, Short]
  def and(a: Long, b: Byte): Long   = macro BitwiseOpsBundle.andL[Long, Byte]

  def and(a: Double, b: Long): Long = macro BitwiseOpsBundle.andL[Double, Long]
  def and(a: Int, b: Long): Long    = macro BitwiseOpsBundle.andL[Int, Long]
  def and(a: Float, b: Long): Long  = macro BitwiseOpsBundle.andL[Float, Long]
  def and(a: Short, b: Long): Long  = macro BitwiseOpsBundle.andL[Short, Long]
  def and(a: Byte, b: Long): Long   = macro BitwiseOpsBundle.andL[Byte, Long]

  def and(a: Int, b: Int): Int      = macro BitwiseOpsBundle.andI[Int, Int]

  def and(a: Int, b: Float): Int    = macro BitwiseOpsBundle.andI[Int, Float]
  def and(a: Int, b: Short): Int    = macro BitwiseOpsBundle.andI[Int, Short]
  def and(a: Int, b: Byte): Int     = macro BitwiseOpsBundle.andI[Int, Byte]

  def and(a: Float, b: Int): Int    = macro BitwiseOpsBundle.andI[Float, Int]
  def and(a: Short, b: Int): Int    = macro BitwiseOpsBundle.andI[Short, Int]
  def and(a: Byte, b: Int): Int     = macro BitwiseOpsBundle.andI[Byte, Int]

  def or(a: Long, b: Long): Long   = macro BitwiseOpsBundle.orL[Long, Long]

  def or(a: Long, b: Double): Long = macro BitwiseOpsBundle.orL[Long, Double]
  def or(a: Long, b: Int): Long    = macro BitwiseOpsBundle.orL[Long, Int]
  def or(a: Long, b: Float): Long  = macro BitwiseOpsBundle.orL[Long, Float]
  def or(a: Long, b: Short): Long  = macro BitwiseOpsBundle.orL[Long, Short]
  def or(a: Long, b: Byte): Long   = macro BitwiseOpsBundle.orL[Long, Byte]

  def or(a: Double, b: Long): Long = macro BitwiseOpsBundle.orL[Double, Long]
  def or(a: Int, b: Long): Long    = macro BitwiseOpsBundle.orL[Int, Long]
  def or(a: Float, b: Long): Long  = macro BitwiseOpsBundle.orL[Float, Long]
  def or(a: Short, b: Long): Long  = macro BitwiseOpsBundle.orL[Short, Long]
  def or(a: Byte, b: Long): Long   = macro BitwiseOpsBundle.orL[Byte, Long]

  def or(a: Int, b: Int): Int   = macro BitwiseOpsBundle.orI[Int, Int]

  def or(a: Int, b: Float): Int = macro BitwiseOpsBundle.orI[Int, Float]
  def or(a: Int, b: Short): Int = macro BitwiseOpsBundle.orI[Int, Short]
  def or(a: Int, b: Byte): Int  = macro BitwiseOpsBundle.orI[Int, Byte]

  def or(a: Float, b: Int): Int = macro BitwiseOpsBundle.orI[Float, Int]
  def or(a: Short, b: Int): Int = macro BitwiseOpsBundle.orI[Short, Int]
  def or(a: Byte, b: Int): Int  = macro BitwiseOpsBundle.orI[Byte, Int]

  def xor(a: Long, b: Long): Long   = macro BitwiseOpsBundle.orL[Long, Long]

  def xor(a: Long, b: Double): Long = macro BitwiseOpsBundle.xorL[Long, Double]
  def xor(a: Long, b: Int): Long    = macro BitwiseOpsBundle.xorL[Long, Int]
  def xor(a: Long, b: Float): Long  = macro BitwiseOpsBundle.xorL[Long, Float]
  def xor(a: Long, b: Short): Long  = macro BitwiseOpsBundle.xorL[Long, Short]
  def xor(a: Long, b: Byte): Long   = macro BitwiseOpsBundle.xorL[Long, Byte]

  def xor(a: Double, b: Long): Long = macro BitwiseOpsBundle.xorL[Double, Long]
  def xor(a: Int, b: Long): Long    = macro BitwiseOpsBundle.xorL[Int, Long]
  def xor(a: Float, b: Long): Long  = macro BitwiseOpsBundle.xorL[Float, Long]
  def xor(a: Short, b: Long): Long  = macro BitwiseOpsBundle.xorL[Short, Long]
  def xor(a: Byte, b: Long): Long   = macro BitwiseOpsBundle.xorL[Byte, Long]

  def xor(a: Int, b: Int): Int   = macro BitwiseOpsBundle.xorI[Int, Int]

  def xor(a: Int, b: Float): Int = macro BitwiseOpsBundle.xorI[Int, Float]
  def xor(a: Int, b: Short): Int = macro BitwiseOpsBundle.xorI[Int, Short]
  def xor(a: Int, b: Byte): Int  = macro BitwiseOpsBundle.xorI[Int, Byte]

  def xor(a: Float, b: Int): Int = macro BitwiseOpsBundle.xorI[Float, Int]
  def xor(a: Short, b: Int): Int = macro BitwiseOpsBundle.xorI[Short, Int]
  def xor(a: Byte, b: Int): Int  = macro BitwiseOpsBundle.xorI[Byte, Int]

  def lshift(a: Byte, places: Int): Int    = macro BitwiseOpsBundle.lshiftI[Byte]
  def lshift(a: Short, places: Int): Int   = macro BitwiseOpsBundle.lshiftI[Short]
  def lshift(a: Float, places: Int): Int   = macro BitwiseOpsBundle.lshiftI[Float]
  def lshift(a: Int, places: Int): Int     = macro BitwiseOpsBundle.lshiftI[Int]
  def lshift(a: Long, places: Int): Long   = macro BitwiseOpsBundle.lshiftL[Long]
  def lshift(a: Double, places: Int): Long = macro BitwiseOpsBundle.lshiftL[Double]

  def rshift(a: Byte, places: Int): Int    = macro BitwiseOpsBundle.rshiftI[Byte]
  def rshift(a: Short, places: Int): Int   = macro BitwiseOpsBundle.rshiftI[Short]
  def rshift(a: Float, places: Int): Int   = macro BitwiseOpsBundle.rshiftI[Float]
  def rshift(a: Int, places: Int): Int     = macro BitwiseOpsBundle.rshiftI[Int]
  def rshift(a: Long, places: Int): Long   = macro BitwiseOpsBundle.rshiftL[Long]
  def rshift(a: Double, places: Int): Long = macro BitwiseOpsBundle.rshiftL[Double]

  def rshiftWithSign(a: Byte, places: Int): Int    = macro BitwiseOpsBundle.rshiftWithSignI[Byte]
  def rshiftWithSign(a: Short, places: Int): Int   = macro BitwiseOpsBundle.rshiftWithSignI[Short]
  def rshiftWithSign(a: Float, places: Int): Int   = macro BitwiseOpsBundle.rshiftWithSignI[Float]
  def rshiftWithSign(a: Int, places: Int): Int     = macro BitwiseOpsBundle.rshiftWithSignI[Int]
  def rshiftWithSign(a: Long, places: Int): Long   = macro BitwiseOpsBundle.rshiftWithSignL[Long]
  def rshiftWithSign(a: Double, places: Int): Long = macro BitwiseOpsBundle.rshiftWithSignL[Double]
}

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
    q"~_root_.notastruct.model.BitwiseOps.bit[Long]($e)"
  def notI[A: c.WeakTypeTag](e: c.Expr[A]) =
    q"~_root_.notastruct.model.BitwiseOps.bit[Int]($e)"

  def andL[A: c.WeakTypeTag, B: c.WeakTypeTag](a: c.Expr[A], b: c.Expr[B]) =
    q"_root_.notastruct.model.BitwiseOps.bit[Long]($a) & _root_.notastruct.model.BitwiseOps.bit[Long]($b)"
  def andI[A: c.WeakTypeTag, B: c.WeakTypeTag](a: c.Expr[A], b: c.Expr[B]) =
    q"_root_.notastruct.model.BitwiseOps.bit[Int]($a) & _root_.notastruct.model.BitwiseOps.bit[Int]($b)"

  def orL[A: c.WeakTypeTag, B: c.WeakTypeTag](a: c.Expr[A], b: c.Expr[B]) =
    q"_root_.notastruct.model.BitwiseOps.bit[Long]($a) | _root_.notastruct.model.BitwiseOps.bit[Long]($b)"
  def orI[A: c.WeakTypeTag, B: c.WeakTypeTag](a: c.Expr[A], b: c.Expr[B]) =
    q"_root_.notastruct.model.BitwiseOps.bit[Int]($a) | _root_.notastruct.model.BitwiseOps.bit[Int]($b)"

  def xorL[A: c.WeakTypeTag, B: c.WeakTypeTag](a: c.Expr[A], b: c.Expr[B]) =
    q"_root_.notastruct.model.BitwiseOps.bit[Long]($a) ^ _root_.notastruct.model.BitwiseOps.bit[Long]($b)"
  def xorI[A: c.WeakTypeTag, B: c.WeakTypeTag](a: c.Expr[A], b: c.Expr[B]) =
    q"_root_.notastruct.model.BitwiseOps.bit[Int]($a) ^ _root_.notastruct.model.BitwiseOps.bit[Int]($b)"

  def lshiftL[A: c.WeakTypeTag](a: c.Expr[A], places: c.Expr[Int]) =
    q"_root_.notastruct.model.BitwiseOps.bit[Long]($a) << $places"
  def lshiftI[A: c.WeakTypeTag](a: c.Expr[A], places: c.Expr[Int]) =
    q"_root_.notastruct.model.BitwiseOps.bit[Int]($a) << $places"

  def rshiftL[A: c.WeakTypeTag](a: c.Expr[A], places: c.Expr[Int]) =
    q"_root_.notastruct.model.BitwiseOps.bit[Long]($a) >>> $places"
  def rshiftI[A: c.WeakTypeTag](a: c.Expr[A], places: c.Expr[Int]) =
    q"_root_.notastruct.model.BitwiseOps.bit[Int]($a) >>> $places"

  def rshiftWithSignL[A: c.WeakTypeTag](a: c.Expr[A], places: c.Expr[Int]) =
    q"_root_.notastruct.model.BitwiseOps.bit[Long]($a) >>> $places"
  def rshiftWithSignI[A: c.WeakTypeTag](a: c.Expr[A], places: c.Expr[Int]) =
    q"_root_.notastruct.model.BitwiseOps.bit[Int]($a) >>> $places"
}
