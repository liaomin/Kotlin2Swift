
import com.alibaba.fastjson.JSON
import com.liam.ast.psi.Converter
import com.liam.ast.psi.Node
import com.liam.ast.psi.Parser
import com.liam.ast.writer.Statement
import com.liam.ast.writer.SwiftWriter
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.config.CompilerConfiguration
import java.io.File
import java.io.FileReader

/**
 * @author liaomin
 * @date 6/17/20 10:51 下午
 * @version 1.0
 */
fun main() {

    var fr: FileReader? = null
    //    val sourceFile = File("/Users/liaomin/Documents/ideaProject/Kotlin/kern/SharedCode/src/commonMain/kotlin/com/hitales/ui/View.kt")
    val sourceFile = File("/Users/liaomin/Documents/ideaProject/Kotlin2Swift/src/main/kotlin/Test.kt")
//    val sourceFile = File("/Users/liaomin/Documents/ideaProject/Kotlin2Swift/src/main/kotlin/com/liam/ast/psi/Node.kt")

    try {
        val reader = FileReader(sourceFile)
        val code =  reader.readText()
        fr = reader
        val parser = Parser(converter = object : Converter(){
            override fun onNode(node: Node, elem: PsiElement) {
//                println(elem.textRange)
//                print("")
            }
        })

        ClassLoader.getSystemClassLoader().javaClass

        val f = parser.parseFile(code)
        println(JSON.toJSONString(f))

//        val ktFile:KtFile = PsiManager.getInstance(proj).findFile(LightVirtualFile("temp.kt", KotlinFileType.INSTANCE, code)) as KtFile
//        val imps = ktFile.importDirectives
//        val imps2 = ktFile.importList
//        ktFile.accept( object :PsiRecursiveElementWalkingVisitor(){
//            override fun visitElement(element: PsiElement?) {
//                super.visitElement(element)
//                println(element)
//            }
//        })
        print("")

        val writer = SwiftWriter()
        val s = Statement()
        val start = System.currentTimeMillis()
        writer.write(f,s)
        println(s)
        print(System.currentTimeMillis() - start)
    }finally {
        fr?.close()
    }
}
