package com.liam.ast.writer.swift.exp.binary

import com.liam.ast.psi.Node
import com.liam.ast.writer.LanguageWriter
import com.liam.ast.writer.Statement
import com.liam.ast.writer.SwiftHandler

/**
 * @author liaomin
 * @date 6/26/20 2:12 下午
 * @version 1.0
 */
class BinaryOpHandler : SwiftHandler<Node.Expr.BinaryOp>(){

    override fun onHandle(writer: LanguageWriter, node: Node.Expr.BinaryOp, statement: Statement) {
        writer.onWriteNode(node.lhs,statement,node)
        writer.onWriteNode(node.oper,statement,node)
        writer.onWriteNode(node.rhs,statement,node)
    }

    override fun getHandleClass(): Class<Node.Expr.BinaryOp> = Node.Expr.BinaryOp::class.java

}
