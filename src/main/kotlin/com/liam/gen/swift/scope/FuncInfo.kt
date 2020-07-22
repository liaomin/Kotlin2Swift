package com.liam.gen.swift.scope

import java.util.*

data class FuncInfo(val name:String, val args:List<Args>, val returnType:String? = null){
    //泛型
    val typeParameters:List<TypeParameter>  by lazy { LinkedList<TypeParameter>() }
    data class Args(val name: String?, val type: String?, val default: String? = null)
    data class TypeParameter(val name: String,val parent: String? = null)
    fun isSameType(argTypes: List<Args>):Boolean{
        val argsSize = args.size
        val argTypeSize = argTypes.size
        if(argsSize == 0 && argTypeSize == 0){
            return true
        }
        if(argTypeSize == 0){
            for (i in 0 until argsSize){
                val arg = args[i]
                if(arg.default == null){
                    return false
                }
            }
            return true
        }
        var j = 0
        var argJ = argTypes[j]
        for (i in 0 until argsSize){
            val arg = args[i]
            var match = false
            if(argJ.name != null){
                if(arg.name.equals(argJ.name) && arg.type.equals(argJ.type)){
                    match = true
                }
            }else{
                if(arg.type.equals(argJ.type)){
                    match = true
                }
            }
            if(!match && arg.default != null){
                match = true
            }
            if(match){
                if(j == argTypeSize - 1 && i == argsSize - 1){
                    return true
                }else{
                    if(j < argTypeSize - 1){
                        j++
                        argJ = argTypes[j]
                    }
                }
            }
        }
        return false
    }
}