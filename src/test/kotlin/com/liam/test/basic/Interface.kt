package com.liam.test.basic

interface Interface {
    fun func1()
}

interface Interface2 : Interface{

}

open class InterfaceClass(val int: Int):Interface2{

    constructor():this(1)
    override fun func1() {

    }

}

class InterfaceClass2:InterfaceClass(1){
    override fun func1() {

    }

}