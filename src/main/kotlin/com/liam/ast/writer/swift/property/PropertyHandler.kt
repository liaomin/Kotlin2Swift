package com.liam.ast.writer.swift.property

import com.liam.ast.psi.Node
import com.liam.ast.writer.LanguageWriter
import com.liam.ast.writer.Statement
import com.liam.ast.writer.SwiftHandler

/**
 * @author liaomin
 * @date 6/26/20 1:59 下午
 * @version 1.0
 */
open class PropertyHandler : SwiftHandler<Node.Decl.Property>(){

    override fun getHandleClass(): Class<Node.Decl.Property> = Node.Decl.Property::class.java

    override fun onHandle(writer: LanguageWriter, node: Node.Decl.Property, statement: Statement) {
        if(node.readOnly){
            statement.append("let")
        }else{
            statement.append("var")
        }

        node.vars?.forEach {
            it?.also {
                it.parent = node
                writer.onWriteNode(it,statement)
                if(it.type == null && node.expr != null && node.expr is Node.Expr.Const){
                    val expr = node.expr
                    if(expr.form == Node.Expr.Const.Form.INT && expr.value.endsWith("L")){
                        statement.append(":Int64")
                    }
                }
            }
        }

        node.expr?.also {
            statement.append(" = ")
            writer.onWriteNode(it,statement,node)
        }

        statement.nextLine()
    }

}
