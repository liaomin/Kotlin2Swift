package com.liam.gen.swift.expr

import com.liam.gen.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.scope.Scope
import com.liam.gen.swift.oper.Oper
import com.liam.gen.swift.scope.PsiResult
import org.jetbrains.kotlin.psi.KtBinaryExpression

open class BinaryExpression : Handler<KtBinaryExpression>() {

    override fun onGenCode(gen: CodeGen, v: KtBinaryExpression, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): PsiResult {
        val statement = Statement()
        var expect= expectType
        val left = v.left ?: error("no left type")
        val right = v.right ?: error("no right type")
        var r = gen.genExpr(left, scope,null,expectType,shouldReturn)
        statement.append(r)
        expect = expect?:r.returnType
        Oper.genCode(gen,left,v.operationReference,right,statement, scope)
        r = gen.genExpr(right, scope,null,expectType,shouldReturn)
        statement.append(r)
        return PsiResult(statement,null,expect ?: r.returnType)
    }

    companion object:BinaryExpression()
}