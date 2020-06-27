package com.liam.ast.writer.swift.decl

import com.liam.ast.psi.Node
import com.liam.ast.writer.LanguageWriter
import com.liam.ast.writer.Statement
import com.liam.ast.writer.SwiftHandler

/**
 * @author liaomin
 * @date 6/24/20 4:01 下午
 * @version 1.0
 */
class FuncHandler : SwiftHandler<Node.Decl.Func>(){

    override fun getHandleClass(): Class<Node.Decl.Func> = Node.Decl.Func::class.java

    override fun onHandle(writer: LanguageWriter, node: Node.Decl.Func, statement: Statement) {
        if(node.receiverType != null){
            node.receiverType.parent = node
            //TODO 扩展方法
        }
        statement.append("func ${node.name}")
        if(node.typeParams.size > 0){
            statement.append("<")
            node.typeParams.forEachIndexed { index, typeParam ->
                if(index>0){
                    statement.append(",")
                }
                writer.onWriteNode(typeParam,statement)
            }
            statement.append(">")
        }
        statement.append("(")
        statement.append(")")
        node.type?.also {
            it.parent = node
            statement.append(" -> ")
            writer.onWriteNode(it,statement)
        }
        statement.append(" { ")
        statement.openQuote()
        statement.nextLine()

        //TODO
        node.body?.also { writer.onWriteNode(it,statement)  }

        statement.closeQuote()
        statement.nextLine()
        statement.append("}")


    }
}
