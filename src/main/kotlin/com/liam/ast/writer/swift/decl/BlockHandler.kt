package com.liam.ast.writer.swift.decl

import com.liam.ast.psi.Node
import com.liam.ast.writer.LanguageWriter
import com.liam.ast.writer.Statement
import com.liam.ast.writer.SwiftHandler

/**
 * @author liaomin
 * @date 6/26/20 5:43 下午
 * @version 1.0
 */

class BlockHandler : SwiftHandler<Node.Block>(){
    override fun onHandle(writer: LanguageWriter, node: Node.Block, statement: Statement) {
        statement.append("{")
        statement.openQuote()
        node.stmts.forEach {
            writer.onWriteNode(it,statement,node)
            statement.nextLine()
        }
        statement.closeQuote()
        statement.append("}")
    }

    override fun getHandleClass(): Class<Node.Block> = Node.Block::class.java

}
