package com.liam.gen.swift.expr

import com.liam.gen.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.scope.PsiResult
import com.liam.gen.swift.scope.Scope
import org.jetbrains.kotlin.psi.*

open class NameRefExpression : Handler<KtNameReferenceExpression>() {

    override fun onGenCode(gen: CodeGen, v: KtNameReferenceExpression, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): PsiResult {
        val statement = Statement()
        val name =  v.getReferencedName()
        statement.append(name)
        val p = v.parent
        if(p is KtCallExpression){
            val argTypeList = ArrayList<String>()
            p.valueArgumentList?.arguments?.forEachIndexed { index, ktValueArgument ->
                ktValueArgument.getArgumentExpression()?.let {
                    argTypeList.add(gen.genExpr(it, scope,targetType, expectType, shouldReturn).returnType!!)
                }
            }
            return PsiResult(statement,name,scope.getFuncType(name,argTypeList))
        }
        return PsiResult(statement,name,scope.getType(name,targetType))
    }

    companion object:NameRefExpression()
}