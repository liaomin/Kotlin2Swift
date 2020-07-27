package com.liam.gen.swift.scope

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.PropertyFilter
import com.intellij.psi.PsiElement
import com.liam.gen.Statement
import com.liam.gen.swift.Logger
import com.liam.gen.swift.per.FuncInfo
import com.liam.gen.swift.per.Modifier
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

open class PackageScope(parent: Scope? = null, val `package`:String? = null) : Scope(parent,`package`) {

    fun getFileScope(fileName:String):FileScope{
        val scope = typeScope.computeIfAbsent(fileName, { PackageScope(this,fileName) })
        if(scope !is FileScope){
            error("")
        }
        return scope
    }

    override fun getScope(type:String):Scope?{
        typeScope.keys.forEach {
            val s = typeScope.get(it)?.getScope(type)
            if(s != null){
                return s
            }
        }
        return null
    }

}
