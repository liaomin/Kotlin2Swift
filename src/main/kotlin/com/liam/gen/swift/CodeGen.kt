package com.liam.gen.swift

import com.intellij.psi.PsiWhiteSpace
import com.liam.gen.Statement
import com.liam.gen.swift.expr.Class
import com.liam.gen.swift.expr.Expr
import com.liam.gen.swift.func.Func
import com.liam.gen.per.FileInfo
import com.liam.gen.swift.property.Property
import com.liam.gen.scope.PsiResult
import com.liam.gen.scope.RootScope
import com.liam.gen.scope.Scope
import com.liam.gen.swift.structed.File
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


open class CodeGen{

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
            type = TypeUtils.getType(it,scope)
            if(!containBasicType && TypeUtils.isBasicType(type)){
                notSupport("basic type can't be here")
            }
            statement.append(type)
        }
        return PsiResult(statement,null,type)
    }


    open fun genType(v: KtTypeProjection, scope: Scope) = v.typeReference?.let { genType(it,scope, it.modifierList) }

    open fun genType(v: KtTypeReference,  scope: Scope, modifierList: KtModifierList? = null):String?{
        if(modifierList != null) notSupport()
        val type = TypeUtils.getType(v,scope)
        return type
    }

    open fun genFiles(files:List<FileInfo>):Scope{
        val scope = RootScope()
        File.genFiles(this,files,scope)
        return scope
    }

    companion object : CodeGen(){

    }

}

