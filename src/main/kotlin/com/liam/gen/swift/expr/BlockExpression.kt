package com.liam.gen.swift.expr

import com.liam.gen.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.scope.PsiResult
import com.liam.gen.scope.Scope
import org.jetbrains.kotlin.psi.*

open class BlockExpression : Handler<KtBlockExpression>() {

    companion object:BlockExpression()

    override fun onGenCode(gen: CodeGen, v: KtBlockExpression, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): PsiResult {
        val statement = Statement()
        val newScope = scope.newScope()
        var type:String? = null
        v.statements?.let {
            if(it.size > 0){
                val lastIsReturnExpression = it.last() is KtReturnExpression
                it.forEachIndexed { index, ktExpression ->
                    var s:Statement? = null
                    val currentLine = statement.currentLine
                    val isLast = index == it.size - 1
                    if(isLast){
                        var r = gen.genExpr(ktExpression,newScope,targetType,expectType,shouldReturn)
                        s = r.statement
                        type = r.returnType
                        if(shouldReturn && !lastIsReturnExpression){
                            statement.append("return ")
                        }
                    }else{
                        var r = gen.genExpr(ktExpression,newScope,targetType,expectType,shouldReturn && !lastIsReturnExpression)
                        s = r.statement
                        type = r.returnType
                    }
                    statement.append(s)
                    if(!isLast && statement.currentLine == currentLine){
                        statement.nextLine()
                    }
                }
            }
        }
        return PsiResult(statement,null,type)
    }
}