package com.liam.gen.swift

import com.intellij.psi.PsiElement
import com.liam.ast.writer.Statement
import com.liam.gen.swift.expr.Class
import com.liam.gen.swift.expr.Expr
import com.liam.gen.swift.expr.Func
import com.liam.gen.swift.property.Property
import com.liam.gen.swift.scope.Scope
import org.jetbrains.kotlin.psi.*
import java.lang.RuntimeException

fun notSupport(message:String = ""):String{
    val msg = "not support $message"
    Logger.error(msg)
    try {
        throw RuntimeException(msg)
    }catch (e:Exception){
        e.printStackTrace();
    }
    return msg
}

interface Handler<T:PsiElement>{
    fun genCode(gen:CodeGen, v: T, statement: Statement, scope: Scope, targetType:String?, expectType:String?, shouldReturn:Boolean):String?
}


abstract class CodeGen(val statement: Statement = Statement()){

    abstract fun genCode()


    open fun genModifiers(modifierList: KtModifierList,target: KtElement,statement: Statement,scope: Scope){
        //TODO
    }

    open fun genClassOrObject(classOrObject: KtClassOrObject, statement: Statement, scope: Scope, targetType:String?, expectType:String?, shouldReturn:Boolean){
        when(classOrObject){
            is KtClass -> Class.genCode(this,classOrObject,statement,scope,targetType,expectType,shouldReturn)
        }
    }

    open fun genProperty(property: KtProperty, statement: Statement, scope: Scope, targetType:String?, expectType:String?, shouldReturn:Boolean){
        Property.genCode(this,property, statement, scope,targetType, expectType, shouldReturn)
    }

    open fun genExpr(v: KtExpression, statement: Statement, scope: Scope, targetType:String?, expectType:String?, shouldReturn:Boolean):String?{
        return Expr.genCode(this,v,statement, scope,targetType,expectType,shouldReturn)
    }
//
//    open fun genExpr(v: KtExpression,scope: Scope):Statement{
//        val statement = Statement()
//        Expr.genCode(this,v,statement, scope)
//        return statement
//    }

    open fun genNamedFunction(func: KtNamedFunction, statement: Statement, scope: Scope, targetType:String?, expectType:String?, shouldReturn:Boolean){
        Func.genCode(this,func,statement, scope,targetType, expectType, shouldReturn)
    }


    open fun genType(v: KtTypeParameter, statement: Statement, scope: Scope, containBasicType:Boolean = true) {
        statement.append(v.name)
        v.extendsBound?.let {
            statement.append(":")
            val type = TypeUtils.getType(it)
            if(!containBasicType && TypeUtils.isBasicType(type)){
                notSupport("basic type can't be here")
            }
            statement.append(type)
        }
    }

    open fun genDeclaration(v:KtDeclaration, statement: Statement, scope: Scope, targetType:String?, expectType:String?, shouldReturn:Boolean){
        when(v){
            is KtClassOrObject -> genClassOrObject(v,statement,scope,targetType,expectType,shouldReturn)
            is KtProperty -> genProperty(v,statement,scope,targetType, expectType, shouldReturn)
            is KtNamedFunction -> genNamedFunction(v,statement, scope, targetType, expectType, shouldReturn)
            else ->{
                notSupport()
            }
        }
//        is KtEnumEntry -> convertEnumEntry(v)
//        is KtClassOrObject -> convertStructured(v)
//        is KtAnonymousInitializer -> convertInit(v)
//        is KtNamedFunction -> convertFunc(v)
//        is KtDestructuringDeclaration -> convertProperty(v)
//        is KtProperty -> convertProperty(v)
//        is KtTypeAlias -> convertTypeAlias(v)
//        is KtSecondaryConstructor -> convertConstructor(v)
//        else -> error("Unrecognized declaration type for $v")
    }

    open fun genType(v: KtTypeProjection,statement: Statement,scope: Scope) = v.typeReference?.let { genType(it,statement,scope, it.modifierList) }

    open fun genType(v: KtTypeReference, statement: Statement, scope: Scope, modifierList: KtModifierList? = null):String?{
        if(modifierList != null) notSupport()
        val type = TypeUtils.getType(v)
        statement.append(type)
        return type
    }


}