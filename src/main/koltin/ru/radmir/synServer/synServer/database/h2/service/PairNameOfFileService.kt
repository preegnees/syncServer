package ru.radmir.synServer.synServer.database.h2.service

import ru.radmir.synServer.synServer.database.h2.dao.PairNameOfFileDAO
import ru.radmir.synServer.synServer.database.h2.entity.PairNameOfFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
class PairNameOfFileService() {

    @Autowired
    private lateinit var pairNameOfFileDAO: PairNameOfFileDAO

    fun setFile(nameDir: String, nameFile: String) {
        val pairNameFile = PairNameOfFile()
        pairNameFile.setNameFile(nameFile)
        pairNameFile.setNameDir(nameDir)
        add(pairNameFile)
    }

    fun getFile(name: String): MutableList<PairNameOfFile?> {
        val names = mutableListOf<PairNameOfFile?>()
        for (i in getByName(name)){
            names.add(i)
        }
        return names
    }

    private fun add(name: PairNameOfFile) {
        pairNameOfFileDAO.save(name)
    }

    private fun getByName(name: String): List<PairNameOfFile?> {
        return pairNameOfFileDAO.getByName(name)
    }

    private fun getAll(): List<PairNameOfFile?>? {
        TODO("Not yet implemented")
    }

    private fun update(name: PairNameOfFile?) {
        TODO("Not yet implemented")
    }

    private fun remove(name: PairNameOfFile?) {
        TODO("Not yet implemented")
    }
}