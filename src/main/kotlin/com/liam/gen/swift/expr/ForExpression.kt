package com.liam.gen.swift.expr

import com.liam.ast.writer.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.scope.Scope
import org.jetbrains.kotlin.psi.*

open class ForExpression : Handler<KtForExpression> {

    var index = 0

    fun genIfTempName():String{
        return "___IF_TEMP_${++index}__"
    }


    override fun genCode(gen: CodeGen, v: KtForExpression, statement: Statement, scope: Scope, targetType:String?, expectType:String?, shouldReturn:Boolean):String?{

        return null
    }

    companion object:ForExpression()
}