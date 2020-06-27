package com.liam.ast.writer

import java.lang.StringBuilder

/**
 * @author liaomin
 * @date 6/19/20 2:51 下午
 * @version 1.0
 */
open class Statement {

    val lines = ArrayList<Line>()

    val beforeNewLine = ArrayList<Line>()

    var currentLine = Line()

    init {
        lines.add(currentLine)
    }

    var packageName:String? = null

    private var tabCount:Int = 0

    fun nextLine(){
        beforeNewLine.forEach {
            setPreFix(it)
            lines.add(it)
        }
        beforeNewLine.clear()
        currentLine = Line()
        lines.add(currentLine)
        setPreFix(currentLine)
    }

    fun next2Line(){
        beforeNewLine.forEach {
            setPreFix(it)
            lines.add(it)
        }
        beforeNewLine.clear()
        currentLine = Line()
        lines.add(currentLine)
        setPreFix(currentLine)
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

    fun append(str:CharSequence?):Statement{
        if(str != null){
            currentLine.append(str)
        }
        return this
    }

    fun appendBeforeNewLine(str:CharSequence):Statement{
        beforeNewLine.add(Line.SingleLine(str))
        return this
    }

    fun appendBeforeHead(str:CharSequence):Statement{
        currentLine.appendBeforeHead(str)
        return this
    }

    fun getLineSize():Int{
        return lines.size
    }

    fun insertLine(str: CharSequence,lineNum:Int){
        if(lineNum<0 || lineNum>(lines.size-1)){
            error("wrong $lineNum")
        }
        val l = Line.SingleLine(str)
        setPreFix(l)
        lines.add(lineNum, l)
    }

    fun insertBeforeLine(str: CharSequence){
        insertLine(str,lines.size - 1)
    }

    fun openQuote(){
        tabCount++
    }

    fun closeQuote(){
        tabCount--
    }

    override fun toString(): String {
        val builder = StringBuilder()
        lines.forEach {
            builder.append("${it.toString()} \n")
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
        class SingleLine(val str: CharSequence):Line(){
            override fun toString(): String {
                if(preFix != null){
                    return "${preFix}$str"
                }
                return str.toString()
            }
        }
    }
}
