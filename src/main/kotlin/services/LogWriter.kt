package services

import java.io.File
import java.time.LocalDate
import java.util.*

class LogWriter {
    val file = File("log-${LocalDate.now()}-${UUID.randomUUID()}")

    init {
        if (!file.exists()){
            log("asd","123123")
        }
    }

    fun log(type: String, message: String) {
        file.printWriter().use {
            it.println("$type , $message")
            it.flush()
        }
    }

    fun logRead(){
        file.readLines().forEach {
            println(it)
        }
    }
}