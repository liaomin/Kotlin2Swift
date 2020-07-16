package com.liam.gen.swift.expr

import com.liam.gen.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.scope.PsiResult
import com.liam.gen.swift.scope.Scope
import org.jetbrains.kotlin.KtNodeTypes
import org.jetbrains.kotlin.psi.KtConstantExpression

open class ConstantExpression : Handler<KtConstantExpression>() {

    companion object:ConstantExpression()

    override fun onGenCode(gen: CodeGen, v: KtConstantExpression, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): PsiResult {
        val statement = Statement()
        when (v.node.elementType) {
            KtNodeTypes.BOOLEAN_CONSTANT -> {
                statement.append(v.text)
                return PsiResult(statement,null,"Bool")
            }
            KtNodeTypes.CHARACTER_CONSTANT -> {
                val value = v.text
                if(expectType != null && expectType == "Character"){
                    statement.append("\"")
                    statement.append(value.subSequence(1,value.length-1))
                    statement.append("\"")
                    return PsiResult(statement,null,"Character")
                }
                statement.append(v.text)
                return PsiResult(statement,null,"String")
            }
            KtNodeTypes.INTEGER_CONSTANT -> {
                var v = v.text.toLowerCase()
                if (v.endsWith("ul")) {
                    statement.append(v.subSequence(0, v.length - 2))
                    return PsiResult(statement,null,"UInt64")
                } else if (v.endsWith("l")) {
                    statement.append(v.subSequence(0, v.length - 1))
                    return PsiResult(statement,null,"Int64")
                } else if (v.endsWith("u")) {
                    statement.append(v.subSequence(0, v.length - 1))
                    return PsiResult(statement,null,"UInt")
                } else {
                    statement.append(v)
                    return PsiResult(statement,null,"Int")
                }
            }
            KtNodeTypes.FLOAT_CONSTANT -> {
                var v = v.text
                if (v.endsWith("f")) {
                    statement.append(v.subSequence(0, v.length - 1))
                } else {
                    statement.append(v)
                }
                return PsiResult(statement,null,"Float")
            }
            KtNodeTypes.NULL -> {
                statement.append("nil")
                return PsiResult(statement,null,"Void")
            }
            else -> error("Unrecognized const type for $v")
        }
    }
}