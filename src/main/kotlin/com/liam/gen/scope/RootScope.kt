package com.liam.gen.scope

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.PropertyFilter
import com.alibaba.fastjson.serializer.SerializerFeature

class RootScope : Scope(null,"",""){

    /**
     * 模块全局名字，Swift模块内名字不能重复
     */
    val globalNames:HashMap<String,String> = HashMap()

    var index = 0

    fun getGlobalName(name:String,packageName: String):String{
        if(globalNames.containsKey(name)){
            val n = "${name}__${index++}__"
            globalNames[n] = n
            return n
        }else{
            globalNames[name] = name
            return name
        }
    }


    fun getPackageScope(packageName:String):PackageScope{
        val scope = typeScope.computeIfAbsent(packageName, { PackageScope(this,packageName) })
        if(scope !is PackageScope){
            error("")
        }
        return scope
    }

    fun getPackageScopeOrNull(packageName:String):PackageScope?{
        val scope = typeScope[packageName]
        if(scope != null && scope is PackageScope){
            return scope
        }
        return null
    }

}