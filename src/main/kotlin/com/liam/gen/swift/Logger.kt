package com.liam.gen.swift

open class Logger {

    fun info(message:Any){
        println(message)
    }

    fun error(message: Any){
        System.err.println(message)
    }

    fun debug(message: Any) =  info(message)

    companion object : Logger()
}