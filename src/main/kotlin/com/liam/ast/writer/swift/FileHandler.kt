package com.liam.ast.writer.swift

import com.liam.ast.psi.Node
import com.liam.ast.writer.BaseLanguageWriter
import com.liam.ast.writer.LanguageWriter
import com.liam.ast.writer.Statement
import com.liam.ast.writer.SwiftHandler

/**
 * @author liaomin
 * @date 6/25/20 10:32 下午
 * @version 1.0
 */
open class FileHandler : SwiftHandler<Node.File>(){

    override fun onHandle(writer: LanguageWriter, node: Node.File, statement: Statement) {
        node.pkg?.also {
            it.parent = node
            writer.onWriteNode(it,statement)
        }
        node.imports.forEach{
            it.parent = node
            writer.onWriteNode(it,statement)
        }
        node.decls.forEach{
            it.parent = node
            writer.onWriteNode(it,statement)
        }
    }

    override fun getHandleClass(): Class<Node.File> = Node.File::class.java

}
