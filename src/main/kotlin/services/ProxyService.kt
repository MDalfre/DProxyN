package services

import model.enum.Indicator
import java.io.IOException
import java.net.Socket

class ProxyService {

    var serverLog = true
    var clientLog = true
    var running = false
    private lateinit var localConnection: Socket
    private lateinit var remoteConnection: Socket
    private val sendReceive = SendReceiveService()

    fun setConnections(localPort: Int, remotePort: Int, remoteAddress: String) {

        if (localPort == 0 || remotePort == 0) {
            throw IOException("Ports must not be null")
        }
        localConnection = SocketConfig().openLocalConnection(localPort)
        remoteConnection = SocketConfig().openRemoteConnection(remoteAddress, remotePort)

        runProxy(localConnection, remoteConnection)

    }

    private fun runProxy(localConnection: Socket, remoteConnection: Socket) {


        do {
            // --- Server Server ( recebe pacotes servidor )
            val serverPackets = sendReceive.receive(remoteConnection, Indicator.Server, serverLog)

            //serverPackets = filter(serverPackets)

            // --- Server Client
            sendReceive.send(localConnection, serverPackets, Indicator.Client, false)

            // --- Client Server
            val clientPackets = sendReceive.receive(localConnection, Indicator.Client, clientLog)

            // --- Server Server ( envia pacotes servidor )
            sendReceive.send(remoteConnection, clientPackets, Indicator.Server, false)

        } while (running)

        if (!running) {
            localConnection.close()
            remoteConnection.close()
//            fire(StatusTextService(false))
        }

//        fire(SystemTextService("[System] Disconnected from the server"))

    }

    fun sendPacket2Server(packet: String, packetNumber: Long) {
        sendReceive.send(remoteConnection, packet, Indicator.iServer, serverLog, packetNumber)
    }

    fun sendPacket2Client(packet: String, packetNumber: Long) {
        sendReceive.send(localConnection, packet, Indicator.iClient, clientLog, packetNumber)
    }
}