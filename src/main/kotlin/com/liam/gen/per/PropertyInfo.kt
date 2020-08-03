package com.liam.gen.per

data class PropertyInfo(val name:String,val type:String?){
    var callName = name
    var override:Boolean = false
}