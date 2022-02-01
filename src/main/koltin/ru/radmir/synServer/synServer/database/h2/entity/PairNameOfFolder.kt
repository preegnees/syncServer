package ru.radmir.synServer.synServer.database.h2.entity

import ru.radmir.synServer.synServer.init.Vars
import javax.persistence.*

@Entity
@Table(name = Vars.h2PairNameOfFolderTableName)
class PairNameOfFolder() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

    @Column(name = Vars.h2PairNameOfFolderColumnFirstName)
    private var firstName: String? = null

    @Column(name = Vars.h2PairNameOfFolderColumnLastName)
    private var lastName: String? = null

    fun getFirstName() = firstName
    fun setFirstName(value: String){
        firstName = value
    }
    fun getLastName() = lastName
    fun setLastName(value: String){
        lastName = value
    }
}