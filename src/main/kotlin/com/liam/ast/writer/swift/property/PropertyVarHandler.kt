package com.liam.ast.writer.swift.property

import com.liam.ast.psi.Node
import com.liam.ast.writer.LanguageWriter
import com.liam.ast.writer.Statement
import com.liam.ast.writer.SwiftHandler

/**
 * @author liaomin
 * @date 6/26/20 1:59 下午
 * @version 1.0
 */
open class PropertyVarHandler : SwiftHandler<Node.Decl.Property.Var>(){

    override fun getHandleClass(): Class<Node.Decl.Property.Var> = Node.Decl.Property.Var::class.java

    override fun onHandle(writer: LanguageWriter, node: Node.Decl.Property.Var, statement: Statement) {
        statement.append(" ${node.name}")

        node.type?.also {
            it.parent = node
            it.ref.parent = it
            statement.append(":")
            writer.onWriteNode(it.ref,statement)
        }

    }

}
