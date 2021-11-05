package services

import model.Log
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime


class LogWriterService {

    var logList = mutableListOf<Log>()
    var systemLogList = mutableListOf<String>()

    fun log(index: Long, type: String, message: String) {
        logList.add(
            Log(index = 1L, type = type, message = message)
        )
    }

    fun systemLog(message: String) {
        systemLogList.add(message)
    }

    fun dumpToFile() {
        val fileName = "Log-${LocalDate.now()}-${LocalDateTime.now().nano}"
        File(fileName).printWriter().use { out ->
            out.println("Index \t Sent By \t Packet")
            logList.map { out.println("[${it.index}] \t [${it.type}] \t ${it.message}") }
        }
    }
}