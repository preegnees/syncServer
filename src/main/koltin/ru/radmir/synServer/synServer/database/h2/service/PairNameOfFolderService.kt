package ru.radmir.synServer.synServer.database.h2.service

import ru.radmir.synServer.synServer.database.h2.entity.PairNameOfFolder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import ru.radmir.synServer.synServer.database.h2.dao.PairNameOfFolderDAO

@Component
class PairNameOfFolderService() {

    @Autowired
    private lateinit var pairNameOfFolderDAO: PairNameOfFolderDAO

    fun setFolder(firstName: String, lastName: String) {
        val pairNameFolder = PairNameOfFolder()
        pairNameFolder.setFirstName(firstName)
        pairNameFolder.setLastName(lastName)
        add(pairNameFolder)
    }

    fun getFolder(name: String): MutableList<PairNameOfFolder?> {
        val names = mutableListOf<PairNameOfFolder?>()
        for (i in getByName(name)){
            names.add(i)
        }
        return names
    }

    fun add(name: PairNameOfFolder) {
        pairNameOfFolderDAO.save(name)
    }

    fun getByName(name: String): List<PairNameOfFolder> {
        return pairNameOfFolderDAO.getByName(name)
    }

    fun getAll(): List<PairNameOfFolder?>? {
        TODO("Not yet implemented")
    }

    fun update(name: PairNameOfFolder?) {
        TODO("Not yet implemented")
    }

    fun remove(name: PairNameOfFolder?) {
        TODO("Not yet implemented")
    }
}