// package notastruct.model

// import scala.reflect.macros.blackbox.Context

// object BitManipulators {
//   def bitSafe[T](f: Long): T = macro BitManipulators.bitSafeImpl[Long, T]
//   def bitSafe[T](f: Int): T = macro BitManipulators.bitSafeImpl[Int, T]
//   def bitSafe[T](f: Short): T = macro BitManipulators.bitSafeImpl[Short, T]
//   def bitSafe[T](f: Byte): T = macro BitManipulators.bitSafeImpl[Byte, T]

//   def bitSafeImpl[F: c.WeakTypeTag, T: c.WeakTypeTag](c: Context)(f: c.Expr[=> F]): c.Expr[T] = {
//     import c.universe._

//     val LongType = typeTag[Long].tpe
//     val IntType = typeTag[Int].tpe
//     val ShortType = typeTag[Short].tpe
//     val ByteType = typeTag[Byte].tpe
//     val validTypes = Set(LongType, IntType, ShortType, ByteType)

//     // def promotionType[A: c.WeakTypeTag, B: c.WeakTypeTag](a: c.Expr[A], b: c.Expr[B]): Either[c.TypeTag[Int], c.TypeTag[Long]] =
//     //   if (weakTypeTag[A].tpe == LongType || weakTypeTag[B].tpe == LongType) {
//     //     Right(typeTag[Long])
//     //   } else {
//     //     Left(typeTag[Int])
//     //   }

//     // def promotionType[A: c.WeakTypeTag]: Either[c.TypeTag[Int], c.TypeTag[Long]] =
//     //   if (weakTypeTag[A].tpe == LongType) {
//     //     Right(typeTag[Long])
//     //   } else {
//     //     Left(typeTag[Int])
//     //   }

//     def safeCast[F: c.WeakTypeTag, T: c.WeakTypeTag](e: c.Expr[=> F]): c.Expr[T] = {
//       println(s"working on expression: $e")
//       val fromType = tq"${weakTypeTag[F]}"
//       val toType = tq"${weakTypeTag[T]}"
//       c.Expr(e match {
//                case Literal(a) => q"_root_.notastruct.model.BitConverters.convert[$fromType, $toType]($a)"
//                case q"$binaryOp($a, $b)" => binaryOp match {
//                  case "&" => q"_root_.notastruct.model.BitManipulators.bitSafe[$toType]($a) & _root_.notastruct.model.BitManipulators.bitSafe[$toType]($b)"
//                  case "|" => q"_root_.notastruct.model.BitManipulators.bitSafe[$toType]($a) | _root_.notastruct.model.BitManipulators.bitSafe[$toType]($b)"
//                  case "^" => q"_root_.notastruct.model.BitManipulators.bitSafe[$toType]($a) ^ _root_.notastruct.model.BitManipulators.bitSafe[$toType]($b)"
//                  case _  => q"_root_.notastruct.model.BitConverters.convert[$fromType, $toType]($e)"
//                }
//                case q"~$a"     => q"~_root_.notastruct.model.BitManipulators.bitSafe[$toType]($a)"
//                case _          => q"_root_.notastruct.model.BitConverters.convert[$fromType, $toType]($e)"
//              })
//     }

//     val fromType = tq"${weakTypeTag[F]}"
//     val toType = tq"${weakTypeTag[T]}"
//     val safe = safeCast[F, T](f)
//     println(s"fromType = $fromType, toType = $toType")

//     c.Expr(q"_root_.notastruct.model.BitConverters.convert[$fromType, $toType]($safe)")
//   }

//   // implicit class BitManipulatorInt(val a: Int) extends AnyVal {
//   //   @inline def &(b: Long): Long = convert[Int, Long](a) & b

//   //   @inline def |(b: Long): Long = convert[Int, Long](a) | b

//   //   @inline def ^(b: Long): Long = convert[Int, Long](a) ^ b

//   //   @inline def &(b: Short): Int = convert[Short, Int](b) & a

//   //   @inline def |(b: Short): Int = convert[Short, Int](b) | a

