package services

import model.Log


class LogWriter {

    var logList = mutableListOf<Log>()
    var systemLogList = mutableListOf<String>()

    fun log(index: Long, type: String, message: String) {
        logList.add(
            Log(index = index, type = type, message = message)
        )
        systemLogList.add("[$index] [$type] -> $message")
    }
}