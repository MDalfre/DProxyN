package services

import java.io.IOException
import java.net.ConnectException
import java.net.ServerSocket
import java.net.Socket

class SocketConfig(
    private val logWriter: LogWriter
) {

    fun openRemoteConnection(ip: String, cnPort: Int): Socket {
        println("Remote Connection Open")
        logWriter.systemLog("Trying to connect server at $ip:$cnPort")
        return try {
            Socket(ip, cnPort).also { logWriter.systemLog("Successfully connected to server") }
        } catch (ex: ConnectException) {
            logWriter.systemLog("Failed to connect: ${ex.message}")
            logWriter.systemLog("Connection closed")
            throw ex
        }

    }

    fun openLocalConnection(clPort: Int): Socket {
        println("Local Connection Open")
        logWriter.systemLog("Local connection open at port $clPort")
        logWriter.systemLog("Waiting for connections ...")
        return try {
            ServerSocket(clPort).accept().also { logWriter.systemLog("Client connected") }
        } catch (ex: IOException) {
            logWriter.systemLog("Client connect failed : ${ex.message}")
            throw ex
        }
    }
}