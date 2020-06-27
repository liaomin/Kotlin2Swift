package com.liam.uitls

import com.sun.nio.file.SensitivityWatchEventModifier
import java.io.File
import java.lang.reflect.Field
import java.nio.file.*
import java.nio.file.FileSystems.getDefault
import kotlin.concurrent.thread

/**
 * @author liaomin
 * @date 6/3/20 5:23 下午
 * @version 1.0
 */
class SourceFileWatch(path:String = ".") {

    companion object{
        var dirField:Field? = null
    }

    val ws: WatchService

    init {
        val fileSystem = getDefault()
        ws = fileSystem.newWatchService()
        val dir = File(path);
        register(dir)
    }

    fun register(file:File){
        if(file.isDirectory){
            file.listFiles().forEach(this::register)
            val path = Paths.get(file.absolutePath)
            path.register(ws, arrayOf(StandardWatchEventKinds.ENTRY_MODIFY,StandardWatchEventKinds.ENTRY_CREATE,StandardWatchEventKinds.ENTRY_DELETE), SensitivityWatchEventModifier.HIGH)
        }
    }

    fun getDirField(c:Class<Any>){
        if(dirField == null){
            try {
                val field =  c.getDeclaredField("dir")
                dirField = field
                field.isAccessible = true
            }catch (e:Exception){
                val sc = c.superclass
                if(sc != null){
                    getDirField(sc)
                }
            }

        }
    }

    fun watch(cb:(file:File, kind: WatchEvent.Kind<Path>)->Unit){
        thread {
            while (true){
                val key = ws.take()
                getDirField(key.javaClass)
                val path = dirField?.get(key) as Path
                val dir = path.toFile().absoluteFile
                val events = key.pollEvents()
                events.forEach {
                    val name = it.context().toString()
                    val file = File(dir,name)
                    if(file.exists() && file.isFile){
                        cb(file,it.kind() as WatchEvent.Kind<Path>)
                    }
                }
                key.reset()
            }
        }
    }
}
