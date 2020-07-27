package com.liam.test.structed

/**
 * https://kotlinlang.org/docs/reference/interfaces.html
 * @author liaomin
 * @date 6/28/20 4:48 下午
 * @version 1.0
 */

interface Interface {
    fun func1()
}

interface Interface2 : Interface

open class InterfaceClass(val int: Int):Interface2{

    constructor():this(1)
    override fun func1() {

    }

}

class InterfaceClass2:InterfaceClass(1){
    override fun func1() {

    }

}

open class InterfaceClass3:Interface2{

    constructor()

    override fun func1() {

    }

}

open class InterfaceClass4:InterfaceClass3{

    constructor():super()

    override fun func1() {

    }

}