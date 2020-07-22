package com.liam.gen.swift.structed

import com.intellij.psi.PsiElement
import com.liam.gen.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.scope.FuncInfo
import com.liam.gen.swift.scope.PsiResult
import com.liam.gen.swift.scope.Scope
import org.jetbrains.kotlin.psi.*
import java.util.*
import kotlin.collections.HashMap

abstract class Struct<T : PsiElement> : Handler<T>() {


    fun genDeclarations(gen: CodeGen,scope: Scope,declarations:List<KtDeclaration>): Statement {
        val statement = Statement()
        //预先解析熟悉和方法
        declarations.forEach {
            when (it){
                is KtNamedFunction -> {
                    gen.genNamedFunction(it,scope,null,null,false)
                }
                is KtProperty -> {
                    gen.genProperty(it,scope,null,null,false)
                }
            }
        }

        declarations.forEach {
            when (it){
                is KtClassOrObject -> {
                    statement.append(gen.genClassOrObject(it,scope,null,null,false))
                }

                is KtNamedFunction -> {
                    statement.append(gen.genNamedFunction(it,scope,null,null,false))
                }

                is KtProperty -> {
                    statement.append(gen.genProperty(it,scope,null,null,false))
                }
//                is KtDestructuringDeclaration -> convertProperty(v)
//                is KtAnonymousInitializer ->
//                is KtTypeAlias -> convertTypeAlias(v)
//                is KtSecondaryConstructor -> convertConstructor(v)
                else -> error("Unrecognized declaration type for $it")
            }
        }
        return statement
    }

}