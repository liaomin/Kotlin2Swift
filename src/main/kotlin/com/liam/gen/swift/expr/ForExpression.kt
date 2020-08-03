package com.liam.gen.swift.expr

import com.liam.gen.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.scope.PsiResult
import com.liam.gen.scope.Scope
import org.jetbrains.kotlin.psi.*

open class ForExpression : Handler<KtForExpression>() {

    companion object:ForExpression()

    override fun onGenCode(gen: CodeGen, v: KtForExpression, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): PsiResult {
        val statement = Statement()
        return PsiResult(statement,null,null)
    }
}