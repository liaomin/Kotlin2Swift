package com.liam.gen.swift.expr

import com.liam.gen.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.scope.PsiResult
import com.liam.gen.scope.Scope
import org.jetbrains.kotlin.psi.KtBinaryExpressionWithTypeRHS

open class BinaryWithTypeRHS : Handler<KtBinaryExpressionWithTypeRHS>() {

    override fun onGenCode(gen: CodeGen, v: KtBinaryExpressionWithTypeRHS, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): PsiResult {
        val statement = Statement()
        var expect= expectType
        val left = v.left ?: error("no left type")
        val right = v.right ?: error("no right type")
        var r = gen.genExpr(left, scope,null,expectType,shouldReturn)
        statement.append(r)
        statement.append(" ${v.operationReference.text} ")
        val rightType = gen.genType(right, scope)
        statement.append(rightType)
        return PsiResult(statement,null,expect ?: rightType)
    }

    companion object:BinaryWithTypeRHS()
}