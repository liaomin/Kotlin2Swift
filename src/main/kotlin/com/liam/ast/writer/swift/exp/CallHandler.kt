package com.liam.ast.writer.swift.exp

import com.liam.ast.psi.Node
import com.liam.ast.writer.LanguageWriter
import com.liam.ast.writer.Statement
import com.liam.ast.writer.SwiftHandler

/**
 * @author liaomin
 * @date 6/29/20 10:33 上午
 * @version 1.0
 */
class CallHandler : SwiftHandler<Node.Expr.Call>(){

    override fun onHandle(writer: LanguageWriter, node: Node.Expr.Call, statement: Statement) {
        writer.onWriteNode(node.expr,statement,node)
        statement.append("(")
        node.args.forEachIndexed { index, valueArg ->
            if(index > 0){
                statement.append(",")
            }
            writer.onWriteNode(valueArg,statement,node)
        }
        statement.append(")")
    }

    override fun getHandleClass(): Class<Node.Expr.Call> = Node.Expr.Call::class.java

}
