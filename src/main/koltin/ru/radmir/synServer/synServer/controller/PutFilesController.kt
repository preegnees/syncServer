package ru.radmir.synServer.synServer.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import ru.radmir.synServer.synServer.controller.json.CreatorJsonPutFiles
import ru.radmir.synServer.synServer.database.h2.entity.PairNameOfFolder
import ru.radmir.synServer.synServer.database.h2.service.PairNameOfFolderService
import ru.radmir.synServer.synServer.database.swaydb.Swaydb
import ru.radmir.synServer.synServer.init.DetectorOfFiles
import ru.radmir.synServer.synServer.init.Init
import java.io.File
import java.nio.file.Paths
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest
import kotlin.io.path.createDirectories

@Controller
@ResponseBody
class PutFilesController {

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

    @PostMapping("/put_files", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun putFiles(request: HttpServletRequest): String {
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

        val newFiles = CreatorJsonPutFiles().start(json)
        lateinit var namesFolders: MutableList<PairNameOfFolder?>
        var desiredFolder: String
        var count = 0
        val root = "files" + File.separator
        try {
            // проверить что name и pair_name будут равны
            for (f in newFiles.client){
                if (!f.name.equals(f.pairName)) {
                    namesFolders = folderService.getFolder(f.name!!)
                    // нужно запретить указывать одинаковое name и pairName
                    for (j in namesFolders) {
                        if (f.pairName == j?.getLastName() || f.pairName == j?.getFirstName()) {
                            count++
                            if (count == 1) {
                                desiredFolder = j?.getFirstName() + "_" + j?.getLastName()
                                // получить все файлы и сравнить с нынешним, что бы не было совпадений
                                val files = File(root + desiredFolder).listFiles()
                                if (files!!.isNotEmpty()) {
                                    val x = File(root + desiredFolder + File.separator + f.fileName)
                                    if (!x.exists()) {
                                        x.createNewFile()
                                        x.writeText(f.contentOfFile!!)
                                        // detectorOfFiles.start()
                                    }
                                } else {
                                    count--
                                }
                            }
                        }
                    }
                }
                if (count == 0 || f.name.equals(f.pairName)) {
                    desiredFolder = f.name + "_" + f.pairName
                    Paths.get(root + desiredFolder).createDirectories()
                    val x = File(root + desiredFolder + File.separator +f.fileName)
                    if (!x.exists()) {
                        x.createNewFile()
                        x.writeText(f.contentOfFile!!)
                    }
                    detectorOfFiles.start()
                } else {
                    // тут может начаться бесконечная петля, потому что мы создали новый файл с новым имененм
                    // и потом мы его вернем на сервер, наврное лучше на сервере прибалвлять дату
                    detectorOfFiles.start()
                    throw Exception("неизвестная ошибка")
                }
            }
        } catch (e: Exception){
            detectorOfFiles.start()
        }
        detectorOfFiles.start()
        return "ok"
    }
}