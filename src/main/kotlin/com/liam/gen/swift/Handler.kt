package com.liam.gen.swift

import com.intellij.psi.PsiElement
import com.liam.gen.scope.FileScope
import com.liam.gen.scope.PsiResult
import com.liam.gen.scope.Scope
import com.liam.gen.swift.func.Func
import com.liam.gen.swift.property.Property
import com.liam.gen.swift.expr.Class
import org.jetbrains.kotlin.psi.*

abstract class Handler <T:PsiElement>{

    open fun perParserStep1(psiElement:T,scope: Scope){

    }

    open fun perParserStep2(psiElement:T,scope: Scope){

    }

    fun perParserDeclarations(declarations: List<KtDeclaration>, scope: Scope, step:Int){
        declarations.forEach {
            when (it){
                is KtNamedFunction -> {
                    if(step == 1){
                        Func.perParserStep1(it,scope)
                    }else{
                        Func.perParserStep2(it,scope)
                    }
                }
                is KtProperty -> {
                    if(step == 1){
                        Property.perParserStep1(it,scope)
                    }else{
                        Property.perParserStep2(it,scope)
                    }
                }
                is KtClassOrObject -> {
//                    var name = it.name!!
//                    val classScope = scope.newClassScope(name)
//                    if(scope is FileScope){
//                        name = rootScope.getGlobalName(name,scope.packageName)
//                        classScope.callName = name
//                    }
//                    scope.typeScope[name] = classScope
                    if(it is KtClass){
                        if(step == 1){
                            Class.perParserStep1(it,scope)
                        }else{
                            Class.perParserStep2(it,scope)
                        }
                    }
                }
            }
        }
    }

    fun genCode(gen: CodeGen, v: T, scope: Scope, targetType:String?, expectType:String?, shouldReturn:Boolean):PsiResult{
        var cache = scope.getCachedResult(v)
        if(cache == null){
            cache = onGenCode(gen, v, scope, targetType, expectType, shouldReturn)
            scope.setCachedResult(v,cache)
        }
        return cache
    }

    abstract fun onGenCode(gen: CodeGen, v: T, scope: Scope, targetType:String?, expectType:String?, shouldReturn:Boolean):PsiResult

}
