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
class BinaryInfixHandler : SwiftHandler<Node.Expr.BinaryOp.Oper.Infix>(){

    override fun getHandleClass(): Class<Node.Expr.BinaryOp.Oper.Infix> = Node.Expr.BinaryOp.Oper.Infix::class.java

    override fun onHandle(writer: LanguageWriter, node: Node.Expr.BinaryOp.Oper.Infix, statement: Statement) {
        when (node.str) {
            "shl" -> statement.append(" << ")
            "shr" -> statement.append(" >> ")
            "ushr" -> statement.append(" >> ")
            "and" -> statement.append(" & ")
            "or" -> statement.append(" | ")
            "xor" -> statement.append(" ^ ")
            else ->{
                try {
                    error("un support infix ${node.str}")
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
    }

}
