package ru.radmir.synServer.synServer.init

import org.springframework.stereotype.Component

@Component
class Vars {
    companion object {
        //      config
        const val configFileName = "config.json"
        const val configRootDirectory = "files"
        //      config errors
        const val configErrorsReadConfigFailed = "error read config.json failed enter password, \n" +
                "ошибка чтения config.json введи пароль"
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //      files
        const val filesDelimiterBetweenPathAndName = "&&&"
        const val filesUnderscore = "_"
        //      files errors
        const val filesErrorsIncomprehensibleFile = "incomprehensible file, \n" + "неопнятный файл"
        const val filesErrorsIncomprehensibleDir = "incomprehensible directory, \n" + "непонтная директория"
        const val filesErrorsThereIsAForeignFile = "there is a foreign file, \n" + "тут есть посторонний файл"
        const val filesErrorsWrongDirectoryName = "wrong directory name, \n" + "неправильное имя диреткории"
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //      other
        const val otherEmpty = ""
        const val otherSaveFileSizeOfFile = "sizeOfFile_"
        const val otherSaveTimeUpdateOfFile = "timeUpdateOfFile_"
        const val otherSaveContentOfFile = "contentOfFile_"
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //      crypto
        const val cryptoMethodCipher = "AES"
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //      databaseH2
        const val h2PairNameOfFolderTableName = "pair_name_of_folder"
        const val h2PairNameOfFolderColumnFirstName = "first_name"
        const val h2PairNameOfFolderColumnLastName = "last_name"
        const val h2PairNameOfFileTableName = "pair_name_of_folder_and_name_file"
        const val h2PairNameOfFileColumnNameDir = "name_dir"
        const val h2PairNameOfFileColumnNameFile = "name_file"
        //      databaseH2 Query
        const val h2QueryPairNameOfFileDAOParamName = "name"
        const val h2QueryPairNameOfFolderDAOParamName = "name"
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //      json
        //      json give
        const val jsonNameDirGiveClient = "name_dir"
        const val jsonNameFileGiveClient = "name_file"
        const val jsonClientGiveClient = "client"
        const val jsonServerGiveServer = "server"
        const val jsonPairNameGiveServer = "pair_name"
        const val jsonFileNameGiveServer = "file_name"
        const val jsonContentOfFileGiveServer = "content_of_file"
        const val jsonSizeOfFileGiveClient = "size_file"
        const val jsonTimeOfFileGiveClient = "time_file"
        const val jsonSizeOfFileGiveServer = "size_file"
        const val jsonTimeOfFileGiveServer = "time_file"
        //      json put
        const val jsonNamePutClient = "name"
        const val jsonPairNamePutClient = "pair_name"
        const val jsonClientPutClient = "client"
        const val jsonFileNamePutClient = "file_name"
        const val jsonContentOfFilePutClient = "content_of_file"
        const val jsonSizeOfFilePutClient = "size_file"
        const val jsonTimeOfFilePutClient = "time_file"
        //      json update
        const val jsonNameDirUpdate = "name_dir"
        const val jsonFileNameUpdate = "name_file"
        const val jsonSizeOfFileUpdate = "size_file"
        const val jsonTimeOfFileUpdate = "time_file"
        const val jsonServerUpdate = "server"
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //      workers with net
        const val netServerResponseOk = "<<100>>" // все хорошо
        const val netServerResponseUsernameAlreadyTaken = "<<-100>>" // имя пользовтеля уже занято
        const val netServerResponseNoUpdates = "<<-200>>" // не было обновлений на сервере
        const val netLinkCheckUpdateFiles = "check_update_files"
        const val netLinkGiveFiles = "give_files"
        const val netLinkPutFiles = "put_files"
        const val netLinkPutRequestParameterName = "name"
        const val netLinkGiveRequestParameterName = "name"
        const val netLinkUpdateRequestParameterName = "name"
        const val netLinkUpdateRequestParameterMe = "me"
        //       workers with net errors
        const val netErrorsUnknownError = "Unknown error"
    }
}