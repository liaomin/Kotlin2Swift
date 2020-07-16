package com.liam.ast.writer.swift.exp.control

import com.liam.ast.psi.Node
import com.liam.ast.writer.LanguageWriter
import com.liam.ast.writer.Statement
import com.liam.ast.writer.SwiftHandler

/**
 * @author liaomin
 * @date 6/28/20 5:06 下午
 * @version 1.0
 */
class WhenHandler : SwiftHandler<Node.Expr.When>(){

    companion object{
        private var index  = 0
        private fun getName():String{
            return "___when_temp_name_${index++}___"
        }
    }

    override fun onHandle(writer: LanguageWriter, node: Node.Expr.When, statement: Statement) {
        val whenName = getName()
        node.expr?.also {
            statement.append("let $whenName = ")
            writer.onWriteNode(it,statement,node)
            statement.nextLine()
            val entriesSize =  node.entries.size
            node.entries.forEachIndexed { index, entry ->
                var haveConds = true
                if( entriesSize == 1 && entry.conds.size == 0){
                    writeExpr(entry.body,writer,statement,entry,false)
                    return
                }else{
                    if(index == 0){
                        statement.append("if($whenName")
                    }else{
                        if(index == entriesSize - 1 && entry.conds.size == 0 ){
                            statement.append("else")
                            haveConds = false
                        }else{
                            statement.append("else if($whenName")
                        }

                    }
                }

                entry.conds.forEach {
                    when (it){
                        is Node.Expr.When.Cond.Expr -> {

                        }
                        is Node.Expr.When.Cond.Is -> {
                            if(it.not){
                                statement.append("!(")
                            }
                            statement.append(" is ")
                            writer.onWriteNode(it.type,statement,node)
                            if(it.not){
                                statement.append(")")
                            }
                        }
                        is Node.Expr.When.Cond.In -> {
                            println()
                        }
                    }
                }
                if(haveConds)statement.append(")")
                writeExpr(entry.body,writer,statement,entry)

            }
        }

    }

    fun writeExpr(body:Node.Expr,writer: LanguageWriter,statement: Statement,parent:Node,addOper:Boolean = true){
        val isBlock = body is Node.Expr.Brace
        if(!isBlock && addOper){
            statement.append(" {")
            statement.openQuote()
            statement.nextLine()
        }

        writer.onWriteNode(body,statement,parent)

        if(!isBlock && addOper){
            statement.closeQuote()
            statement.nextLine()
            statement.append("}")
        }
    }

    override fun getHandleClass(): Class<Node.Expr.When> = Node.Expr.When::class.java

}
