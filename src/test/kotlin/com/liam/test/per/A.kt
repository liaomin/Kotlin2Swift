package com.liam.test.per

fun test(){

}

open class A : com.liam.test.per.other.A() {
    fun d():Int{
        return 1
    }

    val q : com.liam.test.per.other.A = A()
}

fun main() {
    A()
}