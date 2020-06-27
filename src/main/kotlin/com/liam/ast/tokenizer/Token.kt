package com.liam.ast.tokenizer

import java.io.File

/**
 * @author liaomin
 * @date 6/3/20 5:13 下午
 * @version 1.0
 */
data class Token(val value: String,val lineNumber:Int,val charNumber:Int,val isAnnotate:Boolean,val source:File? = null) {

    override fun toString(): String {
        return value
//        return "$value $lineNumber $charNumber $isAnnotate"
    }
}
