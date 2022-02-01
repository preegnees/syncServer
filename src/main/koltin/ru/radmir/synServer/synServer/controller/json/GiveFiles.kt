package ru.radmir.synServer.synServer.controller.json

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.springframework.stereotype.Component
import ru.radmir.synServer.synServer.init.Vars

@Component
data class ClientGive (
    @SerializedName(Vars.jsonNameDirGiveClient) var nameDir  : String? = null,
    @SerializedName(Vars.jsonNameFileGiveClient) var nameFile : String? = null,
    @SerializedName(Vars.jsonSizeOfFileGiveClient) val sizeFile: String? = null,
    @SerializedName(Vars.jsonTimeOfFileGiveClient) val timeFile: String? = null
)

@Component
data class RootGiveFilesClient (
    @SerializedName(Vars.jsonClientGiveClient) var clientGive: ArrayList<ClientGive> = arrayListOf()
)

@Component
class CreatorJsonGiveFilesClient() {
    fun start(json: String): RootGiveFilesClient {
        val gson = Gson()
        return gson.fromJson(json, RootGiveFilesClient::class.java)
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Component
data class ServerGive (
    @SerializedName(Vars.jsonPairNameGiveServer) val pairName : String? = null,
    @SerializedName(Vars.jsonFileNameGiveServer) val fileName : String? = null,
    @SerializedName(Vars.jsonContentOfFileGiveServer) val contentOfFile : String? = null,
    @SerializedName(Vars.jsonSizeOfFileGiveServer) val sizeFile: String? = null,
    @SerializedName(Vars.jsonTimeOfFileGiveServer) val timeFile: String? = null
)

@Component
data class RootGiveFilesServer (
    @SerializedName(Vars.jsonServerGiveServer) var serverGive: ArrayList<ServerGive> = arrayListOf()
)

@Component
class CreatorJsonGiveFileServer() {
    fun start(rootGiveFilesServer: RootGiveFilesServer): String {
        val gson = Gson()
        return gson.toJson(rootGiveFilesServer)
    }
}