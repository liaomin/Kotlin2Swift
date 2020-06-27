package com.liam.ast.writer

import com.liam.ast.psi.Node
import java.util.concurrent.ConcurrentHashMap


/**
 * @author liaomin
 * @date 6/19/20 2:25 下午
 * @version 1.0
 */
enum class Language(val value: String){
    OBJC("objc"),
    SWIFT("swift")
}

abstract class LanguageWriter{

    private  val handlers  = ConcurrentHashMap<Any,HandlerChain<Node>>()

    open fun write(file: Node.File, statement: Statement) = onWriteNode(file,statement)

    abstract fun getLanguage():Language

    open fun onWriteNode(node: Node, statement: Statement,parent:Node? = null) {
        parent?.also { node.parent = it  }
        when(node){
            is Node.Decl.Func.Body.Block -> defaultWriteNode(node, statement){
                onWriteNode(node.block,statement,node)
            }
            is Node.Decl.Func.Body.Expr -> defaultWriteNode(node, statement){
                onWriteNode(node.expr,statement,node)
            }
            is Node.Block -> defaultWriteNode(node,statement){
                node.stmts.forEach {
                    onWriteNode(it,statement,node)
                    statement.nextLine()
                }
            }
            is Node.Stmt.Decl -> defaultWriteNode(node,statement){
                onWriteNode(node.decl,statement,node)
            }
            is Node.Stmt.Expr -> defaultWriteNode(node,statement){
                onWriteNode(node.expr,statement,node)
            }
            else -> defaultWriteNode(node, statement)
        }
    }

    open fun defaultWriteNode(node: Node,statement: Statement,handle:(()->Unit)? = null){
        val chain = getHandlerChain(node)
        if(node is Node.WithModifiers){
            node.mods.forEach {
                onWriteNode(it,statement,node)
            }
        }
        if(node !is Node.WithModifiers && node is Node.WithAnnotations){
            node.anns.forEach {
                onWriteNode(it,statement,node)
            }
        }
        chain.onHandle(this,node, statement)
        handle?.invoke()
    }

    fun <T:Node> getHandlerChain(node: T):HandlerChain<T>{
        val k = node::class.java
        return handlers.computeIfAbsent(k){
            HandlerChain()
        } as HandlerChain<T>
    }

    fun <T:Node> getHandlerChain(c: Class<T>):HandlerChain<T>{
        val k = c
        return handlers.computeIfAbsent(k){
            HandlerChain()
        } as HandlerChain<T>
    }

    fun addHandler(handler: Handler<out Node>) {
        if(handler.getLanguage() == getLanguage()){
            val c = handler.getHandleClass()
            val chain = getHandlerChain(c) as HandlerChain<Node>
            chain.addHandler(handler as  Handler<Node>)
        }else{
            error("not same language")
        }
    }

}

abstract class BaseLanguageWriter : LanguageWriter(){


}
