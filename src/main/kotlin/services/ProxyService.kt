package services

import model.Indicator
import java.io.IOException
import java.net.Socket

class ProxyService(
    private val logWriterService: LogWriterService,
    private val communicationService: CommunicationService = CommunicationService(logWriterService)
) {
    var serverLog = true
    var clientLog = true
    var running = false
    private lateinit var localConnection: Socket
    private lateinit var remoteConnection: Socket

    fun setConnections(localPort: Int, remotePort: Int, remoteAddress: String) {

        if (localPort == 0 || remotePort == 0) {
            logWriterService.systemLog("Fail to start: Ports must not be null")
            throw IOException("Ports must not be null")
        }
        localConnection = SessionSetupService(logWriterService).openLocalConnection(localPort)
        remoteConnection = SessionSetupService(logWriterService).openRemoteConnection(remoteAddress, remotePort)

        runProxy(localConnection = localConnection, remoteConnection = remoteConnection)

    }

    private fun runProxy(localConnection: Socket, remoteConnection: Socket) {


        do {
            // --- Server Server ( receive packets from server )
            val serverPackets =
                communicationService.receive(
                    connectServer = remoteConnection,
                    indicator = Indicator.Server,
                    log = serverLog
                )

            //serverPackets = filter(serverPackets)

            // --- Server Client
            communicationService.send(
                connectServer = localConnection,
                packetToSend = serverPackets,
                indicator = Indicator.Client,
                log = false
            )

            // --- Client Server
            val clientPackets = communicationService.receive(
                connectServer = localConnection,
                indicator = Indicator.Client,
                log = clientLog
            )

            // --- Server Server ( send packets to server )
            communicationService.send(
                connectServer = remoteConnection,
                packetToSend = clientPackets,
                indicator = Indicator.Server,
                log = false
            )

        } while (running)

        if (!running) {
            localConnection.close()
            remoteConnection.close()
        }
        logWriterService.systemLog("Disconnected from the server")
    }

    fun sendPacket2Server(packet: String) {
        try {
            communicationService.send(
                connectServer = remoteConnection,
                packetToSend = packet,
                indicator = Indicator.iServer,
                log = serverLog,
                iPacketNumber = ++communicationService.packetNumber
            )
        } catch (ex: Exception) {
            logWriterService.systemLog("Failed to inject: ${ex.message}")
        }
    }

    fun sendPacket2Client(packet: String) {
        try {
            communicationService.send(
                connectServer = localConnection,
                packetToSend = packet,
                indicator = Indicator.iClient,
                log = clientLog,
                iPacketNumber = ++communicationService.packetNumber
            )
        } catch (ex: Exception) {
            logWriterService.systemLog("Failed to inject: ${ex.message}")
        }
    }
}