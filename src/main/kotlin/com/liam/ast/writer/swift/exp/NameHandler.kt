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
open class NameHandler : SwiftHandler<Node.Expr.Name>() {

    override fun getHandleClass(): Class<Node.Expr.Name> = Node.Expr.Name::class.java

    override fun onHandle(writer: LanguageWriter, node: Node.Expr.Name, statement: Statement) {
        statement.append("${node.name}")
    }

}
