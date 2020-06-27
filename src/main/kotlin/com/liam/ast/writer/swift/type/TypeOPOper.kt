package com.liam.ast.writer.swift.type

import com.liam.ast.psi.Node
import com.liam.ast.writer.LanguageWriter
import com.liam.ast.writer.Statement
import com.liam.ast.writer.SwiftHandler

/**
 * @author liaomin
 * @date 6/26/20 9:03 下午
 * @version 1.0
 */
class TypeOPOper : SwiftHandler<Node.Expr.TypeOp.Oper>(){

    override fun onHandle(writer: LanguageWriter, node: Node.Expr.TypeOp.Oper, statement: Statement) {
        when(node.token){
            Node.Expr.TypeOp.Token.AS -> {
                val p = node.parent
                if(p is Node.Expr.TypeOp && p.rhs is Node.Type && p.rhs.ref is Node.TypeRef.Nullable){
                    statement.append(" as ")
                }else {
                    statement.append(" as! ")
                }
            }
            Node.Expr.TypeOp.Token.AS_SAFE -> {
                val p = node.parent
                if(p is Node.Expr.TypeOp && p.rhs is Node.Type && p.rhs.ref is Node.TypeRef.Nullable){
                    statement.append(" as ")
                }else{
                    statement.append(" as? ")
                }
            }
            Node.Expr.TypeOp.Token.COL -> statement.append(":")
            Node.Expr.TypeOp.Token.IS -> statement.append(" is ")
            Node.Expr.TypeOp.Token.NOT_IS -> statement.append(" !is ")
        }
    }

    override fun getHandleClass() = Node.Expr.TypeOp.Oper::class.java

}
