package com.liam.ast.writer.swift.decl

import com.liam.ast.psi.Node
import com.liam.ast.writer.LanguageWriter
import com.liam.ast.writer.Statement
import com.liam.ast.writer.SwiftHandler

/**
 * @author liaomin
 * @date 6/23/20 10:05 上午
 * @version 1.0
 */
open class StructuredHandler : SwiftHandler<Node.Decl.Structured>(){

    override fun getHandleClass(): Class<Node.Decl.Structured> = Node.Decl.Structured::class.java

    override fun onHandle(writer: LanguageWriter, node: Node.Decl.Structured, statement: Statement) {
        when (node.form){
            Node.Decl.Structured.Form.CLASS -> ClassHandler.onHandle(writer, node, statement)
        }
    }

}
