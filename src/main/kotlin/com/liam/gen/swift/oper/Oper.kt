package com.liam.gen.swift.oper

import com.liam.gen.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.scope.Scope
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtOperationReferenceExpression

open class Oper {

    fun genCode(gen: CodeGen, left: KtExpression, oper: KtOperationReferenceExpression, right:KtExpression, statement: Statement, scope: Scope){
        statement.append(" ${oper.text} ")
    }

    companion object:Oper()
}