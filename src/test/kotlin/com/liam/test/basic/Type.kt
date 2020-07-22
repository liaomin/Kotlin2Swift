package com.liam.test.basic

/**
 * test for https://kotlinlang.org/docs/reference/basic-types.html
 * @author liaomin
 * @date 6/27/20 10:56 上午
 * @version 1.0
 */
class BasicTypes {

    fun a(){

    }

    val str1 = "dwdw"
    val str = """
            for (c in "foo")
                    print(c)
            """
    //  TODO convert
    val UL = 1UL
    val UI = 1U
//    val toB = UI.toByte()
//    val toB2 = !true
//    val toShort = 1.toShort()
//    val toChar = 1.toChar()
//    val toInt = 1.toInt()
//    val toLong = 1.toLong()
//    val toFloat = 1.toFloat()
//    val toDouble = 1.toDouble()
//
//
//    val inv = 1.inv()
//    val rq = 1 shl 1
//    val rOR = 1 or 1
//    val rAnd = 1 and 1
//    val rXOR = 1 xor 1
//    val rushr = 1 ushr 1
//    val shr = 1 shr 1
//
//
//
    val c:Char = 'a'
    val byte:Byte = 1
    val short:Short = 1
    val int:Int = 1
    val long:Long = 1
    val ubyte:UByte = 1u
    val ushort:UShort = 1u
    val uint:UInt = 1u
    val ulong:ULong = 1u
    val float:Float = 1.0f
    val double:Double = 1.02

    val one = 1 // Int
    val threeBillion = 3000000000 // Long
    val oneLong = 1L // Long
    val oneByte: Byte = 1

    val pi = 3.14 // Double
    val e = 2.7182818284 // Double
    val eFloat = 2.7182818284f // Float, actual value is 2.7182817

    var bt:Boolean = true
    var bf:Boolean = false

    val l = 1L + 3 // Long + Int => Long
}
