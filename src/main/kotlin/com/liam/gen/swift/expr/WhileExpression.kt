package com.liam.gen.swift.expr

import com.liam.ast.writer.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.scope.Scope
import org.jetbrains.kotlin.psi.*

open class WhileExpression : Handler<KtWhileExpressionBase> {

    override fun genCode(gen: CodeGen, v: KtWhileExpressionBase, statement: Statement, scope: Scope, targetType:String?, expectType:String?, shouldReturn:Boolean):String?{

        return null
    }

    companion object:WhileExpression()
}