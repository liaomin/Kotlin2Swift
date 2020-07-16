package com.liam.gen.swift.expr

import com.liam.gen.Statement
import com.liam.gen.swift.*
import com.liam.gen.swift.scope.FuncInfo
import com.liam.gen.swift.scope.PsiResult
import com.liam.gen.swift.scope.Scope
import org.jetbrains.kotlin.psi.*

open class Class : Handler<KtClass>() {


    fun genConstructor(gen: CodeGen, constructor:KtPrimaryConstructor, className: String, statement: Statement, scope: Scope){
        val args = ArrayList<FuncInfo.Args>()
        constructor.valueParameters.forEach {
            val name =  it.name
            val type = it.typeReference?.let { TypeUtils.getType(it) }
            args.add(FuncInfo.Args(name!!,type!!))
        }
        scope.addFunc(FuncInfo(className, args))
        statement.nextLine()
        statement.append("init(")
    }

    fun genConstructor(gen: CodeGen, constructor:KtSecondaryConstructor, className: String, statement: Statement, scope: Scope){

    }

    override fun onGenCode(gen: CodeGen, v: KtClass, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): PsiResult {
        val statement = Statement()
        if(v.isEnum() || v.isInterface()){
            notSupport()
        }
        val className = v.name!!
        statement.append("class $className")
        val newScope = scope.newClassScope()
        v.primaryConstructor?.let { genConstructor(gen,it,className,statement,scope) }
        v.declarations?.let {
            statement.append(" {")
            statement.openQuote()
            it.forEach {
                gen.genDeclaration(it,statement,newScope,targetType,expectType, shouldReturn)
            }
            statement.closeQuote()
            statement.append("}")
            print("")
        }
        statement.nextLine()
        return PsiResult(statement,className,className)
    }



    companion object:Class()

}