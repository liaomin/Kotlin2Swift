package com.liam.test.basic


open class TestSuperClassA(val dt:Float){

}

class TestClassB : TestSuperClassA {
    constructor(int: Int):super(1.0f)
}



class TestClass(var int:Int):TestSuperClassA(1.0f){

    val q = 1.2
    val ew = 1.2f

    val q2:Float = 1.2f
    val ew2:Double= 1.2

    constructor(string: String = "",double: Double):this(1)

    init {
        this.int = 1
    }

    init {
        this.int = 2
    }


    val t = 23

    fun test(int: Int ){
        this.int = int
    }
}

fun main2() {
    val d:Double = 12.0
    val qdwdw = TestClass(double = d)
}