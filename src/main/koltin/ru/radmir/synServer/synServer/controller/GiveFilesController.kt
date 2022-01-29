package ru.radmir.synServer.synServer.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import ru.radmir.synServer.synServer.controller.json.CreatorJsonGiveFileServer
import ru.radmir.synServer.synServer.controller.json.CreatorJsonGiveFilesClient
import ru.radmir.synServer.synServer.controller.json.RootGiveFilesServer
import ru.radmir.synServer.synServer.controller.json.ServerGive
import ru.radmir.synServer.synServer.database.h2.service.PairNameOfFolderService
import ru.radmir.synServer.synServer.database.swaydb.Swaydb
import ru.radmir.synServer.synServer.init.DetectorOfFiles
import ru.radmir.synServer.synServer.init.Init
import java.io.File
import java.util.*
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest
import kotlin.collections.ArrayList

@Controller
@ResponseBody
class GiveFilesController {
    @Autowired
    private lateinit var folderService: PairNameOfFolderService
    @Autowired
    private lateinit var detectorOfFiles: DetectorOfFiles
    @Autowired
    private lateinit var storageNameIp: Swaydb
    @Autowired
    private lateinit var init: Init

    @PostConstruct
    fun init(){
        init.start()
    }

    @PostMapping("/give_files", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun giveFiles(request: HttpServletRequest): String {
        val json = request.inputStream.readBytes().toString(Charsets.UTF_8)

        // проверка имени пользователя на то, что оно не повторялось прежде (name, ip)
        val myName = request.getParameter("name")
        val ip = storageNameIp.getHash(myName)
        if (ip!!.isEmpty()) {
            storageNameIp.setHash(myName, request.remoteAddr)
        } else {
            if (ip != request.remoteAddr){
                return "-1"
            }
        }
        val listFilesForSendClient = mutableListOf<ServerGive>()
        val newFilesFromClient = CreatorJsonGiveFilesClient().start(json)
        for (i in newFilesFromClient.clientGive) {
            val root = "files" + File.separator
            val temp = i.nameDir!!.split("_")

            // это все нужно зашифровать !!
            val fileName = i.nameFile
            val pairName: String = if (myName == temp[0] && myName == temp[1]){
                myName
            } else {
                if (myName == temp[0]) temp[1] else temp[0]
            }
            val file = File(root + i.nameDir + File.separator + i.nameFile)
            val contentOfFile = Base64.getEncoder().encodeToString(file.readBytes())
            listFilesForSendClient
                .add(ServerGive(pairName = pairName, fileName = fileName, contentOfFile = contentOfFile))
        }
        return CreatorJsonGiveFileServer()
            .start(RootGiveFilesServer(listFilesForSendClient as ArrayList<ServerGive>))
    }
}