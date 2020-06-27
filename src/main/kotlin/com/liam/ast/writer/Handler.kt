package com.liam.ast.writer

import com.liam.ast.psi.Node

/**
 * @author liaomin
 * @date 6/19/20 2:13 下午
 * @version 1.0
 */
interface Handler<T:Node> {
    fun getLanguage():Language
    fun onHandle(writer: LanguageWriter,node: T,statement: Statement)
    fun getHandleClass():Class<T>

    /**
     * @return true will interrupt chain and only call beforeHandle method
     */
    fun interrupt(node: T,statement:Statement):Boolean

    companion object{
        fun isHandlerClass(c:Class<*>):Boolean{
            var temp:Class<*>? = c
            while (true){
                if(temp == null){
                    return false
                }
                val interfaces = temp.interfaces
                if( interfaces.contains(Handler::class.java)){
                    return true
                }
                temp = temp.superclass
            }
            return false
        }
    }
}

abstract class SwiftHandler<T:Node> : Handler<T> {
    override fun getLanguage(): Language = Language.SWIFT
    override fun interrupt(node: T,statement:Statement): Boolean = false
}

abstract class ObjcHandler<T:Node>  : Handler<T> {
    override fun getLanguage(): Language = Language.OBJC
    override fun interrupt(node: T,statement:Statement): Boolean = false
}


class HandlerChain<T:Node> {

    private val chain:ArrayList<Handler<T>> = ArrayList()

    fun addHandler(handler: Handler<T>){
        chain.add(handler)
    }

    fun onHandle(writer: LanguageWriter,node: T,statement: Statement){
        for (hanlder in chain){
            chain.forEach {
                it.onHandle(writer, node, statement)
            }
        }
    }

}
