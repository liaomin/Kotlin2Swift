package com.liam.gen.swift.expr

import com.liam.ast.writer.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.scope.Scope
import com.liam.gen.swift.notSupport
import org.jetbrains.kotlin.psi.*

open class WhenExpression : Handler<KtWhenExpression> {

    var index = 0

    fun genWhenTempName():String{
        return "___WHEN_TEMP_${++index}___"
    }


    override fun genCode(gen: CodeGen, v: KtWhenExpression, statement: Statement, scope: Scope, targetType:String?, expectType:String?, shouldReturn:Boolean):String?{
         v.subjectExpression?.let {
             val s = statement.newStatement()
             gen.genExpr(it,s, scope, targetType, expectType, shouldReturn)
             val subject = s.toString()
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
                             gen.genType( ktWhenCondition.typeReference!!,statement, scope)
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
                         gen.genExpr(it,statement, scope, targetType, expectType, shouldReturn)
                         statement.closeQuote()
                     }
                 }else{
                     statement.nextLine()
                 }
                 statement.append("}")
             }
         }
        return null
    }

    companion object:WhenExpression()
}