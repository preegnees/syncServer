package ru.radmir.synServer.synServer.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import ru.radmir.synServer.synServer.controller.json.Client
import ru.radmir.synServer.synServer.controller.json.CreatorJsonPutFiles
import ru.radmir.synServer.synServer.database.h2.entity.PairNameOfFolder
import ru.radmir.synServer.synServer.database.h2.service.PairNameOfFolderService
import ru.radmir.synServer.synServer.database.swaydb.Swaydb
import ru.radmir.synServer.synServer.encryption.Cryptographer
import ru.radmir.synServer.synServer.init.DetectorOfFiles
import ru.radmir.synServer.synServer.init.Init
import ru.radmir.synServer.synServer.init.Vars
import java.io.File
import java.nio.file.Paths
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest

@Controller
@ResponseBody
class PutFilesController {
    @Autowired
    private lateinit var folderService: PairNameOfFolderService
    @Autowired
    private lateinit var detectorOfFiles: DetectorOfFiles
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

    @PostMapping("/${Vars.netLinkPutFiles}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun putFiles(request: HttpServletRequest): String {
        val json = request.inputStream.readBytes().toString(Charsets.UTF_8)
        val myName = request.getParameter(Vars.netLinkPutRequestParameterName)

        // start decrypt
        // val myNameEncrypted = request.getParameter(Vars.netLinkPutRequestParameterName)
        // val myName = cryptographer.decryptString(myNameEncrypted)
        val newFilesEncrypted = CreatorJsonPutFiles().start(json)
        val newFiles = arrayListOf<Client>()
        for (i in newFilesEncrypted.client) {
            newFiles.add(
                Client(
                    name = cryptographer.decryptString(i.name!!),
                    pairName = cryptographer.decryptString(i.pairName!!),
                    fileName = cryptographer.decryptString(i.fileName!!),
                    sizeFile = cryptographer.decryptString(i.sizeFile!!),
                    timeFile = cryptographer.decryptString(i.timeFile!!),
                    contentOfFile = i.contentOfFile!!
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

        lateinit var namesFolders: MutableList<PairNameOfFolder?>
        var desiredFolder: String
        var count = 0
        val root = Vars.configRootDirectory + File.separator
        try {
            for (f in newFiles){
                if (!f.name.equals(f.pairName)) {
                    namesFolders = folderService.getFolder(f.name!!)
                    for (j in namesFolders) {
                        if (f.pairName == j?.getLastName() || f.pairName == j?.getFirstName()) {
                            count++
                            if (count == 1) {
                                desiredFolder = j?.getFirstName() + Vars.filesUnderscore + j?.getLastName()
                                // получить все файлы и сравнить с нынешним, что бы не было совпадений
                                val files = File(root + desiredFolder).listFiles()
                                if (!files.isNullOrEmpty()) {
                                    val x = File(root + desiredFolder + File.separator + f.fileName)
                                    // вынести в метод
                                    if (!x.exists()) {
                                        x.createNewFile()
                                    }
                                    val toFile = Vars.otherSaveFileSizeOfFile + f.sizeFile + Vars.otherSaveFileSizeOfFile +
                                            Vars.otherSaveTimeUpdateOfFile + f.timeFile + Vars.otherSaveTimeUpdateOfFile +
                                            Vars.otherSaveContentOfFile + f.contentOfFile + Vars.otherSaveContentOfFile
                                    x.writeText(toFile)
                                    detectorOfFiles.start()
                                } else {
                                    count--
                                }
                            }
                        }
                    }
                }
                if (count == 0 || f.name.equals(f.pairName)) {
                    desiredFolder = f.name + Vars.filesUnderscore + f.pairName
                    File(root + desiredFolder).mkdirs() // до это было Paths.get().createdir
                    val x = File(root + desiredFolder + File.separator +f.fileName)
                    // вынести в метод
                    if (!x.exists()) {
                        x.createNewFile()
                    }
                    val toFile = Vars.otherSaveFileSizeOfFile + f.sizeFile + Vars.otherSaveFileSizeOfFile +
                            Vars.otherSaveTimeUpdateOfFile + f.timeFile + Vars.otherSaveTimeUpdateOfFile +
                            Vars.otherSaveContentOfFile + f.contentOfFile + Vars.otherSaveContentOfFile
                    x.writeText(toFile)
                    detectorOfFiles.start()
                } else {
                    detectorOfFiles.start()
                    // throw Exception(Vars.netErrorsUnknownError)
                }
            }
        } catch (e: Exception){
            detectorOfFiles.start()
        }
        detectorOfFiles.start()
        return Vars.netServerResponseOk
    }
}