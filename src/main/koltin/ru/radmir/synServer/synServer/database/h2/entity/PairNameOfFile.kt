package ru.radmir.synServer.synServer.database.h2.entity

import ru.radmir.synServer.synServer.init.Vars
import javax.persistence.*

@Entity
@Table(name = Vars.h2PairNameOfFileTableName)
class PairNameOfFile() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

    @Column(name = Vars.h2PairNameOfFileColumnNameDir)
    private var nameDir: String? = null

    @Column(name = Vars.h2PairNameOfFileColumnNameFile)
    private var nameFile: String? = null

    fun getNameDir() = nameDir
    fun setNameDir(value: String){
        nameDir = value
    }
    fun getNameFile() = nameFile
    fun setNameFile(value: String){
        nameFile = value
    }
}