package com.liam.gen.scope

import com.liam.gen.per.FuncInfo
import com.liam.gen.per.Modifier
import java.util.*
import kotlin.collections.HashMap


open class ClassScope(parent: Scope?,name:String,packageName:String) : Scope(parent,name,packageName){
    var callName:String = name

    val superClasses: LinkedList<String> by lazy { LinkedList<String>() }
    var isInterface:Boolean = false
    var isEnum:Boolean = false

    fun shouldAddOverride(info: FuncInfo):Boolean{
        superClasses.forEach {
            getScope(it)?.let {
                if(it is ClassScope && !it.isInterface){
                    val funcInfo = it.getFuncInfo(info.name,info.args)
                    if(funcInfo != null && !info.modifierList.contains(Modifier.CONVENIENCE)){
                        return true
                    }else if(funcInfo == null && info.name.equals("init") && info.args.isEmpty()){
                        return true
                    }
                }
            }
        }
        return false
    }
}