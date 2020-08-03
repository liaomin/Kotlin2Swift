package com.liam.gen.scope

import java.util.*
import kotlin.collections.HashMap

class FileScope(parent: Scope? = null, packageName:String, val fileName:String) : Scope(parent,packageName,packageName){
    /**
     * 导入的其他包
     */
    val imports: LinkedList<Scope> by lazy { LinkedList<Scope>() }

    init {
        if(parent != null && parent is PackageScope){
            imports.add(parent)
        }
    }

    fun getAbsoluteName():String = packageName+"."+fileName

    override fun finRealType(type: String): String {
        val cache = typeScope[type]
        if(cache != null && cache is ClassScope){
            return cache.callName
        }
        imports.forEach {
            if(it is ClassScope && it.name.equals(type)){
                return it.callName
            }else{
                val cache = it.typeScope[type]
                if(cache != null && cache is ClassScope){
                    return cache.callName
                }
            }
        }
        return parent?.finRealType(type) ?: type
    }
}
