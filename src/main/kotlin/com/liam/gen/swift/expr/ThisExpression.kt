package com.liam.gen.swift.expr

import com.liam.gen.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.scope.PsiResult
import com.liam.gen.swift.scope.Scope
import org.jetbrains.kotlin.psi.*

open class ThisExpression : Handler<KtThisExpression>() {

    override fun onGenCode(gen: CodeGen, v: KtThisExpression, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): PsiResult {
        val statement = Statement()
        statement.append("self")
        return PsiResult(statement,null,null)
    }

    companion object:ThisExpression()

}