package com.liam.gen.scope

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.PropertyFilter
import com.alibaba.fastjson.serializer.SerializerFeature
import com.intellij.psi.PsiElement
import com.liam.gen.Statement
import com.liam.gen.swift.Logger
import com.liam.gen.per.FuncInfo
import com.liam.gen.per.PropertyInfo
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


data class PsiResult(val statement: Statement, val name:String?, val returnType:String?,var tag:Any? = null){
    companion object{
        val Null:PsiResult by lazy { PsiResult(Statement(),null,null) }
    }
}


open class Scope(val parent: Scope? = null, val name:String, val packageName: String) {

    val scopeName:String = this::class.java.name

    val typeScope:HashMap<String,Scope> = HashMap()

    @Deprecated(" use propertyMap")
    val variableMap:HashMap<String,String?> = HashMap()

    val propertyMap:HashMap<String,PropertyInfo> = HashMap()

    val functions:HashMap<String,ArrayList<FuncInfo>> = HashMap()

    val cache:HashMap<PsiElement,PsiResult> by lazy {  HashMap<PsiElement,PsiResult>() }

    val perCache:HashMap<PsiElement,Any> by lazy {  HashMap<PsiElement,Any>() }

    fun getCachedResult(key:PsiElement):PsiResult? = parent?.getCachedResult(key) ?: cache.get(key)

    fun setCachedResult(key:PsiElement,value:PsiResult){
        if(parent != null){
            parent!!.cache[key] = value
        }else{
            cache[key] = value
        }
    }

    fun addFunc(funcInfo: FuncInfo){
        functions.computeIfAbsent(funcInfo.name){java.util.ArrayList()}.add(funcInfo)
    }

    fun getFunc(name: String):ArrayList<FuncInfo>? = functions.get(name)

    open fun setVariable(variable:String?,type:String?) {
        if(variable != null){
            variableMap[variable] = type
        }
    }

    fun getFuncInfo(name: String,argTypes: List<FuncInfo.Args>): FuncInfo?{
        val infoList = getFunc(name)
        if(infoList != null)for (info in infoList){
            if(info.isSameType(argTypes)){
                return info
            }
        }
        if(parent != null){
            return parent?.getFuncInfo(name,argTypes)
        }
        return null
    }

    fun getFuncType(name: String,argTypes: List<FuncInfo.Args>):String?{
        val funcInfo = getFuncInfo(name,argTypes)
        if(funcInfo == null){
            val classScope =  getScope(name)
            if(classScope != null){
                return name
            }
            Logger.error("not find function $name in scope")
        }
        return funcInfo?.returnType ?: "Void"
    }

    open fun finRealType(type: String):String{
        val cache = typeScope[type]
        if(cache != null && cache is ClassScope){
            return cache.callName
        }
        return parent?.finRealType(type) ?: type
    }

    fun getRealType(type:String):String{
        if(type.contains(".")){
            //absolute ref
            val rootScope = getRootScope()
            val paths = type.split(".")
            for (i in paths.size - 1 downTo 0){
                val pre = paths.subList(0,i)
                val pro = paths.subList(i,paths.size)
                val `package` = pre.joinToString(".")
                val packageScope:PackageScope?= rootScope.getPackageScopeOrNull(`package`)
                if(packageScope != null){
                    var scope:Scope? = packageScope
                    pro.forEachIndexed { index, s ->
                        if(scope != null){
                            var temp = scope?.typeScope?.get(s)
                            if(scope is PackageScope){
                                temp = (scope as PackageScope)?.findScope(s)
                            }
                            if(temp != null && temp is Scope){
                                scope = temp
                                if(index == pro.size - 1 && temp is ClassScope){
                                    return temp.callName
                                }
                            }else{
                                return@forEachIndexed
                            }
                        }else{
                            return@forEachIndexed
                        }
                    }
                }
            }
        }

        return type
    }

    open fun getType(variable:String,targetType:String? = null):String{
        var cacheType = variableMap[variable] ?: parent?.getType(variable)
        if(cacheType == null){
            cacheType = getConstVariableType(variable)
        }
        if(cacheType == null) Logger.error("not find type for $variable in scope,scope name:$name")
        return cacheType ?: "Void"
    }

    open fun getFunctionName(funcName:String,index:Int,argTypes: List<FuncInfo.Args>):String?{
        getFuncInfo(funcName,argTypes)?.let {
           return it.args[index].name
        }
        return null
    }

    open fun getConstVariableType(variable:String):String?{
        return null
    }

    open fun newScope(): Scope {
        val scope = Scope(this,name,packageName)
        return scope
    }

    fun newClassScope(className:String): ClassScope {
        val scope = ClassScope(this,className,packageName)
        typeScope[className] = scope
        return scope
    }

    fun newFileScope(packageName:String,fileName:String): FileScope {
        val scope = FileScope(this,packageName,fileName)
        typeScope[scope.getAbsoluteName()] = scope
        return scope
    }

    open fun getScope(className: String):Scope? {
        var scope =  typeScope[className]
        if(scope == null){
            scope =  parent?.getScope(className)
        }
        return scope
    }

    fun getSuperClass(className: String):String{
        val s = typeScope[className]
        s?.let {
            if(it is ClassScope){
                it.superClasses.forEach {
                    val q = typeScope[it]
                    if(q is ClassScope && !q.isInterface){
                        return it
                    }
                }
            }
        }
        return className
    }

    fun getRootScope():RootScope{
        if(parent != null){
            return parent.getRootScope()
        }
        if(this is RootScope){
            return this
        }
        error("")
    }

    fun isModuleScope():Boolean{
        return when(this){
            is RootScope -> return true
            is PackageScope -> return true
            is FileScope -> return true
            else -> {
                return false
            }
        }
    }

    override fun toString(): String {
        return JSON.toJSONString(this,object :PropertyFilter{
            override fun apply(`object`: Any?, name: String, value: Any?): Boolean {
                return !name.toLowerCase().contains("cache")
            }
        },SerializerFeature.PrettyFormat)
    }

}
