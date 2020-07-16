package com.liam.ast.writer.swift.decl.func

import com.liam.ast.psi.Node
import com.liam.ast.writer.LanguageWriter
import com.liam.ast.writer.Statement
import com.liam.ast.writer.SwiftHandler

/**
 * @author liaomin
 * @date 6/29/20 10:55 上午
 * @version 1.0
 */
class FuncParamHandler : SwiftHandler<Node.Decl.Func.Param>(){

    override fun onHandle(writer: LanguageWriter, node: Node.Decl.Func.Param, statement: Statement) {
        statement.append("${node.name}")
        node.type?.also {
            statement.append(":")
            writer.onWriteNode(it,statement,node)
        }
        node.default?.also {
            statement.append("=")
            writer.onWriteNode(it,statement,node)
        }

    }

    override fun getHandleClass(): Class<Node.Decl.Func.Param> = Node.Decl.Func.Param::class.java

}
