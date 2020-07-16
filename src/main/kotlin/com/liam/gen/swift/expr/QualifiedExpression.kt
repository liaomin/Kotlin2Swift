package com.liam.gen.swift.expr

import com.liam.gen.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.scope.PsiResult
import com.liam.gen.swift.scope.Scope
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtQualifiedExpression

open class QualifiedExpression : Handler<KtQualifiedExpression>() {

    override fun onGenCode(gen: CodeGen, v: KtQualifiedExpression, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): PsiResult {
        val statement = Statement()
        var r = gen.genExpr(v.receiverExpression, scope,targetType, expectType, shouldReturn)
        statement.append(r)
        val targetType = r.returnType
        if(v is KtDotQualifiedExpression){
            statement.append(".")
        }else{
            statement.append("?.")
        }
        v.selectorExpression?.let {
            statement.append(gen.genExpr(it, scope,targetType,expectType, shouldReturn))
        }
        return PsiResult(statement,null,null)
    }

    companion object:QualifiedExpression()

}