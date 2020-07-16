package com.liam.gen.swift.expr

import com.intellij.openapi.util.text.StringUtil
import com.liam.gen.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.scope.Scope
import com.liam.gen.swift.notSupport
import com.liam.gen.swift.scope.PsiResult
import org.jetbrains.kotlin.psi.KtCallExpression
import java.util.*

open class CallExpression : Handler<KtCallExpression>() {

    companion object:CallExpression()

    override fun onGenCode(gen: CodeGen, v: KtCallExpression, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): PsiResult {
        val statement = Statement()
        if(v.calleeExpression != null){
            val nameResult = Expr.genCode(gen,v.calleeExpression!!, scope,targetType,expectType, shouldReturn)
            val returnType = nameResult.returnType
            val funcName = nameResult.statement.toString()
            if(StringUtil.isEmpty(funcName)){
                error("funcName is null")
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
            v.valueArgumentList?.arguments?.let {
                val types = LinkedList<String?>()
                it.forEach {
                    it.getArgumentExpression()!!.let {
                        types.add(gen.genExpr(it, scope,targetType, expectType, shouldReturn).returnType)
                    }
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
            statement.append(")")
            return  PsiResult(statement,null,returnType)
        }else{
            notSupport()
        }
        error("")
    }
}