package com.liam.gen.swift.scope

class RootScope : Scope(){

    fun getPackageScope(packageName:String):PackageScope{
        val scope = typeScope.computeIfAbsent(packageName, { PackageScope(this,packageName) })
        if(scope !is PackageScope){
            error("")
        }
        return scope
    }

}