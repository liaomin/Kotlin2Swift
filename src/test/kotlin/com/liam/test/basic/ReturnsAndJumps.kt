package com.liam.test.basic

/**
 * test for https://kotlinlang.org/docs/reference/returns.html
 * @author liaomin
 * @date 6/27/20 10:56 上午
 * @version 1.0
 */


fun returnTest():Int{
    loop@ for (i in 1..100) {
        for (j in 1..100) {
            if (false) break@loop
        }
    }

    arrayListOf<String>().forEach {
        return@forEach
    }
    return 1
}

fun testReturnFoo() {
    listOf(1, 2, 3, 4, 5).forEach {
        if (it == 3) return@forEach // local return to the caller of the lambda, i.e. the forEach loop
        print(it)
    }
    print(" done with implicit label")

    run loop@{
        listOf(1, 2, 3, 4, 5).forEach {
            if (it == 3) return@loop // non-local return from the lambda passed to run
            print(it)
        }
    }
}