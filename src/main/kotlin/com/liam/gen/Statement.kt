package com.liam.gen

import com.liam.gen.swift.scope.PsiResult
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author liaomin
 * @date 6/19/20 2:51 下午
 * @version 1.0
 */
open class Statement {

    val lines = ArrayList<Line>()

    var currentLine = Line()

    init {
        lines.add(currentLine)
    }

    private var tabCount:Int = 0

    fun reset(){
        lines.clear()
        val d  = Date().year
        tabCount = 0
        currentLine = Line()
        lines.add(currentLine)
    }

    fun newStatement(): Statement = Statement()

    fun nextLine(){
        currentLine = Line()
        lines.add(currentLine)
        setPreFix(currentLine)
    }


    fun setPreFix(line: Line){
        if(tabCount > 0 ){
            val b = StringBuilder()
            for (i in 0 until tabCount){
                b.append("\t")
            }
            line.preFix = b.toString()
        }
    }

    fun append(str:CharSequence?): Statement {
        if(str != null){
            currentLine.append(str)
        }
        return this
    }

    fun append(statement: Statement?): Statement {
        statement?.lines?.forEachIndexed { index, line ->
            append(line.toString())
            if(index != statement.lines.size - 1){
                nextLine()
            }
        }
        return this
    }

    fun append(result: PsiResult): Statement {
        val statement = result.statement
        if(statement.isNotEmpty()){
            append(statement)
        }
        return this
    }


    fun appendBeforeHead(str:CharSequence): Statement {
        currentLine.appendBeforeHead(str)
        return this
    }

    fun getLineSize():Int{
        return lines.size
    }

    fun isNotEmpty():Boolean{
        return lines.size > 1 || currentLine.toString().isNotEmpty()
    }

    fun openQuote(){
        tabCount++
        nextLine()
    }

    fun closeQuote(){
        tabCount--
        nextLine()
    }

    override fun toString(): String {
        val builder = StringBuilder()
        lines.forEachIndexed { index, line ->
            builder.append(line.toString())
            if(index != lines.size - 1){
                builder.append("\n")
            }
        }
        return builder.toString()
    }

    open class Line{
        val nodes = ArrayList<CharSequence>()
        var preFix:String? = null

        fun appendBeforeHead(str:CharSequence){
            nodes.add(0,str)
        }

        fun append(str: CharSequence){
            nodes.add(str)
        }

        override fun toString(): String {
            val builder = StringBuilder()
            if(preFix != null) builder.append(preFix)
            nodes.forEach {
                builder.append(it)
            }
            return builder.toString()
        }

    }
}
