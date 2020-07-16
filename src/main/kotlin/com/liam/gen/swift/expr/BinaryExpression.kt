package com.liam.gen.swift.expr

import com.liam.ast.writer.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.scope.Scope
import com.liam.gen.swift.oper.Oper
import org.jetbrains.kotlin.psi.KtBinaryExpression

open class BinaryExpression : Handler<KtBinaryExpression> {

    override fun genCode(gen: CodeGen, v: KtBinaryExpression, statement: Statement, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): String? {
        var expect= expectType
        val left = v.left ?: error("no left type")
        val right = v.right ?: error("no right type")
        val leftType = gen.genExpr(left,statement, scope,null,expectType,shouldReturn)
        expect = expect?:leftType
        Oper.genCode(gen,left,v.operationReference,right,statement, scope)
        val rightType = gen.genExpr(right,statement, scope,null,expectType,shouldReturn)
        return expect ?: rightType
    }

    companion object:BinaryExpression()
}