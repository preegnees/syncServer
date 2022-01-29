package ru.radmir.synServer.synServer.controller.json

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.springframework.stereotype.Component

@Component
data class ClientGive (
    @SerializedName("nameDir") var nameDir  : String? = null,
    @SerializedName("nameFile") var nameFile : String? = null
)

@Component
data class RootGiveFilesClient (
    @SerializedName("client") var clientGive: ArrayList<ClientGive> = arrayListOf()
)

@Component
class CreatorJsonGiveFilesClient() {
    fun start(json: String): RootGiveFilesClient {
        val gson = Gson()
        return gson.fromJson(json, RootGiveFilesClient::class.java)
    }
}







@Component
data class ServerGive (
    @SerializedName("pair_name") val pairName : String? = null,
    @SerializedName("file_name") val fileName : String? = null,
    @SerializedName("content_of_file") val contentOfFile : String? = null
)

@Component
data class RootGiveFilesServer (
    @SerializedName("server") var serverGive: ArrayList<ServerGive> = arrayListOf()
)

@Component
class CreatorJsonGiveFileServer() {
    fun start(rootGiveFilesServer: RootGiveFilesServer): String {
        val gson = Gson()
        return gson.toJson(rootGiveFilesServer)
    }
}