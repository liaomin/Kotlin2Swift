package com.liam.gen

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import org.jetbrains.kotlin.psi.KtFile


open class SwiftCodeGen {

    fun genCode(project: Project){
//        ProjectFileIndex.SERVICE.getInstance(e.getProject()).iterateContent(object : ContentIterator() {
//            fun processFile(fileInProject: VirtualFile): Boolean {
//                println(fileInProject.fileType)
//                val psiFile1 = PsiManager.getInstance(e.getProject()).findFile(fileInProject)
//                if (psiFile1 == null) println("IT'S NULL!")
//                println(psiFile1!!.text)
//                return true
//            }
//        })
    }


    companion object:SwiftCodeGen()
}

