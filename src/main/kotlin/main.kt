
import com.liam.ast.psi.Converter
import com.liam.ast.psi.Parser
import com.liam.gen.swift.FileCodeGen
//import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
//import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import java.io.File
import java.io.FileReader

/**
 * @author liaomin
 * @date 6/17/20 10:51 下午
 * @version 1.0
 */

fun main() {
    val sourceFile = File("/Users/liaomin/Documents/ideaProject/Kotlin2Swift/src/main/kotlin/Test.kt")
    val reader = FileReader(sourceFile)
    val code =  reader.readText()
    val c =  Converter()
    val ktFile = Parser().parsePsiFile(code)
    FileCodeGen(ktFile).genCode()
}
