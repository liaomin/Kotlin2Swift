package com.liam.gen.swift.structed

import com.liam.gen.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.scope.PsiResult
import com.liam.gen.scope.Scope
import org.jetbrains.kotlin.psi.*

open class Interface : Struct<KtClass>() {


    companion object:Interface()

    override fun onGenCode(gen: CodeGen, v: KtClass, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): PsiResult {
        if(v.isInterface()){
            val statement = Statement()
            val className = v.name!!
            statement.append("protocol $className")
            val newScope = scope.newClassScope(className)
            newScope.isInterface = true
            v.superTypeListEntries.forEachIndexed { index, it ->
                if(index == 0){
                    statement.append(" : ")
                }
                val type = gen.genType(it.typeReference!!,scope)
                type?.let { newScope.superClasses.add(type) }
                if(index > 0){
                    statement.append(", ")
                }
                statement.append(type)
            }
            statement.append(" {")
            statement.openQuote()
            v.declarations?.let {
                statement.append(genDeclarations(gen,newScope,it))
            }
            statement.closeQuote()
            statement.append("}")
            statement.nextLine()
            return PsiResult(statement,className,className)

        }
        return PsiResult.Null
    }
}