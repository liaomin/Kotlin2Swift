package com.liam.gen.swift.property

import com.liam.ast.writer.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.scope.Scope
import com.liam.gen.swift.TypeUtils
import org.jetbrains.kotlin.psi.KtProperty

open class Property: Handler<KtProperty> {

    override fun genCode(gen: CodeGen, property: KtProperty, statement: Statement, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): String? {
        property.modifierList?.also {
            gen.genModifiers(it,property,statement,scope)
        }
        if(property.isVar){
            statement.append("var ")
        }else{
            statement.append("let ")
        }
        var name = property.name!!
        statement.append("$name")
        var type = TypeUtils.getType(property.typeReference)

        val exprStatement = Statement()
        property.delegateExpressionOrInitializer?.let {
            val t = gen.genExpr(it,exprStatement,scope,null,type,true)
            if(type == null){
                type = t
            }
        }
        if(type != null){
            statement.append(":$type")
        }
        statement.append(" = ")
        statement.append(exprStatement)

        statement.nextLine()
        scope.setVariable(name,type)
        return type
    }

    companion object:Property()


}