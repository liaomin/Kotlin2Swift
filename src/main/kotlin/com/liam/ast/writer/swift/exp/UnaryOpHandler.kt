package com.liam.ast.writer.swift.exp

import com.liam.ast.psi.Node
import com.liam.ast.writer.BaseLanguageWriter
import com.liam.ast.writer.LanguageWriter
import com.liam.ast.writer.Statement
import com.liam.ast.writer.SwiftHandler

/**
 * @author liaomin
 * @date 6/22/20 9:53 上午
 * @version 1.0
 */
open class UnaryOpHandler : SwiftHandler<Node.Expr.UnaryOp>() {

    override fun getHandleClass(): Class<Node.Expr.UnaryOp> = Node.Expr.UnaryOp::class.java

    override fun onHandle(writer: LanguageWriter, node: Node.Expr.UnaryOp, statement: Statement) {
        node.expr.parent = node
        node.parent = node
        val toke = node.oper.token
        if(node.expr is Node.Expr.Name){
            val name = node.expr.name
            if(toke == Node.Expr.UnaryOp.Token.INC || toke == Node.Expr.UnaryOp.Token.DEC){
                val op = if(toke == Node.Expr.UnaryOp.Token.INC) "+" else "-"
                if(node.prefix){
                    statement.insertBeforeLine("$name $op= 1")
                    statement.append(name)
                }else{
                    statement.append(name)
                    statement.appendBeforeNewLine("$name $op= 1")
                }
                return
            }
        }
        if(node.prefix){
            writer.onWriteNode(node.oper,statement)
            writer.onWriteNode(node.expr,statement)
        }else{
            writer.onWriteNode(node.expr,statement)
            writer.onWriteNode(node.oper,statement)
        }
    }

}

open class UnaryOpOperHandler : SwiftHandler<Node.Expr.UnaryOp.Oper>() {

    override fun getHandleClass(): Class<Node.Expr.UnaryOp.Oper> = Node.Expr.UnaryOp.Oper::class.java

    override fun onHandle(writer: LanguageWriter, node: Node.Expr.UnaryOp.Oper, statement: Statement) {
        when (node.token){
            Node.Expr.UnaryOp.Token.NEG-> statement.append("-")
            Node.Expr.UnaryOp.Token.POS-> statement.append("+")
//            Node.Expr.UnaryOp.Token.INC-> statement.append("++")
//            Node.Expr.UnaryOp.Token.DEC-> statement.append("--")
            Node.Expr.UnaryOp.Token.NOT-> statement.append("!")
            Node.Expr.UnaryOp.Token.NULL_DEREF-> statement.append(" ! ")
        }
    }
}

