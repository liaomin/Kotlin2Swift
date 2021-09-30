package com.liam.gen.swift.expr

import com.liam.gen.Statement
import com.liam.gen.swift.*
import com.liam.gen.per.FuncInfo
import com.liam.gen.per.Modifier
import com.liam.gen.scope.ClassScope
import com.liam.gen.scope.PsiResult
import com.liam.gen.scope.Scope
import com.liam.gen.swift.structed.Interface
import com.liam.gen.swift.structed.Struct
import org.jetbrains.kotlin.psi.*
import java.util.*
import kotlin.collections.ArrayList

open class Class : Struct<KtClass>() {

    override fun perParserStep1(psiElement: KtClass, scope: Scope) {

    }

    override fun perParserStep2(psiElement: KtClass, scope: Scope) {

    }

    fun <T:KtConstructor<T>> genConstructor(gen: CodeGen,constructor:KtConstructor<T>,scope: Scope,statement: Statement,className: String,onBefore:(s:Statement)->Unit,onAfter:(s:Statement)->Unit,isConvenience:()->Boolean):Statement{
        val args = ArrayList<FuncInfo.Args>()
        val tempStatement = statement.newStatement()
        if(isConvenience()){
            tempStatement.append("convenience ")
        }
        val nodes = tempStatement.currentLine.nodes
        val index = nodes.size
        tempStatement.append("init(")
        constructor.valueParameters.forEachIndexed { index, ktParameter ->
            val name =  ktParameter.name
            val type = ktParameter.typeReference?.let { TypeUtils.getType(it,scope) }
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
        val funcInfo = FuncInfo(className, args, className)
        scope.addFunc(funcInfo)
        val funcInfo2 = FuncInfo("init", args, className)
        scope.getScope(className)?.let {
            it.addFunc(funcInfo2)
            if(it is ClassScope && it.shouldAddOverride(funcInfo2)){
                nodes.add(index,"override ")
            }
        }
        if(isConvenience()){
            funcInfo.modifierList.add(Modifier.CONVENIENCE)
            funcInfo2.modifierList.add(Modifier.CONVENIENCE)
        }
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
                    if(className.equals("InterfaceClass3")){
                        print("")
                    }
                    funcName = scope.getSuperClass(className)
                    if(funcName.equals(className)){
                        return@let
                    }else{
                        st.append("super.init(")
                    }
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
        if(v.isEnum()){
            notSupport()
        }
        if(v.isInterface()){
            return Interface.genCode(gen,v,scope,targetType,expectType,shouldReturn)
        }
        val statement = Statement()
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
        if(primaryConstructor != null){
            genPrimaryConstructor(gen,primaryConstructor,className,statement,scope,superCallExpression)
        }else if(superCallExpression != null){
            val args = ArrayList<FuncInfo.Args>()
            val classFunInfo = FuncInfo(className, args, className)
            scope.addFunc(classFunInfo)
            val initFuncInfo = FuncInfo("init", args, className)
            newScope.addFunc(initFuncInfo)
            if(newScope.shouldAddOverride(initFuncInfo)){
                statement.append("override ")
            }
            statement.append("init(){")
            statement.openQuote()
            val it = superCallExpression!!
            val type = gen.genType(it.typeReference!!,scope)!!
            statement.append("super.init(")
            CallExpression.genArguments(gen,it,scope,statement,type,null,type,true)
            statement.append(")")
            statement.closeQuote()
            statement.append("}")
            statement.nextLine()
        }

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