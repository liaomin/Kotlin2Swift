package com.liam.gen.swift.property

import com.liam.gen.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.scope.Scope
import com.liam.gen.swift.TypeUtils
import com.liam.gen.swift.scope.PsiResult
import org.jetbrains.kotlin.psi.KtProperty

open class Property: Handler<KtProperty>() {

    override fun onGenCode(gen: CodeGen, v: KtProperty, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): PsiResult {
        val statement = Statement()
        val property = v
        property.modifierList?.also {
            gen.genModifiers(it,property,scope,statement)
        }
        if(property.isVar){
            statement.append("var ")
        }else{
            statement.append("let ")
        }
        var name = property.name!!
        statement.append("$name")
        var type = TypeUtils.getType(property.typeReference)

        var exprStatement:Statement? = null
        property.delegateExpressionOrInitializer?.let {
            val t = gen.genExpr(it,scope,null,type,true)
            exprStatement = t.statement
            if(type == null){
                type = t.returnType
            }
        }
        if(type != null){
            statement.append(":$type")
        }
        statement.append(" = ")
        statement.append(exprStatement)

        statement.nextLine()
        scope.setVariable(name,type)
        return PsiResult(statement,name,type)
    }

    companion object:Property()

}