//   //   @inline def ^(b: Short): Int = convert[Short, Int](b) ^ a

//   //   @inline def unary_~: Int = ~a
//   // }

//   // implicit class BitManipulatorShort(val a: Short) extends AnyVal {
//   //   @inline def &(b: Long): Long = convert[Short, Long](a) & b

//   //   @inline def |(b: Long): Long = convert[Short, Long](a) | b

//   //   @inline def ^(b: Long): Long = convert[Short, Long](a) ^ b

//   //   @inline def &(b: Int): Int = convert[Short, Int](a) & b

//   //   @inline def |(b: Int): Int = convert[Short, Int](a) | b

//   //   @inline def ^(b: Int): Int = convert[Short, Int](a) ^ b

//   //   @inline def unary_~: Short = ~convert[Short, Int](a)
//   // }

//   // implicit class BitManipulatorByte(val a: Byte) extends AnyVal {
//   //   @inline def &(b: Long): Long = java.lang.Byte.toUnsignedLong(a) & b

//   //   @inline def |(b: Long): Long = java.lang.Byte.toUnsignedLong(a) | b

//   //   @inline def ^(b: Long): Long = java.lang.Byte.toUnsignedLong(a) ^ b

//   //   @inline def &(b: Int): Int = java.lang.Byte.toUnsignedInt(a) & b

//   //   @inline def |(b: Int): Int = java.lang.Byte.toUnsignedInt(a) | b

//   //   @inline def ^(b: Int): Int = java.lang.Byte.toUnsignedInt(a) ^ b

//   //   @inline def unary_~: Byte = ~convert[Byte, Int](a)
//   // }
// }

// // trait BitManipulator[@specialized(Long, Int, Short, Byte) F, @specialized(Long, Int) T] {
// //   def bits(x: F, offset: Int, width: Int): T
// //   def leftShift(x: F, width: Int): T
// //   def rightShiftWithSign(x: F, width: Int): T
// //   def rightShiftWithoutSign(x: F, width: Int): T
// // }

// // object BitManipulators {
// //   import BitConverters.convert

// //   def bits[F, T](x: F, offset: Int, width: Int): T = macro BitManipulators.bitsImpl[F, T]

// //   def leftShift[F, T](x: F, width: Int): T = macro BitManipulators.leftShiftImpl[F, T]

// //   def rightShiftWithSign[F, T](x: F, width: Int): T = macro BitManipulators.rightShiftWithSignImpl[F, T]

// //   def rightShiftWithoutSign[F, T](x: F

// //   implicit object Long2Long extends BitManipulator[Long, Long] {
// //     override def bits(x: Long, offset: Int, width: Int): Long =
// //       (x & (((1L << width) - 1) << offset)) >> offset
// //     override def leftShift(x: Long, width: Int): Long =
// //       x << width
// //     override def rightShiftWithSign(x: Long, width: Int): Long =
// //       x >> width
// //     override def rightShiftWithoutSign(x: Long, width: Int): Long =
// //       x >>> width
// //   }

// //   implicit object Int2Int extends BitManipulator[Int, Int] {
// //     override def bits(x: Int, offset: Int, width: Int): Int =
// //       (x & (((1 << width) - 1) << offset)) >> offset
// //     override def leftShift(x: Int, width: Int): Int = x << width
// //     override def rightShiftWithSign(x: Int, width: Int): Int = x >> width
// //     override def rightShiftWithoutSign(x: Int, width: Int): Int = x >>> width
// //   }

// //   implicit object Int2Long extends BitManipulator[Int, Long] {
// //     override def bits(x: Int, offset: Int, width: Int): Long =
// //       Long2Long.bits(convert[Int, Long](x), offset, width)
// //     override def leftShift(x: Int, width: Int): Long =
// //       Long2Long.leftShift(convert[Int, Long](x), width)
// //     override def rightShiftWithSign(x: Int, width: Int): Long =
// //       Long2Long.rightShiftWithSign(convert[Int, Long](x), width)
// //     override def rightShiftWithoutSign(x: Int, width: Int): Long =
// //       Long2Long.rightShiftWithoutSign(convert[Int, Long](x), width)
// //   }

