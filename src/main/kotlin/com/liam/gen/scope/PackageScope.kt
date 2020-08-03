package com.liam.gen.scope

open class PackageScope(parent: Scope? = null, val `package`:String) : Scope(parent,`package`,`package`) {

    fun getFileScope(fileName:String):FileScope{
        val scope = typeScope.computeIfAbsent(fileName, { FileScope(this,`package`,fileName) })
        if(scope !is FileScope){
            error("")
        }
        return scope
    }

    fun findScope(name: String):Scope?{
        typeScope.forEach{
            val cache = it.value.typeScope[name]
            if(cache != null){
                return cache
            }
        }
        return null
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
