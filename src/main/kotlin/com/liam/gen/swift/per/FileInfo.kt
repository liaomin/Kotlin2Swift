package com.liam.gen.swift.per

import com.liam.gen.swift.scope.PsiResult
import org.jetbrains.kotlin.psi.KtFile

class FileInfo(val ktFile: KtFile, val packageName:String, val fileName:String){
    var psiResult: PsiResult? = null
    var isPreParsed:Boolean = false
}