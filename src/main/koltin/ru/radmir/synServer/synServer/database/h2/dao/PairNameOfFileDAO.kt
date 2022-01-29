package ru.radmir.synServer.synServer.database.h2.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import ru.radmir.synServer.synServer.database.h2.entity.PairNameOfFile
import org.springframework.stereotype.Repository

@Repository
interface PairNameOfFileDAO: JpaRepository<PairNameOfFile?, Long?> {
    @Query("SELECT * FROM pair_name_of_folder_and_name_file WHERE name_dir = :name OR name_file = :name", nativeQuery = true)
    fun getByName(@Param("name") name: String?): List<PairNameOfFile?>
}