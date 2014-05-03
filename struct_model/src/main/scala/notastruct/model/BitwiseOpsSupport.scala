package notastruct.model

object BitwiseOpsSupport {
  import notastruct.model.BitwiseOps.bit

  implicit class WrappedByte(val b: Byte) extends AnyVal {
    @inline def and(d: Double) = bit[Long](b) & bit[Long](d)
    @inline def or(d: Double)  = bit[Long](b) | bit[Long](d)
    @inline def xor(d: Double) = bit[Long](b) ^ bit[Long](d)

    @inline def and(l: Long)   = bit[Long](b) & l
    @inline def or(l: Long)    = bit[Long](b) | l
    @inline def xor(l: Long)   = bit[Long](b) ^ l

    @inline def and(f: Float)  = bit[Int](b) & bit[Int](f)
    @inline def or(f: Float)   = bit[Int](b) | bit[Int](f)
    @inline def xor(f: Float)  = bit[Int](b) ^ bit[Int](f)

    @inline def and(i: Int)   = bit[Int](b) & i
    @inline def or(i: Int)    = bit[Int](b) | i
    @inline def xor(i: Int)   = bit[Int](b) ^ i

    @inline def and(s: Short) = bit[Int](b) & bit[Int](s)
    @inline def or(s: Short)  = bit[Int](b) | bit[Int](s)
    @inline def xor(s: Short) = bit[Int](b) ^ bit[Int](s)

    @inline def and(x: Byte)  = bit[Int](b) & bit[Int](x)
    @inline def or(x: Byte)   = bit[Int](b) | bit[Int](x)
    @inline def xor(x: Byte)  = bit[Int](b) ^ bit[Int](x)

    @inline def lshift(i: Int) = bit[Int](b) << i
    @inline def rshift(i: Int) = bit[Int](b) >>> i
    @inline def rshiftWithSign(i: Int) = bit[Int](b) >> i
  }

  implicit class WrappedShort(val s: Short) extends AnyVal {
    @inline def and(d: Double) = bit[Long](s) & bit[Long](d)
    @inline def or(d: Double)  = bit[Long](s) | bit[Long](d)
    @inline def xor(d: Double) = bit[Long](s) ^ bit[Long](d)

    @inline def and(l: Long)  = bit[Long](s) & l
    @inline def or(l: Long)   = bit[Long](s) | l
    @inline def xor(l: Long)  = bit[Long](s) ^ l

    @inline def and(f: Float)  = bit[Int](s) & bit[Int](f)
    @inline def or(f: Float)   = bit[Int](s) | bit[Int](f)
    @inline def xor(f: Float)  = bit[Int](s) ^ bit[Int](f)

    @inline def and(i: Int)   = bit[Int](s) & i
    @inline def or(i: Int)    = bit[Int](s) | i
    @inline def xor(i: Int)   = bit[Int](s) ^ i

    @inline def and(x: Short) = bit[Int](s) & bit[Int](x)
    @inline def or(x: Short)  = bit[Int](s) | bit[Int](x)
    @inline def xor(x: Short) = bit[Int](s) ^ bit[Int](x)

    @inline def and(b: Byte)  = bit[Int](s) & bit[Int](b)
    @inline def or(b: Byte)   = bit[Int](s) | bit[Int](b)
    @inline def xor(b: Byte)  = bit[Int](s) ^ bit[Int](b)

    @inline def lshift(i: Int) = bit[Int](s) << i
    @inline def rshift(i: Int) = bit[Int](s) >>> i
    @inline def rshiftWithSign(i: Int) = bit[Int](s) >> i
  }

  implicit class WrappedInt(val i: Int) extends AnyVal {
    @inline def and(d: Double) = bit[Long](i) & bit[Long](d)
    @inline def or(d: Double)  = bit[Long](i) | bit[Long](d)
    @inline def xor(d: Double) = bit[Long](i) ^ bit[Long](d)

    @inline def and(l: Long)  = bit[Long](i) & l
    @inline def or(l: Long)   = bit[Long](i) | l
    @inline def xor(l: Long)  = bit[Long](i) ^ l

    @inline def and(f: Float)  = i & bit[Int](f)
    @inline def or(f: Float)   = i | bit[Int](f)
    @inline def xor(f: Float)  = i ^ bit[Int](f)

    @inline def and(x: Int)   = i & x
    @inline def or(x: Int)    = i | x
    @inline def xor(x: Int)   = i ^ x

    @inline def and(s: Short) = i & bit[Int](s)
    @inline def or(s: Short)  = i | bit[Int](s)
    @inline def xor(s: Short) = i ^ bit[Int](s)

    @inline def and(b: Byte)  = i & bit[Int](b)
    @inline def or(b: Byte)   = i | bit[Int](b)
    @inline def xor(b: Byte)  = i ^ bit[Int](b)
  }

