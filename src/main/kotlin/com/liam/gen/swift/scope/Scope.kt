package com.liam.gen.swift.scope

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.PropertyFilter
import com.intellij.psi.PsiElement
import com.liam.gen.Statement
import com.liam.gen.swift.Logger
import com.liam.gen.swift.per.FuncInfo
import com.liam.gen.swift.per.Modifier
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


data class PsiResult(val statement: Statement, val name:String?, val returnType:String?,var tag:Any? = null){
    companion object{
        val Null:PsiResult by lazy { PsiResult(Statement(),null,null) }
    }
}


open class Scope(val parent: Scope? = null, val name:String? = null) {

    val typeScope:HashMap<String,Scope> = HashMap()

    val variableMap:HashMap<String,String?> = HashMap()

    val functions:HashMap<String,ArrayList<FuncInfo>> = HashMap()

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
        val scope = Scope(this)
        return scope
    }

    fun newClassScope(className:String): ClassScope {
        val scope = ClassScope(this,className)
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

    override fun toString(): String {
        return JSON.toJSONString(this,object :PropertyFilter{
            override fun apply(`object`: Any?, name: String?, value: Any?): Boolean {
                return !name.equals("cache")
            }
        })
    }

    class ClassScope(parent: Scope? = null, name:String? = null) : Scope(parent,name){
        val superClasses:LinkedList<String> by lazy { LinkedList<String>() }
        var isInterface:Boolean = false

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

}
