package com.liam.gen.swift.structed

import com.liam.gen.Statement
import com.liam.gen.swift.CodeGen
import com.liam.gen.swift.per.FileInfo
import com.liam.gen.swift.per.PerParser
import com.liam.gen.swift.scope.PsiResult
import com.liam.gen.swift.scope.RootScope
import com.liam.gen.swift.scope.Scope
import org.jetbrains.kotlin.psi.KtFile

open class File : Struct<KtFile>() {

    fun genFiles(gen: CodeGen, files:List<FileInfo>, scope: RootScope){
        files.forEach {
            PerParser.preParserFile(gen,it,scope,files)
        }
        files.forEach {
            it.psiResult = genCode(gen,it.ktFile,scope,null,null,false)
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