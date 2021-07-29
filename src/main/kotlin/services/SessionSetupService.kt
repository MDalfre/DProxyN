package services

import java.io.IOException
import java.net.ConnectException
import java.net.ServerSocket
import java.net.Socket

class SessionSetupService(
    private val logWriterService: LogWriterService
) {

    fun openRemoteConnection(ip: String, cnPort: Int): Socket {
        println("Remote Connection Open")
        logWriterService.systemLog("Trying to connect server at $ip:$cnPort")
        return try {
            Socket(ip, cnPort).also { logWriterService.systemLog("Successfully connected to server") }
        } catch (ex: ConnectException) {
            logWriterService.systemLog("Failed to connect: ${ex.message}")
            logWriterService.systemLog("Connection closed")
            throw ex
        }

    }

    fun openLocalConnection(clPort: Int): Socket {
        println("Local Connection Open")
        logWriterService.systemLog("Local connection open at port $clPort")
        logWriterService.systemLog("Waiting for connections ...")
        return try {
            ServerSocket(clPort).accept().also { logWriterService.systemLog("Client connected") }
        } catch (ex: IOException) {
            logWriterService.systemLog("Client connect failed : ${ex.message}")
            throw ex
        }
    }
}