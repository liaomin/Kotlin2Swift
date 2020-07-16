package com.liam.gen.swift.expr

import com.liam.ast.writer.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.scope.Scope
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtQualifiedExpression

open class QualifiedExpression : Handler<KtQualifiedExpression> {

    override fun genCode(gen: CodeGen, v: KtQualifiedExpression, statement: Statement, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): String? {
        val targetType = gen.genExpr(v.receiverExpression,statement, scope,targetType, expectType, shouldReturn)
        if(v is KtDotQualifiedExpression){
            statement.append(".")
        }else{
            statement.append("?.")
        }
        v.selectorExpression?.let {
            gen.genExpr(it,statement, scope,targetType,expectType, shouldReturn)
        }
        return null
    }

    companion object:QualifiedExpression()

}