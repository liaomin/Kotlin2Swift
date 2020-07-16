package com.liam.gen.swift.expr

import com.liam.ast.writer.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.scope.Scope
import org.jetbrains.kotlin.psi.*

open class StringTemplateExpression : Handler<KtStringTemplateExpression> {

    override fun genCode(gen: CodeGen, v: KtStringTemplateExpression, statement: Statement, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): String? {
        val raw = v.text.startsWith("\"\"\"")
        if(raw){
            statement.append("\"\"\"")
        }else{
            statement.append("\"")
        }
        v.entries.forEach {
            when(it){
                is KtLiteralStringTemplateEntry ->
                    statement.append(it.text)
                is KtSimpleNameStringTemplateEntry ->
                    statement.append(it.expression?.text ?: error("No short tmpl text"))
                is KtBlockStringTemplateEntry ->
                    gen.genExpr(it.expression ?: error("No expr tmpl"),statement, scope,targetType, expectType, shouldReturn)
                is KtEscapeStringTemplateEntry ->
                    if (v.text.startsWith("\\u"))
                        statement.append(it.text.substring(2))
                    else
                        statement.append(it.unescapedValue.first().toString())
                else ->
                    error("Unrecognized string template type for $v")
            }
        }
        if(raw){
            statement.append("\"\"\"")
        }else{
            statement.append("\"")
        }
        return "String"
    }


    companion object:StringTemplateExpression()


}