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

open class PieceSimpleHandler : SwiftHandler<Node.TypeRef.Simple>() {

    override fun getHandleClass(): Class<Node.TypeRef.Simple> = Node.TypeRef.Simple::class.java

    override fun onHandle(writer: LanguageWriter, node: Node.TypeRef.Simple, statement: Statement) {
        node.pieces.forEach {
            it.parent = node
            writer.onWriteNode(it,statement)
        }

    }
}

open class PieceSimplePieceHandler : SwiftHandler<Node.TypeRef.Simple.Piece>() {

    override fun getHandleClass(): Class<Node.TypeRef.Simple.Piece> = Node.TypeRef.Simple.Piece::class.java

    override fun onHandle(writer: LanguageWriter, node: Node.TypeRef.Simple.Piece, statement: Statement) {
        val name = node.name
        when (name){
            "Long" -> statement.append("Int64")
            else -> statement.append("$name")
        }
    }

}
