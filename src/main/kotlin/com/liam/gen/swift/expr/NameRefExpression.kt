package com.liam.gen.swift.expr

import com.liam.ast.writer.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.scope.Scope
import org.jetbrains.kotlin.psi.*

open class NameRefExpression : Handler<KtNameReferenceExpression> {

    override fun genCode(gen: CodeGen, v: KtNameReferenceExpression, statement: Statement, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): String? {
        val name =  v.getReferencedName()
        statement.append(name)
        val p = v.parent
        if(p is KtCallExpression){
            val argTypeList = ArrayList<String>()
            p.valueArgumentList?.arguments?.forEachIndexed { index, ktValueArgument ->
                ktValueArgument.getArgumentExpression()?.let {
                    val s = statement.newStatement()
                    val t = gen.genExpr(it,s, scope,targetType, expectType, shouldReturn)
                    argTypeList.add(t!!)
                }
            }
            return scope.getFuncType(name,argTypeList)
        }
        return scope.getType(name,targetType)
    }

    companion object:NameRefExpression()
}