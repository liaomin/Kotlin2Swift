package com.liam.test


import com.liam.ast.psi.Parser
import com.liam.gen.swift.CodeGen
import com.liam.gen.per.FileInfo
import com.liam.gen.scope.RootScope
import com.liam.gen.scope.Scope
import org.junit.BeforeClass
import org.junit.Test;
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*

open class GenTest {

    fun testGen(path: String){
        val outFile = File("/Users/liaomin/Documents/ioslearn/SwiftTemp/SwiftTemp/conver/${path.replace(".kt",".swift")}")
        if(!outFile.parentFile.exists()){
            outFile.parentFile.mkdirs()
        }
        val file = File("./src/test/kotlin/${GenTest::class.java.`package`.name.replace(".","/")}",path)
        val sourceFile = file
        val reader = FileReader(sourceFile)
        val code =  reader.readText()
        val ktFile = Parser().parsePsiFile(code)
        val f = com.liam.gen.swift.structed.File()
        val scope = RootScope()
        val codeGen = CodeGen()
        val result = f.genCode(codeGen,ktFile,scope,null,null,false)
        val fw = FileWriter(outFile)
        println(result.statement)
        println(scope)
        fw.write(result.statement.toString())
        fw.close()
    }

    @Test
    fun testGenFiles(){
        val dir = File("./src/test/kotlin/${GenTest::class.java.`package`.name.replace(".","/")}")
        val dirs = dir.listFiles {f -> f.isDirectory}
        val fileList:LinkedList<File> = LinkedList()
        dirs.forEach {
            it.listFiles() { dir, name -> name.endsWith(".kt") }.forEach { fileList.add(it) }
        }
        val fileInfos:LinkedList<FileInfo> = LinkedList()
        val parser = Parser()
        fileList.forEach {
            val reader = FileReader(it)
            val code =  reader.readText()
            val ktFile = parser.parsePsiFile(code,it.name)
            fileInfos.add(FileInfo(ktFile,it.name))
        }
        val codeGen = CodeGen()
        codeGen.genFiles(fileInfos)
    }

    fun addFiles(f:File,fileList:LinkedList<File>){
        if(f.isDirectory){
            f.listFiles().forEach{addFiles(it,fileList)}
        }else if(f.name.endsWith(".kt")){
            fileList.add(f)
        }
    }

    @Test
    fun testPerParse(){
        val dir = File("./src/test/kotlin/${GenTest::class.java.`package`.name.replace(".","/")}")
        val dirs = dir.listFiles {f -> f.isDirectory && f.name.startsWith("per")}
        val fileList:LinkedList<File> = LinkedList()
        dirs.forEach{ addFiles(it,fileList) }
        val fileInfos:LinkedList<FileInfo> = LinkedList()
        val parser = Parser()
        fileList.forEach {
            val reader = FileReader(it)
            val code =  reader.readText()
            val ktFile = parser.parsePsiFile(code,it.name)
            fileInfos.add(FileInfo(ktFile,it.name))
        }
        val codeGen = CodeGen()
        val scope = codeGen.genFiles(fileInfos)
        println(scope)
    }

    @Test
    fun testBasicFunction(){
        testGen("basic/Functions.kt")
    }

    @Test
    fun testBasicType(){
        testGen("basic/Type.kt")
    }

    @Test
    fun testBasicClass(){
        testGen("structed/Class.kt")
    }

    @Test
    fun testBasicInterface(){
        testGen("structed/Interface.kt")
    }

    @Test
    fun testProperties(){
        testGen("structed/Properties.kt")
    }

    @Test
    fun testBasicControlFlow(){
        testGen("basic/ControlFlow.kt")
    }

    companion object{
        @BeforeClass
        @JvmStatic
        fun setUpBeforeClass(){

        }
    }

}