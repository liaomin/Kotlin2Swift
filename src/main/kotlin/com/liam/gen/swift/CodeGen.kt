package com.liam.gen.swift

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.liam.ast.psi.Converter
import com.liam.ast.psi.Node
import com.liam.gen.Statement
import com.liam.gen.swift.expr.Class
import com.liam.gen.swift.expr.Expr
import com.liam.gen.swift.expr.Func
import com.liam.gen.swift.property.Property
import com.liam.gen.swift.scope.PsiResult
import com.liam.gen.swift.scope.Scope
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.children
import java.lang.RuntimeException

fun onError(message:String = ""):String{
    val msg = message
    Logger.error(msg)
    try {
        throw RuntimeException(msg)
    }catch (e:Exception){
        e.printStackTrace();
    }
    return msg
}

fun notSupport(message:String = ""):String{
    val msg = "not support $message"
    return onError(msg)
}


abstract class CodeGen(){

    abstract fun genCode():Statement


    open fun genModifiers(modifierList: KtModifierList, target: KtElement, scope: Scope,statement: Statement){
        modifierList.node?.children().orEmpty().forEach {
            val psi = it.psi
            when (psi){
                is KtAnnotationEntry -> {}
                is KtAnnotation -> {}
                is PsiWhiteSpace -> {}
                else -> when (psi.text) {
                    // We ignore some modifiers because we handle them elsewhere
                    "override" -> {
                    }
                }
            }
        }
    }

    open fun genClassOrObject(classOrObject: KtClassOrObject, scope: Scope, targetType:String?, expectType:String?, shouldReturn:Boolean):PsiResult{
        return when(classOrObject){
            is KtClass -> Class.genCode(this,classOrObject,scope,targetType,expectType,shouldReturn)
            else -> PsiResult.Null
        }
    }

    open fun genProperty(property: KtProperty, scope: Scope, targetType:String?, expectType:String?, shouldReturn:Boolean):PsiResult = Property.genCode(this,property, scope,targetType, expectType, shouldReturn)

    open fun genExpr(v: KtExpression, scope: Scope, targetType:String?, expectType:String?, shouldReturn:Boolean):PsiResult = Expr.genCode(this,v, scope,targetType,expectType,shouldReturn)

    open fun genNamedFunction(func: KtNamedFunction, scope: Scope, targetType:String?, expectType:String?, shouldReturn:Boolean):PsiResult = Func.genCode(this,func, scope,targetType, expectType, shouldReturn)

    open fun genType(v: KtTypeParameter, scope: Scope, containBasicType:Boolean = true):PsiResult {
        val statement = Statement()
        statement.append(v.name)
        var type:String? = null
        v.extendsBound?.let {
            statement.append(":")
            type = TypeUtils.getType(it)
            if(!containBasicType && TypeUtils.isBasicType(type)){
                notSupport("basic type can't be here")
            }
            statement.append(type)
        }
        return PsiResult(statement,null,type)
    }

    open fun genDeclaration(v:KtDeclaration, statement: Statement, scope: Scope, targetType:String?, expectType:String?, shouldReturn:Boolean):PsiResult{
        return when(v){
            is KtClassOrObject -> genClassOrObject(v,scope,targetType,expectType,shouldReturn)
            is KtProperty -> genProperty(v,scope,targetType, expectType, shouldReturn)
            is KtNamedFunction -> genNamedFunction(v, scope, targetType, expectType, shouldReturn)
            else -> {
                notSupport()
                PsiResult.Null
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

    open fun genType(v: KtTypeProjection, scope: Scope) = v.typeReference?.let { genType(it,scope, it.modifierList) }

    open fun genType(v: KtTypeReference,  scope: Scope, modifierList: KtModifierList? = null):String?{
        if(modifierList != null) notSupport()
        val type = TypeUtils.getType(v)
        return type
    }

}