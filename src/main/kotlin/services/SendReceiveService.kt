package services

import commons.ByteToHex
import model.Indicator
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket


class SendReceiveService(
    private val logWriter: LogWriter
) {

    private var byteToHex = ByteToHex()
    var packetNumber: Long = 0

    fun receive(
        connectServer: Socket,
        indicator: Indicator,
        log: Boolean
    ): String {

        while (connectServer.isConnected) {

            val serverIn: InputStream = connectServer.getInputStream()
            if (serverIn.available() == 0) {
                break
            }
            val read = ByteArray(8024)
            val length: Int = serverIn.read(read)
            val byteArrayOutputStream = ByteArrayOutputStream()
            byteArrayOutputStream.write(read, 0, length)

            val readBytes = byteArrayOutputStream.toByteArray()
            val hexString = byteToHex.toHex(readBytes)
            packetNumber++
            when (log) {
                true -> logWriter.log(packetNumber, indicator.name, hexString.toString())
            }
            return hexString.toString()
        }

        return ""

    }

    fun send(
        connectServer: Socket,
        packetToSend: String,
        indicator: Indicator,
        log: Boolean,
        iPacketNumber: Long = 0
    ) {

        if (packetToSend != "") {
            //Regex para remover espaços; Remover espaços depois no StringBuilder do serverReceive
            val stringPacket = packetToSend.replace("\\s".toRegex(), "")

            val byteArray = ByteArray(stringPacket.length / 2)
            for (i in byteArray.indices) {
                val index = i * 2
                val j = stringPacket.substring(index, index + 2).toInt(16)
                byteArray[i] = j.toByte()
            }
            when (log) {
                true -> logWriter.log(iPacketNumber, indicator.name, packetToSend)
            }
            val serverOut: OutputStream = connectServer.getOutputStream()
            serverOut.write(byteArray)
        }

    }
}