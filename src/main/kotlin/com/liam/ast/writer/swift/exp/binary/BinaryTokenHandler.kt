package com.liam.ast.writer.swift.exp.binary

import com.liam.ast.psi.Node
import com.liam.ast.writer.BaseLanguageWriter
import com.liam.ast.writer.LanguageWriter
import com.liam.ast.writer.Statement
import com.liam.ast.writer.SwiftHandler

/**
 * @author liaomin
 * @date 6/24/20 3:47 下午
 * @version 1.0
 */
class BinaryTokenHandler : SwiftHandler<Node.Expr.BinaryOp.Oper.Token>(){

    override fun getHandleClass(): Class<Node.Expr.BinaryOp.Oper.Token> = Node.Expr.BinaryOp.Oper.Token::class.java

    override fun onHandle(writer: LanguageWriter, node: Node.Expr.BinaryOp.Oper.Token, statement: Statement) {
        statement.append("${node.token.str}")
    }

}
