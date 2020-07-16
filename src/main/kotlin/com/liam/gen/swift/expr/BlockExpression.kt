package com.liam.gen.swift.expr

import com.liam.ast.writer.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.scope.Scope
import org.jetbrains.kotlin.psi.*

open class BlockExpression : Handler<KtBlockExpression> {

    companion object:BlockExpression()

    override fun genCode(gen: CodeGen, v: KtBlockExpression, statement: Statement, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): String? {
        val newScope = scope.newScope()
        var type:String? = null
        v.statements?.let {
            if(it.size > 0){
                val lastIsReturnExpression = it.last() is KtReturnExpression
                it.forEachIndexed { index, ktExpression ->
                    val s = statement.newStatement()
                    val currentLine = statement.currentLine
                    val isLast = index == it.size - 1
                    if(isLast){
                        type = gen.genExpr(ktExpression,s,newScope,targetType,expectType,shouldReturn)
                        if(shouldReturn && !lastIsReturnExpression){
                            statement.append("return ")
                        }
                    }else{
                        type = gen.genExpr(ktExpression,s,newScope,targetType,expectType,shouldReturn && !lastIsReturnExpression)
                    }
                    statement.append(s)
                    if(!isLast && statement.currentLine == currentLine){
                        statement.nextLine()
                    }
                }
            }

        }
        return type
    }
}