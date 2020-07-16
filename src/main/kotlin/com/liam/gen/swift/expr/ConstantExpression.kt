package com.liam.gen.swift.expr

import com.liam.ast.writer.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.scope.Scope
import org.jetbrains.kotlin.KtNodeTypes
import org.jetbrains.kotlin.psi.KtConstantExpression

open class ConstantExpression : Handler<KtConstantExpression> {

    override fun genCode(gen: CodeGen, v: KtConstantExpression, statement: Statement, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): String? {
        when (v.node.elementType) {
            KtNodeTypes.BOOLEAN_CONSTANT -> {
                statement.append(v.text)
                return "Bool"
            }
            KtNodeTypes.CHARACTER_CONSTANT -> {
                val value = v.text
                if(expectType != null && expectType == "Character"){
                    statement.append("\"")
                    statement.append(value.subSequence(1,value.length-1))
                    statement.append("\"")
                    return "Character"
                }
                statement.append(v.text)
                return "String"
            }
            KtNodeTypes.INTEGER_CONSTANT -> {
                var v = v.text.toLowerCase()
                if (v.endsWith("ul")) {
                    statement.append(v.subSequence(0, v.length - 2))
                    return "UInt64"
                } else if (v.endsWith("l")) {
                    statement.append(v.subSequence(0, v.length - 1))
                    return "Int64"
                } else if (v.endsWith("u")) {
                    statement.append(v.subSequence(0, v.length - 1))
                    return "UInt"
                } else {
                    statement.append(v)
                    return "Int"
                }
            }
            KtNodeTypes.FLOAT_CONSTANT -> {
                var v = v.text
                if (v.endsWith("f")) {
                    statement.append(v.subSequence(0, v.length - 1))
                } else {
                    statement.append(v)
                }
                return "Float"
            }
            KtNodeTypes.NULL -> {
                statement.append("nil")
                return null
            }
            else -> error("Unrecognized const type for $v")
        }
    }

    companion object:ConstantExpression()
}