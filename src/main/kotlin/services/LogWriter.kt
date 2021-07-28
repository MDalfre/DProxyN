package services

import model.Log


class LogWriter {

    var logList = mutableListOf<Log>()
    var systemLogList = mutableListOf<String>()
    var packetToSend: Log? = null

    fun log(index: Long, type: String, message: String) {
        logList.add(
            Log(index = index, type = type, message = message)
        )
    }

    fun systemLog(message: String) {
        systemLogList.add(message)
    }
}