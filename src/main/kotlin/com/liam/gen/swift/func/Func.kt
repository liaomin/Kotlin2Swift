package com.liam.gen.swift.func

import com.liam.gen.Statement
import com.liam.gen.swift.*
import com.liam.gen.per.FuncInfo
import com.liam.gen.per.Modifier
import com.liam.gen.scope.ClassScope
import com.liam.gen.scope.FileScope
import com.liam.gen.scope.PsiResult
import com.liam.gen.scope.Scope
import org.jetbrains.kotlin.psi.*

open class Func : Handler<KtNamedFunction>() {

    override fun perParserStep1(psiElement: KtNamedFunction, scope: Scope) {
        val funcInfo = getFuncInfo(CodeGen,psiElement,scope,null,null,false)
        scope.perCache[psiElement] = funcInfo
    }

    override fun perParserStep2(psiElement: KtNamedFunction, scope: Scope) {

    }

    fun getFuncInfo(gen: CodeGen, func: KtNamedFunction, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): FuncInfo {
        var funcName = func.name!!
        var callName = funcName
        if(scope is FileScope){
            callName = scope.getRootScope().getGlobalName(funcName,scope.packageName)
        }
        val args = ArrayList<FuncInfo.Args>()
        func.valueParameters.forEach {
            val name =  it.name
            val type = it.typeReference?.let { TypeUtils.getType(it,scope) }
            val default = it.defaultValue?.let {
                gen.genExpr(it ,scope, targetType, expectType, shouldReturn).statement.toString()
            } ?: null
            args.add(FuncInfo.Args(name!!,type!!,default))
        }
        val returnType = func.typeReference?.let { TypeUtils.getType(it,scope) }
        val funcInfo = FuncInfo(funcName, args, returnType)
        funcInfo.callName = callName
        return funcInfo
    }

    override fun onGenCode(gen: CodeGen, v: KtNamedFunction, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): PsiResult {
        val statement = Statement()
        if(v.receiverTypeReference != null){
            //TODO 扩展类
            if(scope.parent != null){
                notSupport()
            }
        }else{
            return PsiResult(statement,null,genNormalFunc(gen,v, statement, scope, targetType, expectType, shouldReturn))
        }
        return PsiResult(statement,null,null)
    }

    fun genNormalFunc(gen: CodeGen, func: KtNamedFunction, s: Statement, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): String?{
        val statement = s.newStatement()
        val newScope = scope.newScope()
        func.modifierList?.let { gen.genModifiers(it,func,scope,statement) }
        val funcInfo = getFuncInfo(gen,func,scope, targetType, expectType, shouldReturn)
        scope.addFunc(funcInfo)
        statement.nextLine()
        if(scope is ClassScope){
            if(scope.shouldAddOverride(funcInfo)){
                statement.append("override ")
                funcInfo.modifierList.add(Modifier.OVERRIDE)
            }
        }
        statement.append("func ${funcInfo.name}")
        if(func.hasTypeParameterListBeforeFunctionName()){
            statement.append("<")
            func.typeParameters.forEachIndexed { index, ktTypeParameter ->
                if(index > 0){
                    statement.append(", ")
                }
                statement.append(gen.genType(ktTypeParameter,newScope,false))
            }
            statement.append(">")
        }
        statement.append(" (")
        funcInfo.args.forEachIndexed { index, args ->
            if(index>0){
                statement.append(",")
            }
            statement.append("${args.name}: ${args.type}")
            newScope.setVariable(args.name,args.type)
            args.default?.let {
                statement.append(" = $it ")
            }
        }
        statement.append(")")
        funcInfo.returnType?.let { statement.append(" -> $it") }
        func.bodyExpression?.let {
            statement.append("{")
            statement.openQuote()
            statement.append(gen.genExpr(it, newScope,targetType, funcInfo.returnType, funcInfo.returnType != null))
            statement.closeQuote()
            statement.append("}")
        }
        s.append(statement)
        s.nextLine()
        return funcInfo.returnType
    }


    companion object:Func()


}