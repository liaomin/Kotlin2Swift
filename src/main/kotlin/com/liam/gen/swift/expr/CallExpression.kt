package com.liam.gen.swift.expr

import com.intellij.openapi.util.text.StringUtil
import com.liam.gen.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.Logger
import com.liam.gen.swift.notSupport
import com.liam.gen.per.FuncInfo
import com.liam.gen.scope.PsiResult
import com.liam.gen.scope.Scope
import org.jetbrains.kotlin.psi.KtCallElement
import org.jetbrains.kotlin.psi.KtCallExpression
import java.util.*

open class CallExpression : Handler<KtCallExpression>() {

    fun genArguments(gen: CodeGen,v: KtCallElement,scope: Scope,statement: Statement,funcName:String,targetType: String?,expectType: String?,shouldReturn: Boolean){
        v.valueArgumentList?.arguments?.let {
            val types = LinkedList<FuncInfo.Args>()
            it.forEach {
                var name = it.getArgumentName()?.asName?.asString()
                var type:String? = null
                it.getArgumentExpression()!!.let {
                    type = gen.genExpr(it, scope,targetType, expectType, shouldReturn).returnType
                }
                types.add(FuncInfo.Args(name,type))
            }
            it.forEachIndexed { index, ktValueArgument ->
                if(index>0){
                    statement.append(" , ")
                }
                val name = ktValueArgument.getArgumentName()?.asName?.asString() ?: scope.getFunctionName(funcName,index,types)
                name?.let { statement.append("$it: ") }
                ktValueArgument.getArgumentExpression()?.let {
                    statement.append(gen.genExpr(it, scope,targetType, expectType, shouldReturn) )
                }
            }
        }
    }

    override fun onGenCode(gen: CodeGen, v: KtCallExpression, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): PsiResult {
        val statement = Statement()
        if(v.calleeExpression != null){
            val nameResult = Expr.genCode(gen,v.calleeExpression!!, scope,targetType,expectType, shouldReturn)
            val returnType = nameResult.returnType
            val funcName = nameResult.statement.toString()
            if(StringUtil.isEmpty(funcName)){
                Logger.error("funcName is null")
            }
            statement.append(funcName)
            if(v.typeArguments.size > 0){
                statement.append("<")
                v.typeArguments.forEachIndexed { index, ktTypeProjection ->
                    if(index>0){
                        statement.append(",")
                    }
                    statement.append(gen.genType(ktTypeProjection, scope))
                }
                statement.append(">")
            }
            statement.append("(")
            genArguments(gen,v,scope, statement, funcName, targetType, expectType, shouldReturn)
            statement.append(")")
            return  PsiResult(statement,null,returnType)
        }else{
            notSupport()
        }
        error("")
    }

    companion object:CallExpression()
}