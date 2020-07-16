package com.liam.gen.swift.expr

import com.liam.ast.writer.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.scope.Scope
import org.jetbrains.kotlin.psi.*

open class IFExpression : Handler<KtIfExpression> {

    var index = 0

    fun genIfTempName():String{
        return "___IF_TEMP_${++index}___"
    }

    fun isTrinocularExpress(express:KtExpression):Boolean{
        if(express is KtIfExpression){
            return false
        }
        if(express is KtBlockExpression && express.statements.size > 1){
            return false
        }
        return true
    }

    fun genTrinocularExpress(gen: CodeGen, v: KtExpression, statement: Statement, scope: Scope, targetType:String?, expectType:String?, shouldReturn:Boolean):String?{
        var express = v
        if(v is KtBlockExpression){
            express = v.statements[0]
        }
        return gen.genExpr(express, statement, scope, targetType, expectType, shouldReturn)
    }

    fun handlerTrinocularOperator(gen: CodeGen, v: KtIfExpression, statement: Statement, scope: Scope, targetType:String?, expectType:String?, shouldReturn:Boolean):String?{
        val then = v.then ?: error("if no have condition")
        val `else` = v.`else`
        if(shouldReturn && `else` != null && isTrinocularExpress(then) && isTrinocularExpress(`else`)){
            //三目运算
            var newScope = scope.newScope()
            gen.genExpr( v.condition ?: error("if no have condition"),statement, newScope,targetType,expectType,shouldReturn)
            statement.append(" ? ")
            newScope = scope.newScope()
            val type = genTrinocularExpress(gen,then,statement, newScope,targetType,expectType,shouldReturn)
            statement.append(" : ")
            newScope = scope.newScope()
            val elseType = genTrinocularExpress(gen,`else`,statement, newScope,targetType,expectType,shouldReturn)
            return type ?: elseType
        }
        return null
    }

    override fun genCode(gen: CodeGen, v: KtIfExpression, statement: Statement, scope: Scope, targetType:String?, expectType:String?, shouldReturn:Boolean):String?{
        val trinocular = handlerTrinocularOperator(gen, v, statement, scope, targetType, expectType, shouldReturn)
        if(trinocular != null){
            return trinocular
        }
        if(shouldReturn){
            statement.append("{")
            statement.openQuote()
        }
        statement.append("if ")
        val conditionScope = scope.newScope()
        gen.genExpr( v.condition ?: error("if no have condition"),statement, conditionScope,targetType,expectType,shouldReturn)
        statement.append(" ")
        val then = v.then ?: error("if no have condition")
        val `else` = v.`else`
        statement.append("{")
        statement.openQuote()
        val thenScope = scope.newScope()
        val thenType = gen.genExpr( then ,statement, thenScope,targetType,expectType,shouldReturn)
        statement.closeQuote()
        statement.append("}")
        if(`else` != null){
            statement.append(" else {")
            statement.openQuote()
            val elseScope = scope.newScope()
            if(shouldReturn && `else` !is BlockExpression && `else` !is ReturnExpression){
                statement.append("return ")
            }
            gen.genExpr( `else`,statement, elseScope,targetType,expectType,shouldReturn)
            statement.closeQuote()
            statement.append("}")
        }else{
            statement.nextLine()
        }
        if(shouldReturn){
            statement.closeQuote()
            statement.append("}()")
        }
        return expectType?:thenType
    }

    companion object:IFExpression()
}