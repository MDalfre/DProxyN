package commons

import java.io.File
import java.io.FileInputStream
import java.util.*

const val REMOTE_ADDRESS = "RemoteAddress"
const val REMOTE_PORT = "RemotePort"
const val LOCAL_PORT = "LocalPort"

object ConfigFileHandler {
    val fileName = "config.cfg"

    fun createConfigFile(remoteAddress: String, remotePort: String, localPort: String) {
        File(fileName).printWriter().use { out ->
            out.println("$REMOTE_ADDRESS=$remoteAddress")
            out.println("$REMOTE_PORT=$remotePort")
            out.println("$LOCAL_PORT=$localPort")
        }
    }

    fun readConfigFile(): MutableList<String> {
        if (File(fileName).exists()) {
            val prop = Properties()
            FileInputStream(fileName).use {
                prop.load(it)
                prop.getProperty(REMOTE_ADDRESS)
                prop.getProperty(REMOTE_PORT)
                prop.getProperty(LOCAL_PORT)
                val remoteAddress = prop.entries.find { properties ->
                    properties.key == REMOTE_ADDRESS
                }?.value.toString()
                val remotePort = prop.entries.find { properties ->
                    properties.key == REMOTE_PORT
                }?.value.toString()
                val localPort = prop.entries.find { properties ->
                    properties.key == LOCAL_PORT
                }?.value.toString()

                return mutableListOf(remoteAddress, remotePort, localPort)
            }
        }

        return mutableListOf("127.0.0.1", "2020", "2021")
    }
}