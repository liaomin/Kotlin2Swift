//package com.liam.test.basic
//
//import org.jetbrains.kotlin.resolve.constants.evaluate.parseBoolean
//
///**
// * https://kotlinlang.org/docs/reference/control-flow.html
// * @author liaomin
// * @date 6/28/20 4:48 下午
// * @version 1.0
// */

fun a():Int{
    return 1
}

fun b():Int{
    return 1
}

fun c():Int{
    if(a() > b()) return 1
    return 2
}


fun d():Int{
    val t = if(a() > b()) 1 else 2
    val t2 = if(a() > b()) {
        c()
        b()
    } else 2
    return t2
}

fun println(s:String){

}


val q2 = if (true) a() else b()

val q3 = if (c() > 1) { a() } else b()
val q4 = if (c() > 1) {
    var i = 1
    a()

} else b()



class ControlFlow {
    fun hasPrefix(x: Any) = when(x) {
        is String -> {
            println("")
            true
        }
        else -> false
    }


    fun whenExpression(x:Int=0,y:Int= 2){
        fun hasPrefix(x: Any) = when(x) {
            is String -> true
            else -> false
        }
        when (x) {
            is Int ->  println("x is Int")
            else -> { // Note the block
                println("x is neither 1 nor 2")
            }
        }
    }

    fun ifExpression(){
        whenExpression(1)
        // Traditional usage
        val a = 1
        val b = 2
        var max = a
        if (a < b) max = b
        if (a > b) {
            max = a
        } else if(b > a){
            max = b
        }else{
            max = 1
        }
        if (a > b) a else b
        if (a > b) {
            whenExpression(1)
            this.whenExpression(1)
            println("Choose a")
            a
        } else {
            whenExpression(1)
            this.whenExpression(1)
            println("Choose b")
            b
        }
    }


    fun ForLoops(){
//        for (i in 1..3) {
//            println(i)
//        }
//        for (i in 6 downTo 0 step 2) {
//            println(i)
//        }
//        val array = ArrayList<String>()
//        for ((index, value) in array.withIndex()) {
//            println("the element at $index is $value")
//        }
    }

    fun WhileLoops(){
        var x = 1000
        while (x > 0) {
            x--
        }

        fun retrieveData():String{
            return ""
        }

        do {
            val dqdq = q23
            val y = retrieveData()
        } while (y != null) // y is visible here!
    }

    val qee = 1
}

val q23 = 1
