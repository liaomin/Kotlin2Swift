package com.liam.gen.swift.expr

import com.liam.gen.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.per.FuncInfo
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
            val argTypeList = ArrayList<FuncInfo.Args>()
            p.valueArgumentList?.arguments?.forEachIndexed { index, ktValueArgument ->
                val name = ktValueArgument.getArgumentName()?.asName?.asString()
                ktValueArgument.getArgumentExpression()?.let {
                    val type = gen.genExpr(it, scope,targetType, expectType, shouldReturn).returnType
                    argTypeList.add(FuncInfo.Args(name, type))
                }
            }
            return PsiResult(statement,name,scope.getFuncType(name,argTypeList))
        }
        return PsiResult(statement,name,scope.getType(name,targetType))
    }

    companion object:NameRefExpression()
}