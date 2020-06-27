package com.liam.uitls

import java.io.File
import java.io.FileInputStream
import java.util.*
import java.util.jar.JarInputStream
import kotlin.collections.ArrayList


/**
 * @author liaomin
 * @date 6/24/20 4:23 下午
 * @version 1.0
 */
class PackageUtils {
    companion object{

        @Throws(Exception::class)
        fun getClassesInPackage(packageName: String) = with(packageName.replace(".", File.separator)) {
            System.getProperty("java.class.path")
                .split(System.getProperty("path.separator").toRegex())
                .flatMap { classpathEntry ->
                    if (classpathEntry.endsWith(".jar")) {
                        JarInputStream(FileInputStream(File(classpathEntry))).use { s ->
                            generateSequence { s.nextJarEntry }
                                .map { it.name }
                                .filter { this in it && it.endsWith(".class") }
                                .map { it.substring(0, it.length - 6) }
                                .map { it.replace('|', '.').replace('/', '.') }
                                .map { Class.forName(it) }
                                .toList()
                        }
                    } else {
                        File(classpathEntry, this).list()
                            ?.asSequence()
                            ?.filter { it.endsWith(".class") }
                            ?.map { it.substring(0, it.length - 6) }
                            ?.map { Class.forName("$packageName.$it") }
                            ?.toList() ?: emptyList()
                        return getClassesInFile(packageName,File(classpathEntry, this))
                    }
                }
        }

        @Throws(Exception::class)
        fun getClassesInFile(prefix:String,file: File):List<Class<*>>{
            val res = ArrayList<Class<*>>()
            val list = file.listFiles()
            for (f in list){
                if(f.isDirectory){
                    var p = Optional.ofNullable(prefix).orElse("")
                    res.addAll(getClassesInFile("$p.${f.name}",f))
                }else{
                    var name = f.name
                    if(name.endsWith(".class")){
                        name = name.substring(0, name.length - 6)
                    }
                    res.add(Class.forName("$prefix.$name"))
                }
            }
            return res
        }
    }

}
