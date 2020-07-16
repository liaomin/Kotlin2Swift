package com.liam.gen.swift

import com.intellij.psi.PsiElement
import com.liam.gen.Statement
import com.liam.gen.swift.scope.PsiResult
import com.liam.gen.swift.scope.Scope

abstract class Handler <T:PsiElement>{

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
