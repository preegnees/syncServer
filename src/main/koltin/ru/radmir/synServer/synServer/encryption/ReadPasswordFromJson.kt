package ru.radmir.synServer.synServer.encryption

import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File
import java.io.IOException

@Component
class PasswordServer() {
    private lateinit var password: String
    fun getPassword() = password
    fun setPassword(value: String) {
        password = value
    }
}

@Component
class ReadPasswordFromJson() {
    @Autowired
    private lateinit var passwordServer: PasswordServer

    fun start(): PasswordServer{
        val file = File("config.json")
        file.createNewFile()
        try {
            passwordServer = Gson()
                .fromJson(
                    File("config.json")
                        .inputStream()
                        .readBytes()
                        .toString(Charsets.UTF_8),
                    PasswordServer::class.java
                )
        } catch (ex: Exception) {
            passwordServer.setPassword("qwerty")
        }
        return passwordServer
    }
}