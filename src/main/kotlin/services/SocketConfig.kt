package services

import java.net.ServerSocket
import java.net.Socket

class SocketConfig {

    fun openRemoteConnection(ip: String, cnPort: Int): Socket {
        println("Remote Connection Open")
        return Socket(ip, cnPort)
    }

    fun openLocalConnection(clPort: Int): Socket {
        println("Local Connection Open")
        return ServerSocket(clPort).accept()
    }
}