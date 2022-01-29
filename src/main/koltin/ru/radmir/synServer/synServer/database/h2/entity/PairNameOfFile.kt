package ru.radmir.synServer.synServer.database.h2.entity

import javax.persistence.*

@Entity
@Table(name = "pair_name_of_folder_and_name_file")
class PairNameOfFile() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

    @Column(name = "name_dir")
    private var nameDir: String? = null

    @Column(name = "name_file")
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