// //   implicit object Short2Int extends BitManipulator[Short, Int] {
// //     override def bits(x: Short, offset: Int, width: Int): Int =
// //       convert[Short, Int](x) & (((1 << (width - 1)) - 1) << offset)
// //     override def leftShift(x: Short, width: Int): Int =
// //       convert[Short, Int](x) << width
// //     override def rightShiftWithSign(x: Short, width: Int): Int =
// //       convert[Short, Int](x) >> width
// //     override def rightShiftWithoutSign(x: Short, width: Int): Int =
// //       convert[Short, Int](x) >>> width
// //   }

// //   implicit object Short2Long extends BitManipulator[Short, Long] {
// //     override def bits(x: Short, offset: Int, width: Int): Long =
// //       Long2Long.bits(convert[Short, Long](x), offset, width)
// //     override def leftShift(x: Short, width: Int): Long =
// //       Long2Long.leftShift(convert[Short, Long](x), width)
// //     override def rightShiftWithSign(x: Short, width: Int): Long =
// //       Long2Long.rightShiftWithSign(convert[Short, Long](x), width)
// //     override def rightShiftWithoutSign(x: Short, width: Int): Long =
// //       Long2Long.rightShiftWithoutSign(convert[Short, Long](x), width)
// //   }

// //   implicit object Short2Short extends BitManipulator[Short, Short] {
// //     override def bits(x: Short, offset: Int, width: Int): Short =
// //       convert[Int, Short](Short2Int.bits(x, offset, width))
// //     override def leftShift(x: Short, width: Int): Short =
// //       convert[Int, Short](Short2Int.leftShift(x, width))
// //     override def rightShiftWithSign(x: Short, width: Int): Short =
// //       convert[Int, Short](Short2Int.rightShiftWithSign(x, width))
// //     override def rightShiftWithoutSign(x: Short, width: Int): Short =
// //       convert[Int, Short](Short2Int.rightShiftWithoutSign(x, width))
// //   }

// //   implicit object Byte2Int extends BitManipulator[Byte, Int] {
// //     override def bits(x: Byte, offset: Int, width: Int): Int =
// //       convert[Byte, Int](x) & (((1 << (width - 1)) - 1) << offset)
// //     override def leftShift(x: Byte, width: Int): Int =
// //       convert[Byte, Int](x) << width
// //     override def rightShiftWithSign(x: Byte, width: Int): Int =
// //       convert[Byte, Int](x) >> width
// //     override def rightShiftWithoutSign(x: Byte, width: Int): Int =
// //       convert[Byte, Int](x) >>> width
// //   }

// //   implicit object Byte2Long extends BitManipulator[Byte, Long] {
// //     override def bits(x: Byte, offset: Int, width: Int): Long =
// //       Long2Long.bits(convert[Byte, Long](x), offset, width)
// //     override def leftShift(x: Byte, width: Int): Long =
// //       Long2Long.leftShift(convert[Byte, Long](x), width)
// //     override def rightShiftWithSign(x: Byte, width: Int): Long =
// //       Long2Long.rightShiftWithSign(convert[Byte, Long](x), width)
// //     override def rightShiftWithoutSign(x: Byte, width: Int): Long =
// //       Long2Long.rightShiftWithoutSign(convert[Byte, Long](x), width)
// //   }

// //   implicit object Byte2Byte extends BitManipulator[Byte, Byte] {
// //     override def bits(x: Byte, offset: Int, width: Int): Byte =
// //       convert[Int, Byte](Byte2Int.bits(x, offset, width))
// //     override def leftShift(x: Byte, width: Int): Byte =
// //       convert[Int, Byte](Byte2Int.leftShift(x, width))
// //     override def rightShiftWithSign(x: Byte, width: Int): Byte =
// //       convert[Int, Byte](Byte2Int.rightShiftWithSign(x, width))
// //     override def rightShiftWithoutSign(x: Byte, width: Int): Byte =
// //       convert[Int, Byte](Byte2Int.rightShiftWithoutSign(x, width))
// //   }
// // }
