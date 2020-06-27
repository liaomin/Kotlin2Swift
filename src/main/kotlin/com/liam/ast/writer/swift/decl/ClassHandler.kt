package com.liam.ast.writer.swift.decl

import com.liam.ast.psi.Node
import com.liam.ast.writer.LanguageWriter
import com.liam.ast.writer.Statement

/**
 * @author liaomin
 * @date 6/26/20 2:45 下午
 * @version 1.0
 */
class ClassHandler {

    companion object{
        fun onHandle(writer: LanguageWriter, node: Node.Decl.Structured, statement: Statement){
            if(node.form == Node.Decl.Structured.Form.CLASS){
                statement.append("class ${node.name} ")
                if(node.typeParams.size > 0){
                    statement.append("<")
                    node.typeParams.forEachIndexed { index, typeParam ->
                        if(index>0){
                            statement.append(",")
                        }
                        writer.onWriteNode(typeParam,statement)
                    }
                    statement.append("> ")
                }
                statement.append("{")
                statement.openQuote()
                statement.next2Line()

                node.members.forEach {
                    it.parent = node
                    writer.onWriteNode(it,statement)
                }
                //TODO


                statement.closeQuote()
                statement.nextLine()
                statement.append("}")
                statement.next2Line()
            }
        }
    }

}
