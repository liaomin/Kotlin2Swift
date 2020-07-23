package com.liam.test

import ControlFlow
import com.liam.ast.psi.Converter
import com.liam.ast.psi.Parser
import com.liam.gen.swift.FileCodeGen
import org.junit.BeforeClass
import org.junit.Test;
import java.io.File
import java.io.FileReader
import java.io.FileWriter

open class GenTest {


    fun testGen(path: String){
        val outFile = File("/Users/liaomin/Documents/ioslearn/SwiftTemp/SwiftTemp/conver/${path.replace(".kt",".swift")}")
        if(outFile.parentFile.exists()){
            outFile.parentFile.mkdirs()
        }
        val file = File("./src/test/kotlin/${GenTest::class.java.`package`.name.replace(".","/")}",path)
        val sourceFile = file
        val reader = FileReader(sourceFile)
        val code =  reader.readText()
        val ktFile = Parser().parsePsiFile(code)
        val codeGen =  FileCodeGen(ktFile)
        val fw = FileWriter(outFile)
        fw.write(codeGen.genCode().toString())
        fw.close()
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
        testGen("basic/Class.kt")
    }

    @Test
    fun testBasicInterface(){
        testGen("basic/Interface.kt")
    }

    @Test
    fun testBasicControlFlow(){
        val q =  ControlFlow().hasPrefix(1)
        testGen("basic/ControlFlow.kt")
    }

    companion object{
        @BeforeClass
        @JvmStatic
        fun setUpBeforeClass(){

        }
    }

}