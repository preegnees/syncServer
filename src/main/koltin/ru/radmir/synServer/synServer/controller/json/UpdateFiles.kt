package ru.radmir.synServer.synServer.controller.json

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.springframework.stereotype.Component

@Component
data class Server (
    @SerializedName("nameDir") var nameDir  : String? = null,
    @SerializedName("nameFile") var nameFile : String? = null
)

@Component
data class RootUpdateFiles (
    @SerializedName("server") var server : ArrayList<Server> = arrayListOf()
)

@Component
class CreatorJsonUpdatedFiles() {

    fun start(rootUpdateFiles: RootUpdateFiles): String {
        val gson = Gson()
        return gson.toJson(rootUpdateFiles)
    }
}