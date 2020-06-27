package com.liam.ast.writer.swift.type

import com.liam.ast.psi.Node
import com.liam.ast.writer.BaseLanguageWriter
import com.liam.ast.writer.LanguageWriter
import com.liam.ast.writer.Statement
import com.liam.ast.writer.SwiftHandler

/**
 * 泛型
 * @author liaomin
 * @date 6/23/20 10:18 上午
 * @version 1.0
 */
open class TypeHandler : SwiftHandler<Node.Type>() {

    override fun getHandleClass(): Class<Node.Type> = Node.Type::class.java

    override fun onHandle(writer: LanguageWriter, node: Node.Type, statement: Statement) {
        writer.onWriteNode(node.ref,statement,node)
    }

}
