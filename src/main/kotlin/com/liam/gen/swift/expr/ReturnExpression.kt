package com.liam.gen.swift.expr

import com.liam.ast.writer.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.scope.Scope
import org.jetbrains.kotlin.psi.*

open class ReturnExpression : Handler<KtReturnExpression> {

    override fun genCode(gen: CodeGen, v: KtReturnExpression, statement: Statement, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): String? {
        val l = v.getLabelName() //return where
        v.returnedExpression?.let {
            statement.append("return ")
            return gen.genExpr(it,statement,scope,targetType, expectType, shouldReturn)
        }
        return null
    }

    companion object:ReturnExpression()

}