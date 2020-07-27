package com.liam.gen.swift.per

import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.scope.RootScope
import com.liam.gen.swift.scope.Scope

/**
 * 预解析，往scope填写属性方法，判断属性方法是否重载等
 */
open class PerParser{

    fun preParserFile(gen: CodeGen, v: FileInfo, scope: RootScope, allFiles: List<FileInfo>) {
        if(!v.isPreParsed){
            val psiFile = v.ktFile
            psiFile.importDirectives?.forEach {
                val packages = StringBuilder()
                it.importedFqName?.pathSegments()?.forEachIndexed { index, name ->
                    if(index > 0){
                        packages.append(".")
                    }
                    packages.append(name)
                }
                val path = packages.toString()
                var packagePath:String = path
                var importType:String? = null
                if(!it.isAllUnder){
                    val index = path.lastIndexOf(".")
                    packagePath = path.substring(0 ,index)
                    importType = path.substring(index+1,path.length)
                }
                val import = allFiles.filter { it.packageName.equals(packagePath) }
                if(import.size != 1) error("")
                import.last().let {
                    if(!it.isPreParsed){
                        this.preParserFile(gen, it, scope, allFiles)
                    }
                }
                val packageScope = scope.getPackageScope(packagePath)
                var importScope:Scope? = packageScope
                if(importType != null){
                    importScope = packageScope.getScope(importType)
                }
                if(importScope != null){
                    packageScope.getFileScope(v.fileName).imports.add(importScope)
                }
            }
        }
    }


    companion object: PerParser()
}