package com.liam.gen.swift.expr

import com.liam.gen.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.scope.Scope
import com.liam.gen.swift.notSupport
import com.liam.gen.scope.PsiResult
import org.jetbrains.kotlin.psi.*

open class WhenExpression : Handler<KtWhenExpression>() {

    override fun onGenCode(gen: CodeGen, v: KtWhenExpression, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): PsiResult {
        val statement = Statement()
        v.subjectExpression?.let {
            val r = gen.genExpr(it, scope, targetType, expectType, shouldReturn)
            val subject = r.statement.toString()
            val entriesSize = v.entries.size
            v.entries.forEachIndexed { i,entry ->
                when (i){
                    0 -> statement.append("if")
                    entriesSize - 1 -> statement.append("else")
                    else -> statement.append("else if")
                }
                val haveCondition = entry.conditions.isNotEmpty()
                if(haveCondition){
                    statement.append("(")
                }
                entry.conditions.forEachIndexed { index, ktWhenCondition ->
                    when (ktWhenCondition){
//                         is KtWhenConditionWithExpression ->
                        is KtWhenConditionInRange -> {
                            INExpression.genWhenCode(gen, subject, ktWhenCondition, statement, scope, targetType, expectType, shouldReturn)
                        }
                        is KtWhenConditionIsPattern -> {
                            if(ktWhenCondition.isNegated){
                                statement.append("!(")
                            }
                            statement.append("$subject is ")
                            statement.append(gen.genType(ktWhenCondition.typeReference!!, scope))
                            if(ktWhenCondition.isNegated){
                                statement.append(")")
                            }
                        }
                        else -> notSupport("Unrecognized when cond of $v")
                    }
                }
                if(haveCondition){
                    statement.append(")")
                }
                statement.append("{")
                if(entry.expression != null){
                    entry.expression?.let {
                        statement.openQuote()
                        statement.append(gen.genExpr(it, scope, targetType, expectType, shouldReturn))
                        statement.closeQuote()
                    }
                }else{
                    statement.nextLine()
                }
                statement.append("}")
            }
        }
        return PsiResult(statement,null,expectType)
    }

    companion object:WhenExpression()
}