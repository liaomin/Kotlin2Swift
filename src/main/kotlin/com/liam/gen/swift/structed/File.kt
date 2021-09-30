package com.liam.gen.swift.structed

import com.liam.gen.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.per.FileInfo
import com.liam.gen.per.PerParser
import com.liam.gen.scope.PsiResult
import com.liam.gen.scope.RootScope
import com.liam.gen.scope.Scope
import org.jetbrains.kotlin.fileClasses.javaFileFacadeFqName
import org.jetbrains.kotlin.psi.KtFile

open class File : Struct<KtFile>() {

    fun genFiles(gen: CodeGen, files:List<FileInfo>, scope: RootScope){
        files.forEach { File.perParserStep1(it.ktFile,scope) }
        files.forEach { File.perParserStep2(it.ktFile,scope) }
    }

    override fun perParserStep1(psiElement: KtFile, scope: Scope) {
        if(scope is RootScope){
            val packageName = psiElement.packageFqName.asString()
            val fileName = psiElement.name
            val psiFile = psiElement
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
                val packageScope = scope.getPackageScope(packagePath)
                var importScope:Scope? = packageScope
                if(importType != null){
                    importScope = packageScope.getScope(importType)
                }
                if(importScope != null){
                    packageScope.getFileScope(packageName).imports.add(importScope)
                }
            }
            val packageScope = scope.getPackageScope(packageName)
            val fileScope = packageScope.getFileScope(fileName)
            perParserDeclarations(psiElement.declarations,fileScope,1)
        }
    }

    override fun perParserStep2(psiElement: KtFile, scope: Scope) {
        if(scope is RootScope){
            val packageName = psiElement.packageFqName.asString()
            val fileName = psiElement.name
            val packageScope = scope.getPackageScope(packageName)
            val fileScope = packageScope.getFileScope(fileName)
            perParserDeclarations(psiElement.declarations,fileScope,2)
        }
    }

    override fun onGenCode(gen: CodeGen, v: KtFile, scope: Scope, targetType: String?, expectType: String?, shouldReturn: Boolean): PsiResult {
        val statement = Statement()
        if(scope.parent != null) error("file scope must root scope")
        val packageName = v.packageDirective?.takeIf { it.packageNames.isNotEmpty() }?.let { it.packageNames.first() }?.getReferencedName()?:""
        val fileName = v.name
        val fileScope = scope.newFileScope(packageName,fileName)
        val dec = genDeclarations(gen,fileScope,v.declarations)
        statement.append(dec)
//
//
//        v.importDirectives?.forEach {
//            val packaes = StringBuilder()
//            it.importedFqName?.pathSegments()?.forEachIndexed { index, name ->
//                if(index > 0){
//                    packaes.append(".")
//                }
//                packaes.append(name)
//            }
//            if(it.isAllUnder){
//                packaes.append(".*")
//            }
////            importes.add(packaes.toString())
//        }

        return PsiResult(statement,fileName,null)
    }

    companion object :File()

}