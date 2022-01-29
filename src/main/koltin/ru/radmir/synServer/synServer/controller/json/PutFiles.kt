package ru.radmir.synServer.synServer.controller.json

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.springframework.stereotype.Component

@Component
data class Client (
    @SerializedName("name") val name : String? = null,
    @SerializedName("pair_name") val pairName : String? = null,
    @SerializedName("file_name") val fileName : String? = null,
    @SerializedName("content_of_file") val contentOfFile : String? = null
)

@Component
data class RootPutFiles (
    @SerializedName("client") var client: ArrayList<Client> = arrayListOf()
)

@Component
class CreatorJsonPutFiles() {
    fun start(json: String): RootPutFiles {
        val gson = Gson()
        return gson.fromJson(json, RootPutFiles::class.java)
    }
}