package ru.radmir.synServer.synServer.database.h2.entity

import javax.persistence.*

@Entity
@Table(name = "pair_name_of_folder")
class PairNameOfFolder() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

    @Column(name = "first_name")
    private var firstName: String? = null

    @Column(name = "last_name")
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