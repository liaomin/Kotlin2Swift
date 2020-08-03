package com.liam.gen.per

import com.liam.gen.scope.PsiResult
import org.jetbrains.kotlin.psi.KtFile

class FileInfo(val ktFile: KtFile,  val fileName:String){
    val packageName:String = ktFile.packageFqName.asString()
    var psiResult: PsiResult? = null
}