package services

import java.net.ServerSocket
import java.net.Socket

class SocketConfig {

    fun openRemoteConnection(ip: String, cnPort: Int): Socket {
        return Socket(ip, cnPort)
    }

    fun openLocalConnection(clPort: Int): Socket {
        return ServerSocket(clPort).accept()
    }
}