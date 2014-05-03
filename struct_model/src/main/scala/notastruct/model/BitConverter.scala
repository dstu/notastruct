package notastruct.model

import scala.reflect.macros.blackbox.Context

trait BitConverter[F, T] {
  def convert(x: F): T
}

object BitConverters {
  def convert[F, T](f: F): T = macro BitConverters.convertImpl[F, T]

  def convertImpl[F: c.WeakTypeTag, T: c.WeakTypeTag](c: Context)(f: c.Expr[F]): c.Expr[T] = {
    import c.universe._
    val fType = weakTypeTag[F].tpe
    val tType = weakTypeTag[T].tpe
    if (fType == tType) {
      c.Expr(q"$f")
    } else {
      val LongType = typeTag[Long].tpe
      val FloatType = typeTag[Float].tpe
      val IntType = typeTag[Int].tpe
      val ShortType = typeTag[Short].tpe
      val ByteType = typeTag[Byte].tpe
      (fType, tType) match {
        case (LongType, FloatType) => c.Expr(q"_root_.java.lang.Float.intBitsToFloat($f.asInstanceOf[Int])")
        case (LongType, IntType) => c.Expr(q"$f.asInstanceOf[Int]")
        case (LongType, ShortType) => c.Expr(q"$f.asInstanceOf[Short]")
        case (LongType, ByteType) => c.Expr(q"$f.asInstanceOf[Byte]")
        case (FloatType, LongType) => c.Expr(q"_root_.java.lang.Integer.toUnsignedLong(_root_.java.lang.Float.floatToRawIntBits($f))")
        case (FloatType, IntType) => c.Expr(q"_root_.java.lang.Float.floatToRawIntBits($f)")
        case (IntType, LongType) => c.Expr(q"_root_.java.lang.Integer.toUnsignedLong($f)")
        case (IntType, FloatType) => c.Expr(q"_root_.java.lang.Float.intBitsToFloat($f)")
        case (IntType, ShortType) => c.Expr(q"$f.asInstanceOf[Short]")
        case (IntType, ByteType) => c.Expr(q"$f.asInstanceOf[Byte]")
        case (ShortType, LongType) => c.Expr(q"_root_.java.lang.Short.toUnsignedLong($f)")
        case (ShortType, IntType) => c.Expr(q"_root_.java.lang.Short.toUnsignedInt($f)")
        case (ShortType, ByteType) => c.Expr(q"$f.asInstanceOf[Byte]")
        case (ByteType, LongType) => c.Expr(q"_root_.java.lang.Byte.toUnsignedLong($f)")
        case (ByteType, IntType) => c.Expr(q"_root_.java.lang.Byte.toUnsignedInt($f)")
        case (ByteType, ShortType) => c.Expr(q"_root_.java.lang.Byte.toUnsignedInt($f).asInstanceOf[Short]")
        case _ => c.Expr(q"implicitly[_root_.notastruct.model.BitConverter[$fType, $tType]].convert($f)")
      }
    }
  }
}
