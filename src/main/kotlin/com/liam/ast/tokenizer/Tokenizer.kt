package com.liam.ast.tokenizer

import java.io.File
import java.io.FileReader
import java.lang.RuntimeException
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * @author liaomin
 * @date 6/3/20 5:13 下午
 * @version 1.0
 */
abstract class Tokenizer {

    protected val tokens:ArrayList<Token> = ArrayList()

    private val delimCache:HashMap<Char,Char>
    private val operatorCache:HashMap<Char,Char>

    init {
        val s = getDelim()
        delimCache = HashMap<Char,Char>(s.length)
        val operatorString = getOperator()
        operatorCache = HashMap<Char,Char>(operatorString.length)
        val charArray = s.toCharArray()
        charArray.forEach {
            delimCache.put(it,it)
        }
        operatorString.toCharArray().forEach {
            operatorCache.put(it,it)
        }
    }

    fun isDelim(c:Char):Boolean{
        return delimCache.containsKey(c)
    }

    fun isOperator(c:Char):Boolean{
        return operatorCache.containsKey(c)
    }

    fun parser(sourceFile: File):Tokenizer{
        var fr:FileReader? = null
        try {
            val reader = FileReader(sourceFile)
            val code =  reader.readText()
            fr = reader
            return parser(code,sourceFile)
        }finally {
            fr?.close()
        }
    }

    abstract fun parser(code: String,sourceFile: File?= null):Tokenizer

    open fun onParserChar(char: Char,index:Int,code: String,sourceFile: File?):Int{
        return index
    }

    open fun onStartParserLine(char: Char,index:Int,code: String,sourceFile: File?= null):Int{
        return index
    }

    abstract fun getDelim():String

    abstract fun getOperator():String

    abstract fun getFileExtensionName():String
}

abstract class DefaultTokenizer : Tokenizer() {

    var lineNumber = 1

    var charNumber = 0

    val value = StringBuilder()

    fun addToken( sourceFile: File? ){
        if(value.isNotEmpty()){
            val v = value.toString()
            tokens.add(Token(v,lineNumber,charNumber - v.length ,false,sourceFile))
            value.clear()
        }
    }

    override fun parser(code:String, sourceFile: File?):Tokenizer {
        var index = 0
        while (index < code.length){
            var char = code.get(index)
            if(index == 0){
                var i = onStartParserLine(char,index,code,sourceFile)
                if(i != index){
                    index = i
                    continue
                }
            }
            if(isOperator(char)){
                addToken(sourceFile)
                tokens.add(Token(char.toString(),lineNumber,charNumber - 1 ,false,sourceFile))
                charNumber++
                index++
                continue
            }
            if(isDelim(char)){
                addToken(sourceFile)
                if(char == '\n'){
                    lineNumber++
                    charNumber = 0
                    index++
                    if(index < code.length){
                        char = code.get(index)
                        index = onStartParserLine(char,index,code)
                    }
                    continue
                }else{
                    charNumber++
                }
                index++
                continue
            }
            charNumber++
            var offset = onParserChar(char,index,code,sourceFile)
            if(offset != index){
                index = offset
            }else{
                value.append(char)
            }
            index++
        }
        addToken(sourceFile)
        return this
    }

    override fun toString(): String {
        val builder = StringBuilder()
        tokens.forEach {
            builder.append("$it \n")
        }
        return builder.toString()
    }

}
