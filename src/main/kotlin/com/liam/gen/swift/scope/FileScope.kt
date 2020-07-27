package com.liam.gen.swift.scope

import java.util.*
import kotlin.collections.HashMap

class FileScope(parent: Scope? = null, val packageName:String, val fileName:String) : Scope(parent,fileName){
    /**
     * 导入的其他包
     */
    val imports: LinkedList<Scope> by lazy { LinkedList<Scope>() }

    /**
     * swift没有包的概念，会发生命名冲突，需要重新命名
     */
    val reNameMap = HashMap<String,String>()

    fun getAbsoluteName():String = packageName+"."+fileName
}