package com.liam.gen.per

import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.func.Func
import com.liam.gen.scope.FileScope
import com.liam.gen.scope.RootScope
import com.liam.gen.scope.Scope
import com.liam.gen.swift.property.Property
import com.liam.gen.swift.structed.File
import org.jetbrains.kotlin.psi.*

/**
 * 预解析，往scope填写属性方法，判断属性方法是否重载等
 */
open class PerParser{

    var index = 0

    fun perParserDeclarations(gen: CodeGen,declarations: List<KtDeclaration>,scope: Scope){
        //kotlin 不允许同一个包下名字重复，swift 则是不允许同一模块名字不重复
        val rootScope = scope.getRootScope()
        declarations.forEach {
            when (it){
                is KtNamedFunction -> {
                    val funcInfo = Func.getFuncInfo(gen,it,scope,null,null,false)
                    scope.addFunc(funcInfo)
                }
                is KtProperty -> {
                    Property.preParser(it,scope)
                }
                is KtClassOrObject -> {
                    var name = it.name!!
                    val classScope = scope.newClassScope(name)
                    if(scope is FileScope){
                        name = rootScope.getGlobalName(name,scope.packageName)
                        classScope.callName = name
                    }
                    scope.typeScope[name] = classScope
                    if(it is KtClass){
                        classScope.isInterface = it.isInterface()
                        classScope.isEnum = it.isEnum()
                    }
                    perParserDeclarations(gen,it.declarations,classScope)
                }
            }
        }
    }

    fun preParserFile(gen: CodeGen, scope: RootScope,group: Map<String,List<FileInfo>>) {

    }

    fun preParserFile(gen: CodeGen, v: FileInfo, scope: RootScope, allFiles: List<FileInfo>) {
//            perParserDeclarations(gen,psiFile.declarations,fileScope)
        
    }


    companion object: PerParser()
}