package ru.radmir.synServer.synServer.init

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Init() {

    @Autowired
    private lateinit var detectorOfFiles:  DetectorOfFiles

    fun start(){
        detectorOfFiles.start()
    }
}