  implicit class WrappedFloat(val f: Float) extends AnyVal {
    @inline def and(d: Double) = bit[Long](f) & bit[Long](d)
    @inline def or(d: Double)  = bit[Long](f) | bit[Long](d)
    @inline def xor(d: Double) = bit[Long](f) ^ bit[Long](d)

    @inline def and(l: Long)  = bit[Long](f) & l
    @inline def or(l: Long)   = bit[Long](f) | l
    @inline def xor(l: Long)  = bit[Long](f) ^ l

    @inline def and(i: Int)   = bit[Int](f) & i
    @inline def or(i: Int)    = bit[Int](f) | i
    @inline def xor(i: Int)   = bit[Int](f) ^ i

    @inline def and(s: Short)  = bit[Int](f) & bit[Int](s)
    @inline def or(s: Short)   = bit[Int](f) | bit[Int](s)
    @inline def xor(s: Short)  = bit[Int](f) ^ bit[Int](s)

    @inline def and(b: Byte)   = bit[Int](f) & bit[Int](b)
    @inline def or(b: Byte)    = bit[Int](f) | bit[Int](b)
    @inline def xor(b: Byte)   = bit[Int](f) ^ bit[Int](b)
  }

  implicit class WrappedLong(val l: Long) extends AnyVal {
    @inline def and(d: Double) = l & bit[Long](d)
    @inline def or(d: Double)  = l | bit[Long](d)
    @inline def xor(d: Double) = l ^ bit[Long](d)

    @inline def and(x: Long)  = l & x
    @inline def or(x: Long)   = l | x
    @inline def xor(x: Long)  = l ^ x

    @inline def and(i: Int)   = l & bit[Long](i)
    @inline def or(i: Int)    = l | bit[Long](i)
    @inline def xor(i: Int)   = l ^ bit[Long](i)

    @inline def and(s: Short) = l & bit[Long](s)
    @inline def or(s: Short)  = l | bit[Long](s)
    @inline def xor(s: Short) = l ^ bit[Long](s)

    @inline def and(b: Byte)  = l & bit[Long](b)
    @inline def or(b: Byte)   = l | bit[Long](b)
    @inline def xor(b: Byte)  = l ^ bit[Long](b)

    @inline def lshift(i: Int) = l << i
    @inline def rshift(i: Int) = l >>> i
    @inline def rshiftWithSign(i: Int) = l >> i
  }

  implicit class WrappedDouble(val d: Double) extends AnyVal {
    @inline def and(x: Double) = bit[Long](d) & bit[Long](x)
    @inline def or(x: Double)  = bit[Long](d) | bit[Long](x)
    @inline def xor(x: Double) = bit[Long](d) ^ bit[Long](x)

    @inline def and(l: Long)  = bit[Long](d) & l
    @inline def or(l: Long)   = bit[Long](d) | l
    @inline def xor(l: Long)  = bit[Long](d) ^ l

    @inline def and(i: Int)   = bit[Long](d) & bit[Long](i)
    @inline def or(i: Int)    = bit[Long](d) | bit[Long](i)
    @inline def xor(i: Int)   = bit[Long](d) ^ bit[Long](i)

    @inline def and(s: Short) = bit[Long](d) & bit[Long](s)
    @inline def or(s: Short)  = bit[Long](d) | bit[Long](s)
    @inline def xor(s: Short) = bit[Long](d) ^ bit[Long](s)

    @inline def and(b: Byte)  = bit[Long](d) & bit[Long](b)
    @inline def or(b: Byte)   = bit[Long](d) | bit[Long](b)
    @inline def xor(b: Byte)  = bit[Long](d) ^ bit[Long](b)

    @inline def lshift(i: Int) = bit[Long](d) << i
    @inline def rshift(i: Int) = bit[Long](d) >>> i
    @inline def rshiftWithSign(i: Int) = bit[Long](d) >> i
  }
}
