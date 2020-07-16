
import com.alibaba.fastjson.JSON
import com.liam.ast.psi.Converter
import com.liam.ast.psi.Node
import com.liam.ast.psi.Parser
import com.liam.ast.writer.Statement
import com.liam.ast.writer.SwiftWriter
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
//import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
//import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.config.CompilerConfiguration
import java.io.File
import java.io.FileReader
import java.io.FileWriter

/**
 * @author liaomin
 * @date 6/17/20 10:51 下午
 * @version 1.0
 */

fun testWrite(swiftWriter: SwiftWriter<Any>, inFile:File, outFile:File){
    if(inFile.exists()){
        if(!outFile.parentFile.exists()){
            outFile.parentFile.mkdirs()
        }
        if(inFile.isDirectory){
            inFile.listFiles().forEach {
                var name = it.name
                if(name.endsWith(".kt")){
                    name = name.substring(0,name.length - 3) +".swift"
                }
                testWrite(swiftWriter, it, File(outFile,name))
            }
        }
        if(inFile.isFile && inFile.name.endsWith(".kt")){
            var of = outFile
            if(!outFile.name.endsWith(".swift")){
                var name = inFile.name
                name = name.substring(0,name.length - 3) +".swift"
                of = File(outFile,name)
            }
            val sourceFile = inFile
            var fr: FileReader? = null
            var fw: FileWriter? = null
            try {
                val reader = FileReader(sourceFile)
                val code =  reader.readText()
                fr = reader
                val f = Parser.parseFile(code)
                val s = Statement()
                val start = System.currentTimeMillis()
                swiftWriter.write(f,s)
                println(s)
                print(System.currentTimeMillis() - start)
                fw = FileWriter(of)
                fw.write(s.toString())
            }finally {
                fw?.close()
                fr?.close()
            }
        }
    }
}

fun main() {

    var fr: FileReader? = null
    //    val sourceFile = File("/Users/liaomin/Documents/ideaProject/Kotlin/kern/SharedCode/src/commonMain/kotlin/com/hitales/ui/View.kt")
    val sourceFile = File("/Users/liaomin/Documents/ideaProject/Kotlin2Swift/src/main/kotlin/Test.kt")
//    val sourceFile = File("/Users/liaomin/Documents/ideaProject/Kotlin2Swift/src/main/kotlin/com/liam/ast/psi/Node.kt")

    try {
        val reader = FileReader(sourceFile)
        val code =  reader.readText()
        fr = reader
        val parser = Parser()
        val f = parser.parseFile(code)
        println(JSON.toJSONString(f))
//
//
//        print("")
//
        val writer = SwiftWriter<Any>()
//        val s = Statement()
//        val start = System.currentTimeMillis()
//        writer.write(f,s)
//        println(s)
//        print(System.currentTimeMillis() - start)

        val testDire = File("./src/main/kotlin/com/liam/test")
        testWrite(writer,testDire,File("/Users/liaomin/Documents/ioslearn/Learn/Learn/conver"))


    }finally {
        fr?.close()
    }
}
