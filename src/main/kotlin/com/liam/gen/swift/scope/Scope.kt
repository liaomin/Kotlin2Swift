package com.liam.gen.swift.scope

import com.alibaba.fastjson.JSON
import com.intellij.psi.PsiElement
import com.liam.ast.writer.Statement
import com.liam.gen.swift.Logger
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtDeclaration
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashMap

data class FuncInfo(val name:String, val args:List<Args>, val returnType:String? = null){
    data class Args(val name: String, val type: String, val default: String? = null)
    fun isSameType(argTypes: List<String?>):Boolean{
        if(argTypes.size == args.size){
            for (i in 0 until args.size){
                if(!args[i].type.equals(argTypes[i])){
                    return false
                }
            }
            return true
        }
        return false
    }
}

data class PsiResult(val statement:Statement,val name:String?,val returnType:String?)

open class Scope(val parent: Scope? = null, val name:String? = null) {

    val variableMap:HashMap<String,String?> = HashMap()

    val functions:ConcurrentHashMap<String,ArrayList<FuncInfo>> = ConcurrentHashMap()

    val structed:ConcurrentHashMap<String, Scope> = ConcurrentHashMap()

    val cache:HashMap<in PsiElement,PsiResult> by lazy {  HashMap<PsiElement,PsiResult>() }

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

    fun getFunc(name: String):ArrayList<FuncInfo>{
        return functions.computeIfAbsent(name){java.util.ArrayList()}
    }

    open fun setVariable(variable:String,type:String?) {
        variableMap[variable] = type
    }

    fun getFuncInfo(name: String,argTypes: List<String?>): FuncInfo?{
        for (info in getFunc(name)){
            if(info.isSameType(argTypes)){
                return info
            }
        }
        if(parent != null){
            return parent?.getFuncInfo(name,argTypes)
        }
        Logger.error("not find func info for $name")
        return null
    }

    fun getFuncType(name: String,argTypes: List<String>):String?{
        return getFuncInfo(name,argTypes)?.returnType ?: "Void"
    }

    open fun getType(variable:String,targetType:String? = null):String{
        if(targetType != null){
            return structed.computeIfAbsent(targetType, {k -> Scope(this, k) }).getType(variable)
        }
        var cacheType = variableMap[variable] ?: parent?.getType(variable)
        if(cacheType == null){
            cacheType = getConstVariableType(variable)
        }
        if(cacheType == null) Logger.error("not find type for $variable in scope,scope name:$name")
        return cacheType ?: "Void"
    }

    open fun getFunctionName(funcName:String,index:Int,argTypes: List<String?>):String?{
        getFuncInfo(funcName,argTypes)?.let {
           return it.args[index].name
        }
        return null
    }

    open fun getConstVariableType(variable:String):String?{
        return null
    }

    open fun newScope(): Scope {
        val scope = Scope(this)
        return scope
    }

    open fun newClassScope(): Scope {
        val scope = ClassScope(this)
        return scope
    }

    override fun toString(): String {
        return JSON.toJSONString(this)
    }


    fun discovery(field:KtDeclaration):Scope{
        when (field) {
            is KtClassOrObject -> {
                ClassDiscoverer.onDiscovery(field,this)
            }
        }
        return this
    }

    fun discovery(file:KtFile):Scope{
        FileDiscoverer.onDiscovery(file,this)
        return this
    }

    class ClassScope(parent: Scope? = null, name:String? = null) : Scope(parent,name)
}
