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
open class ThisHandler : SwiftHandler<Node.Expr.This>() {

    override fun getHandleClass(): Class<Node.Expr.This> = Node.Expr.This::class.java

    override fun onHandle(writer: LanguageWriter, node: Node.Expr.This, statement: Statement) {
        statement.append("self")
    }

}
