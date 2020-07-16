package com.liam.ast.writer.swift.exp

import com.liam.ast.psi.Node
import com.liam.ast.writer.BaseLanguageWriter
import com.liam.ast.writer.LanguageWriter
import com.liam.ast.writer.Statement
import com.liam.ast.writer.SwiftHandler
import java.lang.StringBuilder

/**
 * @author liaomin
 * @date 6/22/20 9:53 上午
 * @version 1.0
 */
open class StringTmplHandler : SwiftHandler<Node.Expr.StringTmpl>() {

    override fun getHandleClass(): Class<Node.Expr.StringTmpl> = Node.Expr.StringTmpl::class.java

    override fun onHandle(writer: LanguageWriter, node: Node.Expr.StringTmpl, statement: Statement) {
        val b  = StringBuilder()
        node.elems.forEach {
            b.append(it.toString())
        }
        if(node.raw){
            statement.append("\"\"\"")
        }else{
            statement.append("\"")
        }
        statement.append(b.toString())
        if(node.raw){
            statement.append("\"\"\"")
        }else{
            statement.append("\"")
        }
    }

}
