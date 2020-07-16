package com.liam.gen.swift.expr

import com.liam.ast.writer.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.scope.Scope
import com.liam.gen.swift.notSupport
import org.jetbrains.kotlin.psi.*

open class INExpression {

    fun genWhenCode(gen: CodeGen, subject :String, v: KtWhenConditionInRange, statement: Statement, scope: Scope, targetType:String?, expectType:String?, shouldReturn:Boolean):String?{
        notSupport(" type in ")
        return null
        v.rangeExpression?.let {
            statement.append("$subject ")
            val range = it
            if(v.isNegated){
                statement.append("!")
            }
            statement.append("in ")
        }
    }

    //for
    fun genForCode(gen: CodeGen, v: KtExpression, statement: Statement, scope: Scope, targetType:String?, expectType:String?, shouldReturn:Boolean):String?{

        return null
    }
    companion object:INExpression()
}