package ru.radmir.synServer.synServer.init

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import ru.radmir.synServer.synServer.database.h2.service.PairNameOfFileService
import ru.radmir.synServer.synServer.database.h2.service.PairNameOfFolderService
import java.io.File
import javax.sound.sampled.Line

@Component
class DetectorOfFiles() {
    @Autowired
    private lateinit var storageFolder: PairNameOfFolderService
    @Autowired
    private lateinit var storageFile: PairNameOfFileService

    private val root = Vars.configRootDirectory

    fun start(){
        File(root).mkdir() // создание директории если она не создана
        if (checkFileOnError()){ // проверка на посторонние файлы и неправильные директории
            checkFiles(root)
        }
    }

    private fun checkFileOnError(): Boolean {
        val folder = File(root)
        val listOfFolder = folder.listFiles()
        if (!listOfFolder.isNullOrEmpty()) {
            for (f in listOfFolder) {
                if (!f.isDirectory) {
                    throw Exception("${Vars.filesErrorsThereIsAForeignFile}: $f")
                }
                if (f.name.split(Vars.filesUnderscore).size != 2) {
                    throw Exception("${Vars.filesErrorsWrongDirectoryName}: $f")
                }
            }
        }
        return true
    }

    private fun checkFiles(nameDir: String){

        val folder = File(nameDir)
        val listOfFiles = folder.listFiles()
        var isAdded = false

        if (!listOfFiles.isNullOrEmpty()) {
            for (f in listOfFiles){
                if (f.isDirectory){
                    lateinit var firstName: String
                    lateinit var lastName: String
                    try {
                        firstName = f.name.split(Vars.filesUnderscore)[0]
                        lastName = f.name.split(Vars.filesUnderscore)[1]
                    } catch (e: Exception){
                        throw Exception("${Vars.filesErrorsIncomprehensibleDir}: ${f.name}")
                    }

                    val namesOfFolder = storageFolder.getFolder(firstName)
                    if (namesOfFolder.isNotEmpty()){
                        for (i in namesOfFolder) {
                            if (i?.getFirstName() == firstName && i.getLastName() == lastName){
                                isAdded = true
                                break
                            } else {
                                isAdded = false
                            }
                        }
                    } else {
                        isAdded = true
                        storageFolder.setFolder(firstName, lastName)
                    }
                    if (!isAdded) {
                        storageFolder.setFolder(firstName, lastName)
                    }

                    val newDir = nameDir + File.separator + f.name
                    checkFiles(newDir)
                    continue
                }

                if (f.isFile &&
                    f.name.split(Vars.filesDelimiterBetweenPathAndName).size == 2) {
                    val tempFolder = nameDir.split(File.separator).last()
                    val tempFile = f.name
                    isAdded = false

                    if (storageFile.getFile(tempFolder).isNotEmpty()){
                        for (i in storageFile.getFile(tempFile)) {
                            if (i?.getNameDir().equals(tempFolder) &&
                                i?.getNameFile().equals(tempFile)){
                                isAdded = true
                                break
                            }
                        }
                    } else {
                        isAdded = true
                        storageFile.setFile(tempFolder, tempFile)
                    }
                    if (!isAdded) {
                        storageFile.setFile(tempFolder, tempFile)
                    }
                } else{
                    throw Exception("${Vars.filesErrorsIncomprehensibleFile}: ${f.name}")
                }
            }
        }
    }
}