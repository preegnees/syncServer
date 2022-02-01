package ru.radmir.synServer.synServer.controller.json

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.springframework.stereotype.Component
import ru.radmir.synServer.synServer.init.Vars

@Component
data class Server (
    @SerializedName(Vars.jsonNameDirUpdate) var nameDir  : String? = null,
    @SerializedName(Vars.jsonFileNameUpdate) var nameFile : String? = null,
    @SerializedName(Vars.jsonSizeOfFileUpdate) var sizeFile: String? = null,
    @SerializedName(Vars.jsonTimeOfFileUpdate) var timeFile: String? = null
)

@Component
data class RootUpdateFiles (
    @SerializedName(Vars.jsonServerUpdate) var server : ArrayList<Server> = arrayListOf()
)

@Component
class CreatorJsonUpdatedFiles() {
    fun start(rootUpdateFiles: RootUpdateFiles): String {
        val gson = Gson()
        return gson.toJson(rootUpdateFiles)
    }
}