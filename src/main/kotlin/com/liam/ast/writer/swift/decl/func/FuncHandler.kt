package com.liam.ast.writer.swift.decl.func

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
            error("not support")
        }
        statement.nextLine()
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
        node.params.forEachIndexed { index, param ->
            if(index > 0){
                statement.append(",")
            }
            statement.append("_ ")
            writer.onWriteNode(param,statement,node)
        }
        statement.append(")")
        node.type?.also {
            it.parent = node
            statement.append(" -> ")
            writer.onWriteNode(it,statement)
        }


        //TODO
        node.body?.also {
            val isExpr = it is Node.Decl.Func.Body.Expr
            if(isExpr){
                statement.append(" {")
                statement.openQuote()
            }
            writer.onWriteNode(it,statement,node)
            if(isExpr){
                statement.closeQuote()
                statement.append("}")
            }
        }

    }
}
