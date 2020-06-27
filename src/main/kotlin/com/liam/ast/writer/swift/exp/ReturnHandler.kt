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
open class ReturnHandler : SwiftHandler<Node.Expr.Return>() {

    override fun getHandleClass(): Class<Node.Expr.Return> = Node.Expr.Return::class.java

    override fun onHandle(writer: LanguageWriter, node: Node.Expr.Return, statement: Statement) {
        statement.append("return ")
        node.expr?.also {
            it.parent = node
            writer.onWriteNode(it,statement)
        }
    }

}
