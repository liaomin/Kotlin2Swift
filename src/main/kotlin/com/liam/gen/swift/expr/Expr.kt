package com.liam.gen.swift.expr

import com.liam.gen.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.Handler
import com.liam.gen.swift.scope.Scope
import com.liam.gen.swift.property.Property
import com.liam.gen.swift.notSupport
import com.liam.gen.swift.scope.PsiResult
import org.jetbrains.kotlin.psi.*

open class Expr: Handler<KtExpression>() {

    override fun onGenCode(gen: CodeGen, v: KtExpression, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): PsiResult {
        return when(v){
            is KtConstantExpression ->  ConstantExpression.genCode(gen, v, scope,targetType,expectType,shouldReturn)
            is KtCallExpression ->  CallExpression.genCode(gen, v, scope,targetType,expectType,shouldReturn)
            is KtNameReferenceExpression ->  NameRefExpression.genCode(gen, v, scope,targetType,expectType, shouldReturn)
            is KtBlockExpression ->  BlockExpression.genCode(gen, v, scope,targetType, expectType, shouldReturn)
            is KtProperty ->  Property.genCode(gen,v, scope,targetType,expectType,shouldReturn)
            is KtReturnExpression ->  ReturnExpression.genCode(gen,v, scope,targetType,expectType,shouldReturn)
            is KtStringTemplateExpression ->  StringTemplateExpression.genCode(gen,v, scope,targetType,expectType,shouldReturn)
            is KtQualifiedExpression ->  QualifiedExpression.genCode(gen,v, scope,targetType,expectType,shouldReturn)
            is KtIfExpression ->  IFExpression.genCode(gen,v, scope,targetType,expectType,shouldReturn)
//            is KtTryExpression -> convertTry(v)
            is KtForExpression -> ForExpression.genCode(gen, v, scope, targetType, expectType, shouldReturn)
            is KtWhileExpressionBase -> WhileExpression.genCode(gen,v, scope, targetType, expectType, shouldReturn)
            is KtBinaryExpression ->  BinaryExpression.genCode(gen,v, scope,targetType,expectType,shouldReturn)
//            is KtUnaryExpression -> convertUnaryOp(v)
            is KtBinaryExpressionWithTypeRHS -> BinaryWithTypeRHS.genCode(gen,v, scope,targetType,expectType,shouldReturn)
//            is KtIsExpression -> convertTypeOp(v)
//            is KtCallableReferenceExpression -> convertDoubleColonRefCallable(v)
//            is KtClassLiteralExpression -> convertDoubleColonRefClass(v)
//            is KtParenthesizedExpression -> convertParen(v)
//            is KtFunctionLiteral -> convertBrace(v)
//            is KtLambdaExpression -> convertBrace(v)
            is KtThisExpression -> ThisExpression.genCode(gen, v, scope, targetType, expectType, shouldReturn)
//            is KtSuperExpression -> convertSuper(v)
            is KtWhenExpression -> WhenExpression.genCode(gen, v, scope, targetType, expectType, shouldReturn)
//            is KtObjectLiteralExpression -> convertObject(v)
//            is KtThrowExpression -> convertThrow(v)
//            is KtContinueExpression -> convertContinue(v)
//            is KtBreakExpression -> convertBreak(v)
//            is KtCollectionLiteralExpression -> convertCollLit(v)
//            is KtSimpleNameExpression -> StringTemplateExpression.genCode(gen,v,statement,scope)
//            is KtLabeledExpression -> convertLabeled(v)
//            is KtAnnotatedExpression -> convertAnnotated(v)
//            is KtCallExpression -> convertCall(v)
//            is KtArrayAccessExpression -> convertArrayAccess(v)
            is KtNamedFunction -> Func.genCode(gen, v, scope, targetType, expectType, shouldReturn)
//
//            is KtDestructuringDeclaration -> convertPropertyExpr(v)
//            // TODO: this is present in a recovery test where an interface decl is on rhs of a gt expr
//            is KtClass -> throw Converter.Unsupported("Class expressions not supported")
            else -> {
                notSupport("type for $v")
                PsiResult(Statement(),null,null)
            }
        }
    }

    companion object:Expr()
}