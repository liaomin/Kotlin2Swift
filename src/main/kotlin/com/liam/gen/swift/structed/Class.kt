package com.liam.gen.swift.expr

import com.liam.gen.Statement
import com.liam.gen.swift.*
import com.liam.gen.swift.scope.FuncInfo
import com.liam.gen.swift.scope.PsiResult
import com.liam.gen.swift.scope.Scope
import com.liam.gen.swift.structed.Struct
import org.jetbrains.kotlin.psi.*
import java.util.*
import kotlin.collections.ArrayList

open class Class : Struct<KtClass>() {

    fun <T:KtConstructor<T>> genConstructor(gen: CodeGen,constructor:KtConstructor<T>,scope: Scope,statement: Statement,className: String,onBefore:(s:Statement)->Unit,onAfter:(s:Statement)->Unit,isConvenience:()->Boolean):Statement{
        val args = ArrayList<FuncInfo.Args>()
        val tempStatement = statement.newStatement()
        if(isConvenience()){
            tempStatement.append("convenience ")
        }
        tempStatement.append("init(")
        constructor.valueParameters.forEachIndexed { index, ktParameter ->
            val name =  ktParameter.name
            val type = ktParameter.typeReference?.let { TypeUtils.getType(it) }
            val default = ktParameter.defaultValue?.let {
                gen.genExpr(it ,scope, null, null, false).statement.toString()
            } ?: null
            if(index > 0){
                tempStatement.append(", ")
            }
            tempStatement.append("$name: $type")
            default?.let {
                tempStatement.append(" = $it ")
            }
            if(constructor is KtPrimaryConstructor){
                val readOnly = ktParameter.hasValOrVar() && !ktParameter.isMutable
                if(readOnly){
                    statement.append("let $name: $type ")
                }else{
                    statement.append("var $name: $type ")
                }
                statement.nextLine()
            }

            args.add(FuncInfo.Args(name!!,type!!,default))
        }
        scope.addFunc(FuncInfo(className, args, className))
        tempStatement.append(") {")
        tempStatement.openQuote()
        onBefore(tempStatement)
        if(constructor is KtPrimaryConstructor) constructor.valueParameters.forEach {
            val name = it.name
            tempStatement.append("self.$name = $name")
            tempStatement.nextLine()
        }
        constructor.bodyBlockExpression?.let {
            tempStatement.append(gen.genExpr(it, scope,null, className, true))
        }
        onAfter(tempStatement)
        tempStatement.closeQuote()
        tempStatement.append("}")
        tempStatement.nextLine()
        return tempStatement
    }

    fun genPrimaryConstructor(gen: CodeGen, constructor:KtPrimaryConstructor, className: String, statement: Statement, scope: Scope,superCallExpression:KtSuperTypeCallEntry?){
        statement.append(genConstructor(gen,constructor,scope,statement,className,{},{
            val st = it
            superCallExpression?.let {
                val type = gen.genType(it.typeReference!!,scope)!!
                st.append("super.init(")
                CallExpression.genArguments(gen,it,scope,st,type,null,type,true)
                st.append(")")
            }
        }){
            false
        })

    }


    fun genSecondaryConstructor(gen: CodeGen,v:KtSecondaryConstructor,scope: Scope,statement: Statement,className: String){
        statement.append(genConstructor(gen,v,scope,statement,className,{},{
            val st = it
            v.getDelegationCall().let {
                var funcName = className
                if(it.isCallToThis){
                    st.append("self.init(")
                }else{
                    funcName = scope.getSuperClass(className)
                    if(funcName.equals(className)){
                        Logger.error("not found super class for $className")
                    }
                    st.append("super.init(")
                }
                val temp = st.newStatement()
                CallExpression.genArguments(gen,it,scope,temp,funcName,null,funcName,false)
                st.append(temp)
                st.append(")")
            }
        }){
            v.getDelegationCall().isCallToThis
        })
    }

    override fun onGenCode(gen: CodeGen, v: KtClass, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): PsiResult {
        val statement = Statement()
        if(v.isEnum() || v.isInterface()){
            notSupport()
        }
        val className = v.name!!
        statement.append("class $className")
        val newScope = scope.newClassScope(className)
        var superCallExpression:KtSuperTypeCallEntry? = null
        v.superTypeListEntries.forEachIndexed { index, it ->
            if(index == 0){
                statement.append(" : ")
            }
            if(it is KtSuperTypeCallEntry){
                superCallExpression = it
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
        val primaryConstructor = v.primaryConstructor
        primaryConstructor?.let { genPrimaryConstructor(gen,it,className,statement,scope,superCallExpression) }

        v.declarations?.let {
            val secondaryConstructors = LinkedList<KtSecondaryConstructor>()
            val initializers = LinkedList<KtClassInitializer>()
            val others = LinkedList<KtDeclaration>()
            it.forEach {
                if(it is KtSecondaryConstructor){
                    secondaryConstructors.add(it)
                }else if(it is KtClassInitializer){
                    initializers.add(it)
                }else{
                    others.add(it)
                }
            }

            secondaryConstructors.forEach {
                genSecondaryConstructor(gen,it, scope, statement, className)
            }

            statement.append(genDeclarations(gen,newScope,others))
        }
        statement.closeQuote()
        statement.append("}")
        statement.nextLine()
        return PsiResult(statement,className,className)
    }



    companion object:Class()

}