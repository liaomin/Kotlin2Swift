package com.liam.ast.writer.swift.exp.control

import com.liam.ast.psi.Node
import com.liam.ast.writer.LanguageWriter
import com.liam.ast.writer.Statement
import com.liam.ast.writer.SwiftHandler
import com.liam.ast.writer.SwiftWriter

/**
 * @author liaomin
 * @date 6/28/20 5:06 下午
 * @version 1.0
 */
class IFHandler : SwiftHandler<Node.Expr.If>(){

    override fun onHandle(writer: LanguageWriter, node: Node.Expr.If, statement: Statement) {
        val parent = node.parent
        if(parent != null
                && parent is Node.Expr.BinaryOp
                && parent.oper is Node.Expr.BinaryOp.Oper.Token
                && parent.oper.token ==  Node.Expr.BinaryOp.Token.ASSN ) {
            error(" if else assign not support yet, ${node.element?.text}")
        }

        statement.append("if (")
        writer.onWriteNode(node.expr,statement,node)
        statement.append(") ")
        if(node.body !is Node.Expr.Brace){
            statement.append("{ ")
            statement.openQuote()
            statement.nextLine()
        }

        writer.onWriteNode(node.body,statement,node)
        if(node.body !is Node.Expr.Brace){
            statement.closeQuote()
            statement.nextLine()
            statement.append("} ")
        }

        if(node.elseBody != null){
            statement.append(" else ")
            if(node.elseBody is Node.Expr.Name){
                statement.append("{")
                statement.openQuote()
                statement.nextLine()
            }
            writer.onWriteNode(node.elseBody,statement,node)
            if(node.elseBody is Node.Expr.Name){
                statement.closeQuote()
                statement.nextLine()
                statement.append("}")
            }
        }
        statement.nextLine()
    }

    override fun getHandleClass(): Class<Node.Expr.If> = Node.Expr.If::class.java

}
