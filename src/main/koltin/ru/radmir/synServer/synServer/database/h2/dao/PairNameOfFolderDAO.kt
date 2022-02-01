package ru.radmir.synServer.synServer.database.h2.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import ru.radmir.synServer.synServer.database.h2.entity.PairNameOfFolder
import org.springframework.stereotype.Repository
import ru.radmir.synServer.synServer.init.Vars

@Repository
interface PairNameOfFolderDAO: JpaRepository<PairNameOfFolder?, Long?> {
    @Query("SELECT * FROM ${Vars.h2PairNameOfFolderTableName} " +
            "WHERE ${Vars.h2PairNameOfFolderColumnFirstName} = :name OR ${Vars.h2PairNameOfFolderColumnLastName} = :name",
        nativeQuery = true)
    fun getByName(@Param(Vars.h2QueryPairNameOfFolderDAOParamName) name: String): List<PairNameOfFolder>
}