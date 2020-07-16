package com.liam.gen.swift

import com.liam.gen.swift.scope.Scope
import org.jetbrains.kotlin.psi.*

class FileCodeGen(val psiFile:KtFile) : CodeGen(){

    val name = psiFile.name

    val packageName = psiFile.packageDirective?.takeIf { it.packageNames.isNotEmpty() }?.let { it.packageNames.first() }?.getReferencedName()?:""

    val importes:ArrayList<String> = ArrayList()

    override fun genCode() {
        val scope = Scope().discovery(psiFile)
        psiFile.importDirectives?.forEach {
            val packaes = StringBuilder()
            it.importedFqName?.pathSegments()?.forEachIndexed { index, name ->
                if(index > 0){
                    packaes.append(".")
                }
                packaes.append(name)
            }
            if(it.isAllUnder){
                packaes.append(".*")
            }
            importes.add(packaes.toString())
        }

        psiFile.declarations.forEach {
            when (it){
//                is KtEnumEntry -> convertEnumEntry(v)
                is KtClassOrObject -> {
                    statement.nextLine()
                    genClassOrObject(it,statement, scope,null,null,false)
                }
//                is KtAnonymousInitializer -> convertInit(v)
                is KtNamedFunction -> {
                    statement.nextLine()
                    genNamedFunction(it,statement, scope,null,null,false)
                }
//                is KtDestructuringDeclaration -> convertProperty(v)
                is KtProperty -> {
                    statement.nextLine()
                    genProperty(it,statement, scope,null,null,false)
                }
//                is KtTypeAlias -> convertTypeAlias(v)
//                is KtSecondaryConstructor -> convertConstructor(v)
//                else -> error("Unrecognized declaration type for $it")
            }
        }

        println(scope)
        println(statement)
    }

}