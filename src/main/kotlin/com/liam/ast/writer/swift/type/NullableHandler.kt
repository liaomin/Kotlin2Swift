package com.liam.ast.writer.swift.type

import com.liam.ast.psi.Node
import com.liam.ast.writer.BaseLanguageWriter
import com.liam.ast.writer.LanguageWriter
import com.liam.ast.writer.Statement
import com.liam.ast.writer.SwiftHandler

/**
 * @author liaomin
 * @date 6/23/20 10:18 上午
 * @version 1.0
 */
open class NullableHandler : SwiftHandler<Node.TypeRef.Nullable>() {

    override fun getHandleClass(): Class<Node.TypeRef.Nullable> = Node.TypeRef.Nullable::class.java

    override fun onHandle(writer: LanguageWriter, node: Node.TypeRef.Nullable, statement: Statement) {
        node.type.parent = node
        writer.onWriteNode(node.type,statement)
        statement.append("?")
    }

}
