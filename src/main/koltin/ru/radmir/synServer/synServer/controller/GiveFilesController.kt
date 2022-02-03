package ru.radmir.synServer.synServer.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import ru.radmir.synServer.synServer.controller.json.*
import ru.radmir.synServer.synServer.database.h2.service.PairNameOfFolderService
import ru.radmir.synServer.synServer.database.swaydb.Swaydb
import ru.radmir.synServer.synServer.encryption.Cryptographer
import ru.radmir.synServer.synServer.init.DetectorOfFiles
import ru.radmir.synServer.synServer.init.Init
import ru.radmir.synServer.synServer.init.Vars
import java.io.File
import java.util.*
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest
import kotlin.collections.ArrayList

@Controller
@ResponseBody
class GiveFilesController {
    @Autowired
    private lateinit var storage: Swaydb
    @Autowired
    private lateinit var init: Init
    @Autowired
    private lateinit var cryptographer: Cryptographer

    @PostConstruct
    fun init(){
        init.start()
    }

    @PostMapping("/${Vars.netLinkGiveFiles}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun giveFiles(request: HttpServletRequest): String {
        val json = request.inputStream.readBytes().toString(Charsets.UTF_8)
//        val myNameEncrypted = request.getParameter(Vars.netLinkGiveRequestParameterName)
        val myName = request.getParameter(Vars.netLinkGiveRequestParameterName)

        // start decrypt
        // val myName = cryptographer.decryptString(myNameEncrypted) // parameter
        val newFilesFromClientEncrypted = CreatorJsonGiveFilesClient().start(json)
        val newFilesFromClient = arrayListOf<ClientGive>()
        for (i in newFilesFromClientEncrypted.clientGive) {
            newFilesFromClient.add(
                ClientGive(
                    nameDir = cryptographer.decryptString(i.nameDir!!),
                    nameFile = cryptographer.decryptString(i.nameFile!!),
                    sizeFile = cryptographer.decryptString(i.sizeFile!!),
                    timeFile = cryptographer.decryptString(i.timeFile!!)
                )
            )
        }
        // end decrypt

        // проверка имени пользователя на то, что оно не повторялось прежде (name, ip)
        val ip = storage.get(myName)
        if (ip!!.isEmpty()) {
            storage.set(myName, request.remoteAddr)
        } else {
            if (ip != request.remoteAddr){
                return Vars.netServerResponseUsernameAlreadyTaken
            }
        }
        val listFilesForSendClient = arrayListOf<ServerGive>()
        for (i in newFilesFromClient) {
            val root = Vars.configRootDirectory + File.separator
            val temp = i.nameDir!!.split(Vars.filesUnderscore)

            val fileName = i.nameFile
            val pairName: String = if (myName == temp[0] && myName == temp[1]){
                myName
            } else {
                if (myName == temp[0]) temp[1] else temp[0]
            }
            val file = File(root + i.nameDir + File.separator + i.nameFile)
            if (!file.exists()) {
                continue
            }
            val fromFile = file.readText(Charsets.UTF_8) // file.readText()
            val sizeFile = fromFile.split(Vars.otherSaveFileSizeOfFile)[1]
            val timeFile = fromFile.split(Vars.otherSaveTimeUpdateOfFile)[1]
            val contentOfFile = fromFile.split(Vars.otherSaveContentOfFile)[1]

            listFilesForSendClient.add(
                ServerGive(
                    pairName = pairName,
                    fileName = fileName,
                    sizeFile = sizeFile,
                    timeFile = timeFile,
                    contentOfFile = contentOfFile
                )
            )
        }

        // start encrypt
        val listFilesForSendClientEncrypted = arrayListOf<ServerGive>()
        for (i in listFilesForSendClient) {
            listFilesForSendClientEncrypted.add(
                ServerGive(
                    pairName = cryptographer.encryptString(i.pairName!!),
                    fileName = cryptographer.encryptString(i.fileName!!),
                    sizeFile = cryptographer.encryptString(i.sizeFile!!),
                    timeFile = cryptographer.encryptString(i.timeFile!!),
                    contentOfFile = i.contentOfFile
                )
            )
        }
        // end encrypt
        return CreatorJsonGiveFileServer()
            .start(RootGiveFilesServer(listFilesForSendClientEncrypted))
    }
}