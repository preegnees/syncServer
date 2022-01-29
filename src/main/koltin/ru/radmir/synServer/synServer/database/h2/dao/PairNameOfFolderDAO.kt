package ru.radmir.synServer.synServer.database.h2.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import ru.radmir.synServer.synServer.database.h2.entity.PairNameOfFolder
import org.springframework.stereotype.Repository

@Repository
interface PairNameOfFolderDAO: JpaRepository<PairNameOfFolder?, Long?> {
    @Query("SELECT * FROM pair_name_of_folder WHERE first_name = :name OR last_name = :name", nativeQuery = true)
    fun getByName(@Param("name") name: String): List<PairNameOfFolder>
}