package com.liam.ast.writer.swift

import com.liam.ast.psi.Node
import com.liam.ast.writer.BaseLanguageWriter
import com.liam.ast.writer.LanguageWriter
import com.liam.ast.writer.Statement
import com.liam.ast.writer.SwiftHandler

/**
 * @author liaomin
 * @date 6/23/20 10:05 上午
 * @version 1.0
 */
open class ModifierKeywordHandler: SwiftHandler<Node.Modifier.Lit>(){

    override fun getHandleClass(): Class<Node.Modifier.Lit> = Node.Modifier.Lit::class.java

    override fun onHandle(writer: LanguageWriter, node: Node.Modifier.Lit, statement: Statement) {
        when(node.keyword){
            Node.Modifier.Keyword.ABSTRACT -> {}
            Node.Modifier.Keyword.FINAL -> {}
            Node.Modifier.Keyword.OPEN -> {}
            Node.Modifier.Keyword.ANNOTATION -> {}
            Node.Modifier.Keyword.SEALED -> {}
            Node.Modifier.Keyword.DATA -> {}
            Node.Modifier.Keyword.OVERRIDE -> {}
            Node.Modifier.Keyword.LATEINIT -> {}
            Node.Modifier.Keyword.INNER -> {}
            Node.Modifier.Keyword.PRIVATE -> { statement.append("private ") }
            Node.Modifier.Keyword.PROTECTED -> {}
            Node.Modifier.Keyword.PUBLIC -> {}
            Node.Modifier.Keyword.INTERNAL -> {}
            Node.Modifier.Keyword.IN -> {}
            Node.Modifier.Keyword.OUT -> {}
            Node.Modifier.Keyword.NOINLINE -> {}
            Node.Modifier.Keyword.CROSSINLINE -> {}
            Node.Modifier.Keyword.VARARG -> {}
            Node.Modifier.Keyword.REIFIED -> {}
            Node.Modifier.Keyword.TAILREC -> {}
            Node.Modifier.Keyword.OPERATOR -> {}
            Node.Modifier.Keyword.INFIX -> {}
            Node.Modifier.Keyword.INLINE -> {}
            Node.Modifier.Keyword.EXTERNAL -> {}
            Node.Modifier.Keyword.SUSPEND -> {}
            Node.Modifier.Keyword.CONST -> {}
            Node.Modifier.Keyword.ACTUAL -> {}
            Node.Modifier.Keyword.EXPECT -> {}
        }
    }

}
