package com.liam.gen.swift.expr

import com.liam.gen.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.notSupport
import com.liam.gen.scope.PsiResult
import com.liam.gen.scope.Scope
import org.jetbrains.kotlin.psi.*

open class ReturnExpression : Handler<KtReturnExpression>() {

    override fun onGenCode(gen: CodeGen, v: KtReturnExpression, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): PsiResult {
        val statement = Statement()
        val l = v.getLabelName() //return where
        if(l != null){
            notSupport()
        }
        v.returnedExpression?.let {
            statement.append("return ")
            val r = gen.genExpr(it,scope,targetType, expectType, shouldReturn)
            statement.append(r)
            PsiResult(statement,null,r.returnType)
        }
        return PsiResult(statement,null,null)
    }

    companion object:ReturnExpression()

}