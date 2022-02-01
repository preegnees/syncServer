package ru.radmir.synServer.synServer.database.swaydb

import org.springframework.stereotype.Component
import ru.radmir.synServer.synServer.init.Vars
import swaydb.java.Map
import swaydb.java.memory.MemoryMap
import swaydb.java.serializers.Default.stringSerializer


@Component
class Swaydb {
    private var map: Map<String, String, Void>? = MemoryMap
        .functionsOff(stringSerializer(), stringSerializer())
        .get()
    fun set(name: String, hash: String){
        map?.put(name, hash)
    }
    fun get(name: String): String? {
        return try{
            map?.get(name)?.get()
        } catch (e: Exception){
            Vars.otherEmpty
        }
    }
}