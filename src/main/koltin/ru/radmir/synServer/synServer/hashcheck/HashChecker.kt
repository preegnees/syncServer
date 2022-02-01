package ru.radmir.synServer.synServer.hashcheck

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.radmir.synServer.synServer.database.swaydb.Swaydb
import ru.radmir.synServer.synServer.init.Vars
import java.io.File

@Component
class HashChecker {
    @Autowired
    private lateinit var storage: Swaydb
    private var hashAll = Vars.otherEmpty
    private val root = Vars.configRootDirectory

    fun start(name: String, me: Boolean): Boolean{
        val oldHash = storage.get(name + me.toString())
        val dirs = getDirs(name) // [path/file, ]
        if (me){ // если только для name
            dirs.forEach{ if (name + Vars.filesUnderscore + name in it) { hash(it) } } // ищем name_name in path
        } else {
            dirs.forEach{ hash(it) }
        }
        return if (hashAll == oldHash) {
            hashAll = Vars.otherEmpty
            false // do not update
        } else {
            storage.set(name + me.toString(), hashAll)
            hashAll = Vars.otherEmpty
            true // updated
        }
    }
    private fun hash(path: String){
       hashAll += File(path).hashCode().toString()
    }
    private fun getDirs(name: String): MutableList<String>{
        val files = mutableListOf<String>()
        for (i in File(root).listFiles()!!){
            if (name in i.name) {
                val listFiles = File(i.path).listFiles()
                if (listFiles!!.isNotEmpty()){
                    for (j in listFiles) {
                        files.add(j.path.toString())
                    }
                }
            }
        }
        return files
    }
}