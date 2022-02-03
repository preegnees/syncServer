package ru.radmir.synServer.synServer.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import ru.radmir.synServer.synServer.controller.json.CreatorJsonUpdatedFiles
import ru.radmir.synServer.synServer.controller.json.RootUpdateFiles
import ru.radmir.synServer.synServer.controller.json.Server
import ru.radmir.synServer.synServer.database.h2.entity.PairNameOfFile
import ru.radmir.synServer.synServer.database.h2.service.PairNameOfFileService
import ru.radmir.synServer.synServer.database.h2.service.PairNameOfFolderService
import ru.radmir.synServer.synServer.database.swaydb.Swaydb
import ru.radmir.synServer.synServer.encryption.Cryptographer
import ru.radmir.synServer.synServer.hashcheck.HashChecker
import ru.radmir.synServer.synServer.init.DetectorOfFiles
import ru.radmir.synServer.synServer.init.Init
import ru.radmir.synServer.synServer.init.Vars
import java.io.File
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest

@Controller
@ResponseBody
class CheckUpdateFilesController {
    @Autowired
    private lateinit var folderService: PairNameOfFolderService
    @Autowired
    private lateinit var fileService: PairNameOfFileService
    @Autowired
    private lateinit var cryptographer: Cryptographer
    @Autowired
    private lateinit var detectorOfFiles: DetectorOfFiles
    @Autowired
    private lateinit var hashChecker: HashChecker
    @Autowired
    private lateinit var storageNameIp: Swaydb
    @Autowired
    private lateinit var init: Init

    @PostConstruct
    fun init(){
        init.start()
    }

    @GetMapping("/${Vars.netLinkCheckUpdateFiles}")
    fun checkUpdateFiles(request: HttpServletRequest): String{
        // val myNameEncrypted = request.getParameter(Vars.netLinkUpdateRequestParameterName)
        val myName = request.getParameter(Vars.netLinkUpdateRequestParameterName)
        // тут можно попросить только свои фалы, это отрабатывает только тогда, когда мы указалаи только свой никнейм
        val me = request.getParameter(Vars.netLinkUpdateRequestParameterMe)

        // start decrypt
        // val myName = cryptographer.decryptString(myNameEncrypted)
        // end decrypt

        // проверка имени пользователя на то, что оно не повторялось прежде (name, ip)
        val ip = storageNameIp.get(myName)
        if (ip!!.isEmpty()) {
            storageNameIp.set(myName, request.remoteAddr)
        } else {
            if (ip != request.remoteAddr){
                return Vars.netServerResponseUsernameAlreadyTaken
            }
        }

        var json: String = Vars.otherEmpty
        try {
            val isUpdated = hashChecker.start(myName, !me.isNullOrEmpty())
            if (isUpdated){
                // тут есть ошибка, что при удалении вручную файла, он остается в базе данных
                // потом просто удалить когда к нему обратяться и не найдут
                val namesFolders = folderService.getFolder(myName)
                if (namesFolders.isEmpty()) {
                    return json
                }
                lateinit var namesFiles: MutableList<PairNameOfFile?>
                val forJsonResponse = ArrayList<Server>()
                for (i in namesFolders){
                    namesFiles = (fileService.getFile(i?.getFirstName() + Vars.filesUnderscore + i?.getLastName()))
                    for (j in namesFiles){
                        val pathFile = File("").absolutePath + File.separator +
                                Vars.configRootDirectory + File.separator +
                                j?.getNameDir()!! + File.separator +
                                j.getNameFile()!!
                        val fromFile = File(pathFile).readText(Charsets.UTF_8)
                        val sizeFile = fromFile.split(Vars.otherSaveFileSizeOfFile)[1]
                        val timeFile = fromFile.split(Vars.otherSaveTimeUpdateOfFile)[1]
                        forJsonResponse.add(
                            Server(
                                nameDir = j.getNameDir()!!,
                                nameFile = j.getNameFile()!!,
                                sizeFile = sizeFile,
                                timeFile = timeFile
                            )
                        )
                    }
                }
                // start encrypt
                val forJsonResponseEncrypted: ArrayList<Server> = arrayListOf()
                for (i in forJsonResponse) {
                    forJsonResponseEncrypted.add(
                        Server(
                            nameDir = cryptographer.encryptString(i.nameDir!!),
                            nameFile = cryptographer.encryptString(i.nameFile!!),
                            sizeFile = cryptographer.encryptString(i.sizeFile!!),
                            timeFile = cryptographer.encryptString(i.timeFile!!)
                        )
                    )
                }
                // end encrypt
                val rootUpdateFiles = RootUpdateFiles(forJsonResponseEncrypted)
                json = CreatorJsonUpdatedFiles().start(rootUpdateFiles)
            } else {
                json = Vars.netServerResponseNoUpdates
            }
        } catch (e: Exception) {
            json = Vars.netServerResponseNoUpdates
            detectorOfFiles.start()
        }
        detectorOfFiles.start()
        return json
    }
}