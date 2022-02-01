package ru.radmir.synServer.synServer.controller.json

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.springframework.stereotype.Component
import ru.radmir.synServer.synServer.init.Vars

@Component
data class Client (
    @SerializedName(Vars.jsonNamePutClient) val name : String? = null,
    @SerializedName(Vars.jsonPairNamePutClient) val pairName : String? = null,
    @SerializedName(Vars.jsonFileNamePutClient) val fileName : String? = null,
    @SerializedName(Vars.jsonContentOfFilePutClient) val contentOfFile : String? = null,
    @SerializedName(Vars.jsonSizeOfFilePutClient) val sizeFile: String? = null,
    @SerializedName(Vars.jsonTimeOfFilePutClient) val timeFile: String? = null
)

@Component
data class RootPutFiles (
    @SerializedName(Vars.jsonClientPutClient) var client: ArrayList<Client> = arrayListOf()
)

@Component
class CreatorJsonPutFiles() {
    fun start(json: String): RootPutFiles {
        val gson = Gson()
        return gson.fromJson(json, RootPutFiles::class.java)
    }
}