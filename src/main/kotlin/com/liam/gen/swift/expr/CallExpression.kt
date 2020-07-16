package com.liam.gen.swift.expr

import com.intellij.openapi.util.text.StringUtil
import com.liam.ast.writer.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.scope.Scope
import com.liam.gen.swift.notSupport
import org.jetbrains.kotlin.psi.KtCallExpression
import java.util.*

open class CallExpression : Handler<KtCallExpression> {
    override fun genCode(gen: CodeGen, v: KtCallExpression, statement: Statement, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): String? {
        if(v.calleeExpression != null){
            val nameStatement = statement.newStatement()
            val returnType = Expr.genCode(gen,v.calleeExpression!! , nameStatement, scope,targetType,expectType, shouldReturn)
            val funcName = nameStatement.toString()
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
                    gen.genType(ktTypeProjection,statement, scope)
                }
                statement.append(">")
            }
            statement.append("(")
            v.valueArgumentList?.arguments?.let {
                val types = LinkedList<String?>()
                val s = statement.newStatement()
                it.forEach {
                    it.getArgumentExpression()!!.let {
                        val type = gen.genExpr(it,s, scope,targetType, expectType, shouldReturn)
                        types.add(type)
                    }
                }
                it.forEachIndexed { index, ktValueArgument ->
                    if(index>0){
                        statement.append(" , ")
                    }
                    val name = ktValueArgument.getArgumentName()?.asName?.asString() ?: scope.getFunctionName(funcName,index,types)
                    name?.let { statement.append("$it: ") }
                    ktValueArgument.getArgumentExpression()?.let { gen.genExpr(it,statement, scope,targetType, expectType, shouldReturn)  }
                }
            }
            statement.append(")")
            return returnType
        }else{
            notSupport()
        }
        return null
    }

    companion object:CallExpression()
}