package services

import model.Indicator
import java.io.IOException
import java.net.Socket

class ProxyService(
    private val logWriter: LogWriter,
    private val sendReceive: SendReceiveService = SendReceiveService(logWriter)
) {
    var serverLog = true
    var clientLog = true
    var running = false
    private lateinit var localConnection: Socket
    private lateinit var remoteConnection: Socket

    fun setConnections(localPort: Int, remotePort: Int, remoteAddress: String) {

        if (localPort == 0 || remotePort == 0) {
            throw IOException("Ports must not be null")
        }
        localConnection = SocketConfig().openLocalConnection(localPort)
        remoteConnection = SocketConfig().openRemoteConnection(remoteAddress, remotePort)

        runProxy(localConnection = localConnection, remoteConnection = remoteConnection)

    }

    private fun runProxy(localConnection: Socket, remoteConnection: Socket) {


        do {
            // --- Server Server ( recebe pacotes servidor )
            val serverPackets =
                sendReceive.receive(
                    connectServer = remoteConnection,
                    indicator = Indicator.Server,
                    log = serverLog
                )

            //serverPackets = filter(serverPackets)

            // --- Server Client
            sendReceive.send(
                connectServer = localConnection,
                packetToSend = serverPackets,
                indicator = Indicator.Client,
                log = false
            )

            // --- Client Server
            val clientPackets = sendReceive.receive(
                connectServer = localConnection,
                indicator = Indicator.Client,
                log = clientLog
            )

            // --- Server Server ( envia pacotes servidor )
            sendReceive.send(
                connectServer = remoteConnection,
                packetToSend = clientPackets,
                indicator = Indicator.Server,
                log = false
            )

        } while (running)

        if (!running) {
            localConnection.close()
            remoteConnection.close()
//            fire(StatusTextService(false))
        }

//        fire(SystemTextService("[System] Disconnected from the server"))

    }

    fun sendPacket2Server(packet: String, packetNumber: Long) {
        sendReceive.send(
            connectServer = remoteConnection,
            packetToSend = packet,
            indicator = Indicator.iServer,
            log = serverLog,
            iPacketNumber = packetNumber
        )
    }

    fun sendPacket2Client(packet: String, packetNumber: Long) {
        sendReceive.send(
            connectServer = localConnection,
            packetToSend = packet,
            indicator = Indicator.iClient,
            log = clientLog,
            iPacketNumber = packetNumber
        )
    }
}