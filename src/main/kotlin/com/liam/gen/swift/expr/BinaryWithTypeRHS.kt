package com.liam.gen.swift.expr

import com.liam.ast.writer.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.scope.Scope
import org.jetbrains.kotlin.psi.KtBinaryExpressionWithTypeRHS

open class BinaryWithTypeRHS : Handler<KtBinaryExpressionWithTypeRHS> {

    override fun genCode(gen: CodeGen, v: KtBinaryExpressionWithTypeRHS, statement: Statement, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): String? {
        var expect= expectType
        val left = v.left ?: error("no left type")
        val right = v.right ?: error("no right type")
        gen.genExpr(left,statement, scope,null,expectType,shouldReturn)
        statement.append(" ${v.operationReference.text} ")
        val rightType = gen.genType(right,statement, scope)
        return expect ?: rightType
    }

    companion object:BinaryWithTypeRHS()
}