package com.liam.gen.swift.expr

import com.liam.gen.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.notSupport
import com.liam.gen.swift.scope.PsiResult
import com.liam.gen.swift.scope.Scope
import org.jetbrains.kotlin.psi.*

open class WhileExpression : Handler<KtWhileExpressionBase>() {

    override fun onGenCode(gen: CodeGen, v: KtWhileExpressionBase, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): PsiResult {
        notSupport()
        return PsiResult.Null
    }

    companion object:WhileExpression()
}