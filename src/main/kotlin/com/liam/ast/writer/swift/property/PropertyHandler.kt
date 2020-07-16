package com.liam.ast.writer.swift.property

import com.liam.ast.psi.Node
import com.liam.ast.writer.LanguageWriter
import com.liam.ast.writer.Statement
import com.liam.ast.writer.SwiftHandler
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.resolve.calls.callUtil.getType

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
                writer.onWriteNode(it,statement,node)
                if(it.type == null && node.expr != null && node.expr is Node.Expr.Const && node.expr.form == Node.Expr.Const.Form.INT){
                    val expr = node.expr
                    val v = expr.value.toLowerCase()
                    if(v.endsWith("ul")){
                        statement.append(":UInt64")
                    }else if(v.endsWith("l")){
                        statement.append(":Int64")
                    }else if(v.endsWith("u")){
                        statement.append(":UInt")
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
