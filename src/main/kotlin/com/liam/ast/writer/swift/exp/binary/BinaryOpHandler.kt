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
        if((node.lhs is Node.Expr.Const || node.lhs is Node.Expr.Name)
                && node.oper is Node.Expr.BinaryOp.Oper.Token
                && node.oper.token == Node.Expr.BinaryOp.Token.DOT
                && node.rhs is Node.Expr.Call
                && node.rhs.args.isEmpty()
                && node.rhs.expr is Node.Expr.Name) {
            val name = node.rhs.expr.name
            when (name) {
                "inv" -> {
                    if(node.lhs is Node.Expr.Const){
                        statement.append("~${node.lhs.value}")
                    }else if(node.lhs is Node.Expr.Name){
                        statement.append("~${node.lhs.name}")
                    }
                    return
                }
                "toByte" -> {
                    if(node.lhs is Node.Expr.Const){
                        statement.append("Int8(${node.lhs.value})")
                    }else if(node.lhs is Node.Expr.Name){
                        statement.append("Int8(${node.lhs.name})")
                    }
                    return
                }
                "toShort" -> {
                    if(node.lhs is Node.Expr.Const){
                        statement.append("Int16(${node.lhs.value})")
                    }else if(node.lhs is Node.Expr.Name){
                        statement.append("Int16(${node.lhs.name})")
                    }
                    return
                }
                "toInt" -> {
                    if(node.lhs is Node.Expr.Const){
                        statement.append("Int(${node.lhs.value})")
                    }else if(node.lhs is Node.Expr.Name){
                        statement.append("Int(${node.lhs.name})")
                    }
                    return
                }
                "toLong" -> {
                    if(node.lhs is Node.Expr.Const){
                        statement.append("Int64(${node.lhs.value})")
                    }else if(node.lhs is Node.Expr.Name){
                        statement.append("Int64(${node.lhs.name})")
                    }
                    return
                }
                "toFloat" -> {
                    if(node.lhs is Node.Expr.Const){
                        statement.append("Float(${node.lhs.value})")
                    }else if(node.lhs is Node.Expr.Name){
                        statement.append("Float(${node.lhs.name})")
                    }
                    return
                }
                "toDouble" -> {
                    if(node.lhs is Node.Expr.Const){
                        statement.append("Double(${node.lhs.value})")
                    }else if(node.lhs is Node.Expr.Name){
                        statement.append("Double(${node.lhs.name})")
                    }
                    return
                }
                "toChar" -> {
                    if(node.lhs is Node.Expr.Const){
                        statement.append("${node.lhs.value}.toChar()")
                    }else if(node.lhs is Node.Expr.Name){
                        statement.append("${node.lhs.name}.toChar()")
                    }
                    return
                }
            }
        }


        writer.onWriteNode(node.lhs,statement,node)
        writer.onWriteNode(node.oper,statement,node)
        writer.onWriteNode(node.rhs,statement,node)
    }

    override fun getHandleClass(): Class<Node.Expr.BinaryOp> = Node.Expr.BinaryOp::class.java

}
