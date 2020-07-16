package com.liam.ast.writer.swift.exp

import com.liam.ast.psi.Node
import com.liam.ast.writer.BaseLanguageWriter
import com.liam.ast.writer.LanguageWriter
import com.liam.ast.writer.Statement
import com.liam.ast.writer.SwiftHandler

/**
 * @author liaomin
 * @date 6/22/20 9:53 上午
 * @version 1.0
 */
open class ConstHandler : SwiftHandler<Node.Expr.Const>() {

    override fun getHandleClass(): Class<Node.Expr.Const> = Node.Expr.Const::class.java

    override fun onHandle(writer: LanguageWriter, node: Node.Expr.Const, statement: Statement) {
        val from = node.form
        when(from){
            Node.Expr.Const.Form.NULL -> statement.append("nil")
            Node.Expr.Const.Form.INT -> {
                var v = node.value.toLowerCase()
                if(v.endsWith("ul")){
                    statement.append(v.subSequence(0,v.length-2))
                } else if(v.endsWith("l") || v.endsWith("u" )){
                    statement.append(v.subSequence(0,v.length-1))
                }else{
                    statement.append(v)
                }
            }
            Node.Expr.Const.Form.FLOAT -> {
                var v = node.value
                if(v.endsWith("f")){
                    statement.append(v.subSequence(0,v.length-1))
                }else{
                    statement.append(v)
                }
            }
            Node.Expr.Const.Form.CHAR -> {
                statement.append("\"${node.value.subSequence(1,2)}\"")
            }
            else -> {
                statement.append(node.value)
            }
        }
    }

}
