package ru.radmir.synServer.synServer.encryption

import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.radmir.synServer.synServer.init.Vars
import java.io.File
import java.io.IOException

@Component
class PasswordServer() {
    private lateinit var password: String
    fun getPassword() = password
}

@Component
class ReadPasswordFromJson() {
    @Autowired
    private lateinit var passwordServer: PasswordServer

    fun start(): PasswordServer{
        val file = File(Vars.configFileName)
        file.createNewFile()
        try {
            passwordServer = Gson()
                .fromJson(
                    File(Vars.configFileName)
                        .inputStream()
                        .readBytes()
                        .toString(Charsets.UTF_8),
                    PasswordServer::class.java
                )
        } catch (ex: Exception) {
            throw Exception(Vars.configErrorsReadConfigFailed)
        }
        return passwordServer
    }
}