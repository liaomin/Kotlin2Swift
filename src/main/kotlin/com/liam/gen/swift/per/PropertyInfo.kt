package com.liam.gen.swift.per

import com.liam.gen.swift.scope.Scope

data class PropertyInfo(val name:String,val type:String,val scope: Scope){
    var override:Boolean = false
}