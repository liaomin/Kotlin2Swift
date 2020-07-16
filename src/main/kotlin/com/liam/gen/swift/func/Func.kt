package com.liam.gen.swift.expr

import com.liam.ast.writer.Statement
import com.liam.gen.swift.*
import com.liam.gen.swift.scope.FuncInfo
import com.liam.gen.swift.scope.Scope
import org.jetbrains.kotlin.psi.*

open class Func : Handler<KtNamedFunction> {

    fun getFuncInfo(gen: CodeGen, func: KtNamedFunction, s: Statement, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): FuncInfo {
        val funcName = func.name!!
        val args = ArrayList<FuncInfo.Args>()
        func.valueParameters.forEach {
            val name =  it.name
            val type = it.typeReference?.let { TypeUtils.getType(it) }
            val default = it.defaultValue?.let {
                val statement = s.newStatement()
                gen.genExpr(it,statement ,scope, targetType, expectType, shouldReturn)
                statement.toString()
            } ?: null
            args.add(FuncInfo.Args(name!!,type!!,default))
        }
        val returnType = func.typeReference?.let { TypeUtils.getType(it) }
        return FuncInfo(funcName, args, returnType);
    }

    override fun genCode(gen: CodeGen, v: KtNamedFunction, statement: Statement, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): String? {
        if(v.receiverTypeReference != null){
            //TODO 扩展类
            if(scope.parent != null){
                notSupport()
            }
        }else{
           return genNormalFunc(gen,v, statement, scope, targetType, expectType, shouldReturn)
        }
        return null
    }

    fun genNormalFunc(gen: CodeGen, func: KtNamedFunction, s: Statement, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): String?{
        val statement = s.newStatement()
        val newScope = scope.newScope()
        func.modifierList?.let { gen.genModifiers(it,func,statement,scope) }
        val funcInfo = getFuncInfo(gen,func,statement,scope, targetType, expectType, shouldReturn)
        scope.addFunc(funcInfo)
        statement.nextLine()
        statement.append("func ${funcInfo.name}")
        if(func.hasTypeParameterListBeforeFunctionName()){
            statement.append("<")
            func.typeParameters.forEachIndexed { index, ktTypeParameter ->
                if(index > 0){
                    statement.append(", ")
                }
                gen.genType(ktTypeParameter,statement,newScope,false)
            }
            statement.append(">")
        }
        statement.append("(")
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
            gen.genExpr(it,statement, newScope,targetType, funcInfo.returnType, funcInfo.returnType != null)
            statement.closeQuote()
            statement.append("}")
        }
        s.append(statement)
        s.nextLine()
        return funcInfo.returnType
    }




//    open fun convertFuncBody(v: KtExpression) =
//            if (v is KtBlockExpression) Node.Decl.Func.Body.Block(convertBlock(v)).map(v)
//            else Node.Decl.Func.Body.Expr(convertExpr(v)).map(v)

    companion object:Func()


}