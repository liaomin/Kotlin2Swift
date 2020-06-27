package com.liam.ast.tokenizer

import java.io.File
import java.lang.RuntimeException

/**
 * @author liaomin
 * @date 6/4/20 4:31 下午
 * @version 1.0
 */

class KotlinTokenizer : DefaultTokenizer(){

    override fun getFileExtensionName(): String {
        return ".kt"
    }

    override fun getDelim(): String {
        val c:Char = '\u000C'
        return " \t\n\r${c}"
    }

    override fun getOperator(): String {
        return ":,()[]{}+-*/%=.;"
    }

    override fun onParserChar(char: Char, index: Int, code: String, sourceFile: File?): Int {
        if(char === '"'){
            var i = index
            if(i < code.length - 2 && code.get(i+1) === '"' && code.get(i+2) === '"'){
                if(value.isNotEmpty())
                    throw RuntimeException("parse error")
                value.append(char)
                var i = index + 1
                val startLinNumber = lineNumber
                var startCharNumber = charNumber
                charNumber++
                while (i < code.length){
                    val c = code.get(i)
                    if(c === '\n'){
                        lineNumber++
                        charNumber = 0
                    }
                    if(c === char && i > index+2 && code.get(i-1) === '"' && code.get(i-2) === '"'){
                        value.append(c)
                        val v = value.toString()
                        tokens.add(Token(v,startLinNumber,startCharNumber,false,sourceFile))
                        value.clear()
                        return i
                    }else{
                        i++
                        value.append(c)
                        charNumber++
                    }
                }
            }
        }
        if(char === '\'' || char === '\"'){
            if(value.isNotEmpty())
                throw RuntimeException("parse error")
            value.append(char)
            var i = index + 1
            charNumber++
            while (i < code.length){
                val c = code.get(i)
                if(c === char){
                    value.append(c)
                    tokens.add(Token(value.toString(),lineNumber,charNumber,false,sourceFile))
                    value.clear()
                    return i
                }else{
                    i++
                    value.append(c)
                    charNumber++
                }
            }
        }
        return super.onParserChar(char, index, code, sourceFile)
    }

    override fun onStartParserLine(char: Char, index: Int, code: String,sourceFile: File?): Int {
        if(value.isNotEmpty())
            throw RuntimeException("parse error")
        var startChar = char
        var startIndex = index
        while (startChar === ' '){
            startIndex ++
            charNumber ++
            if(startIndex < code.length){
                startChar = code.get(startIndex)
            }else{
                return index
            }
        }
        val annotate = isStartAnnotate(startChar,startIndex,code)
        if(annotate != null){
            return offsetToEndAnnotate(annotate,startIndex,code,sourceFile)
        }
        if(startChar === 'i'
            && code.get(startIndex+1) === 'm'
            && code.get(startIndex+2) === 'p'
            && code.get(startIndex+3) === 'o'
            && code.get(startIndex+4) === 'r'
            && code.get(startIndex+5) === 't'
            && code.get(startIndex+6) === ' '
        ){
            tokens.add(Token("import",lineNumber,startIndex,false,sourceFile))
            startIndex += 7
            while (startIndex < code.length){
                val c = code.get(startIndex)
                if(c == '\n'){
                    tokens.add(Token(value.toString().trim(),lineNumber,charNumber,false,sourceFile))
                    value.clear()
                    return startIndex
                }
                startIndex++
                value.append(c)
            }
        }
        return index
    }

    private fun isStartAnnotate(char: Char, index: Int, code: String):String?{
        val length = code.length
        if(char === '/'){
            if(index < length - 1 && code.get(index+1) === '/'){
                return "//"
            }
            if(index < length - 2 && code.get(index+1) === '*' && code.get(index+1) === '*'){
                return "/**"
            }
        }
        return null
    }

    private fun offsetToEndAnnotate(annotate: String, index: Int, code: String,sourceFile: File?):Int{
        val length = code.length
        var startIndex = index
        if(annotate == "//"){
            while (startIndex < length){
                val c = code.get(startIndex)
                if(c === '\n'){
                    tokens.add(Token(value.toString().substring(2),lineNumber,charNumber,true,sourceFile))
                    value.clear()
                    return startIndex
                }else{
                    value.append(c)
                }
                startIndex++
            }
        }
        if(annotate == "/**"){
            val startLineNumber  = lineNumber
            while (startIndex < length){
                val c = code.get(startIndex)
                if(c === '\n'){
                    lineNumber++
                    charNumber = 0
                }
                if(c === '/' && code.get(startIndex-1) === '*'){
                    value.append(c)
                    val v = value.toString()
                    tokens.add(Token(v.substring(3,v.length-2),startLineNumber,charNumber,true,sourceFile))
                    value.clear()
                    return ++startIndex
                }else{
                    value.append(c)
                }
                startIndex++
            }
        }

        return length
    }

}
