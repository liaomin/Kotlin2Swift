package com.liam.test.structed

/**
 * https://kotlinlang.org/docs/reference/properties.html
 * @author liaomin
 * @date 6/28/20 4:48 下午
 * @version 1.0
 */

//TODO
//lateinit

open class BaseAddress{
    val baseInt:Int = 1
    open var baseInt2:Int = 0
        get() = 1
}

class Address : BaseAddress(){
//    lateinit var d:AdW
    var name: String = "Holmes, Sherlock"
    var street: String = "Baker"
    var city: String = "London"
    var state: String? = null
    var zip: String = "123456"
    var size: Int  = 1
    override var baseInt2:Int
        get() = 1
        set(value) {
            super.baseInt2 = 1
        }


    var isEmpty: Boolean = false
        get() = this.size == 0
        set(value) {
            field = value
        }

    val isEmpty1:Boolean get() =  this.size == 0

    fun copyAddress(address: Address): Address {
//        this::d.isInitialized
        val result = Address() // there's no 'new' keyword in Kotlin
        result.name = address.name // accessors are called
        result.street = address.street

        return result
    }
}