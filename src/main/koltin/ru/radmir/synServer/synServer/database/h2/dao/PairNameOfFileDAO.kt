package ru.radmir.synServer.synServer.database.h2.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import ru.radmir.synServer.synServer.database.h2.entity.PairNameOfFile
import org.springframework.stereotype.Repository
import ru.radmir.synServer.synServer.init.Vars

@Repository
interface PairNameOfFileDAO: JpaRepository<PairNameOfFile?, Long?> {
    @Query("SELECT * FROM ${Vars.h2PairNameOfFileTableName} " +
            "WHERE ${Vars.h2PairNameOfFileColumnNameDir} = :name OR ${Vars.h2PairNameOfFileColumnNameFile} = :name",
        nativeQuery = true)
    fun getByName(@Param(Vars.h2QueryPairNameOfFileDAOParamName) name: String?): List<PairNameOfFile?>
}