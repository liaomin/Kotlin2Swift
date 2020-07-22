package com.liam.gen.swift.expr

import com.liam.gen.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.scope.PsiResult
import com.liam.gen.swift.scope.Scope
import org.jetbrains.kotlin.psi.*

open class IFExpression : Handler<KtIfExpression>() {


    fun isTrinocularExpress(express:KtExpression):Boolean{
        if(express is KtIfExpression){
            return false
        }
        if(express is KtBlockExpression && express.statements.size > 1){
            return false
        }
        return true
    }

    fun genTrinocularExpress(gen: CodeGen, v: KtExpression, statement: Statement, scope: Scope, targetType:String?, expectType:String?, shouldReturn:Boolean):PsiResult{
        var express = v
        if(v is KtBlockExpression){
            express = v.statements[0]
        }
        return gen.genExpr(express, scope, targetType, expectType, shouldReturn)
    }

    fun handlerTrinocularOperator(gen: CodeGen, v: KtIfExpression, scope: Scope, targetType:String?, expectType:String?, shouldReturn:Boolean):PsiResult?{
        val then = v.then ?: error("if no have condition")
        val `else` = v.`else`
        val statement = Statement()
        if(shouldReturn && `else` != null && isTrinocularExpress(then) && isTrinocularExpress(`else`)){
            //三目运算
            var newScope = scope.newScope()
            statement.append(gen.genExpr( v.condition ?: error("if no have condition"), newScope,targetType,expectType,shouldReturn))
            statement.append(" ? ")
            newScope = scope.newScope()
            var r = genTrinocularExpress(gen,then,statement, newScope,targetType,expectType,shouldReturn)
            statement.append(r)
            var type = r.returnType
            statement.append(" : ")
            newScope = scope.newScope()
            r = genTrinocularExpress(gen,`else`,statement, newScope,targetType,expectType,shouldReturn)
            statement.append(r)
            return PsiResult(statement,null,type?:r.returnType)
        }
        return null
    }

    override fun onGenCode(gen: CodeGen, v: KtIfExpression, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): PsiResult {
        val statement = Statement()
        val trinocular = handlerTrinocularOperator(gen, v, scope, targetType, expectType, shouldReturn)
        if(trinocular != null){
            return trinocular
        }
        if(shouldReturn){
            statement.append("{")
            statement.openQuote()
        }
        statement.append("if ")
        val conditionScope = scope.newScope()
        var r = gen.genExpr( v.condition ?: error("if no have condition"), conditionScope,targetType,expectType,shouldReturn)
        statement.append(r)
        statement.append(" ")
        val then = v.then ?: error("if no have condition")
        val `else` = v.`else`
        statement.append("{")
        statement.openQuote()
        val thenScope = scope.newScope()
        r = gen.genExpr( then , thenScope,targetType,expectType,shouldReturn)
        statement.append(r)
        val thenType = r.returnType
        statement.closeQuote()
        statement.append("}")
        if(`else` != null){
            statement.append(" else {")
            statement.openQuote()
            val elseScope = scope.newScope()
            if(shouldReturn && `else` !is BlockExpression && `else` !is ReturnExpression){
                statement.append("return ")
            }
            r = gen.genExpr( `else`, elseScope,targetType,expectType,shouldReturn)
            statement.append(r)
            statement.closeQuote()
            statement.append("}")
        }
        statement.nextLine()
        if(shouldReturn){
            statement.closeQuote()
            statement.append("}()")
        }
        return PsiResult(statement,null,expectType?:thenType)
    }

    companion object:IFExpression()
}