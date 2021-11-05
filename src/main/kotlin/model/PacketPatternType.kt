package model

import androidx.compose.ui.text.toUpperCase
import logWriterService
import proxyService
import services.CommunicationService
import services.LogWriterService
import services.ProxyService
import java.util.Locale

enum class PacketPatternTypeClient(val pattern: PacketPattern) {
    PING(
        pattern = PacketPattern("C3", "0E", null, "00")
    ),
    LOGIN(
        pattern = PacketPattern("C3", "F1", null, "01")
    ),
    WHISPER(
        pattern = PacketPattern("C1", "02")
    ),
    CHAT(
        pattern = PacketPattern("C1", "00")
    )
}

var monsterlist = mutableListOf<String>()

class PacketPatternTypeTest {

    val list: Map<PacketPattern, PacketPatternTypeClient> = mapOf(
        PacketPattern("C3", "0E") to PacketPatternTypeClient.PING,
        PacketPattern("C3", "F1") to PacketPatternTypeClient.LOGIN,
        PacketPattern("C1", "02") to PacketPatternTypeClient.WHISPER,
        PacketPattern("C1", "00") to PacketPatternTypeClient.CHAT
    )

    fun readProcessor(packet: List<String>, from: Indicator) {
        if (from == Indicator.Client) {
            when (PacketPattern(packet[0], packet[2])) {
                PacketPattern("C1", "00") -> readChat(packet)
                PacketPattern("C3", "F1") -> processLogin(packet)
                PacketPattern("C1", "F3") -> processCharacterScreen(packet)
            }

            when (PacketPattern(packet[0], packet[1], packet[2])) {
                PacketPattern("C1", "07", "11") -> hitKill(packet, from)
            }
        } else if (from == Indicator.Server) {
            when (PacketPattern(packet[0], packet[2])) {
                PacketPattern("C1", "D4") -> objectMoved(packet)
            }
        }
    }



    fun hitKill(packet: List<String>, from: Indicator) {
        println("Possible target found")
        if (from == Indicator.Client) {
            val packetClean = packet.filter { it.isNotEmpty() }

            if ("${packet[3]},${packet[4]}" !in monsterlist) {
                println("Monster ${packet[3]} , ${packet[4]} Added to list")
                monsterlist.add("${packet[3]},${packet[4]}")
            }

            if (packetClean.size == 7) {
                println("Found enemy ! id: ${packetClean[3]} ${packetClean[4]}")
                proxyService.sendPacket2Server(packetGenerator(packetClean), 50, 20, false)
            }
        }

    }

    fun objectMoved(packet: List<String>) {
        if (packet.size >= 10 && packet[3] != "02" && packet[4] != "00") {
            val objectId1 = packet[3].toInt(16)
            val objectId2 = packet[4].toInt(16)
            val positionX = packet[5]
            val positionY = packet[6]

            println("-----------")
            println("Object: $objectId1,$objectId2 moved to $positionX, $positionY")
            println(monsterlist)

            if (monsterlist.size > 0) {
                println("Killing ${monsterlist.size} enemies")
                monsterlist.forEach {
                    val monsterId = it.split(",")
                    val newPacket = listOf(
                        "C1",
                        "07",
                        "11",
                        monsterId[0],
                        monsterId[1],
                        "25",
                        "E2"
                    )
                    hitKill(newPacket, Indicator.Client)
                }
            }

//            val newPacket = listOf(
//                "C1",
//                "07",
//                "11",
//                monsterlist[0],
//                monsterlist[1],
//                "25",
//                "E2"
//            )
//
//            println("CreatedPacket -> $newPacket")
//            println("------------")
//            hitKill(newPacket, Indicator.Client)
        }
    }


    private fun packetGenerator(packet: List<String>): String {
        val packetString = java.lang.StringBuilder("")
        packet.forEach {
            packetString.append("$it ")
        }

        val result = packetString.toString()
        println(result)
        return result
    }

    fun processLogin(packet: List<String>) {
        val userName = packet.subList(4, 10)
        val password = packet.subList(14, 20)
        println(
            """
            [Login]
            [raw Username] ${userName.joinToString(" ").toByte()}
            [raw Password] ${password.joinToString(" ").toByte()}
            [Username] ${decryptStringWithXORFromHex(userName, 0x20)}
            [Password] ${decryptStringWithXORFromHex(password, 0x20)}
        """.trimIndent()
        )
        println("Username: $userName")
        println("Password: $password")

    }

    private fun readChat(packet: List<String>) {
        if (packet.size > 60) {
            val from = packet.subList(3, 12).checkForEmptyChar()
            val message = packet.subList(13, packet.size).checkForEmptyChar()
            val decFrom = decryptStringFromHex(from)
            val decMessage = decryptStringFromHex(message)
            println(
                """
            [Chat Message]
            [From] $decFrom
            [Message] $decMessage
        """.trimIndent()
            )
        }
    }

    fun processCharacterScreen(packet: List<String>) {
        val packetCorrected = packet.filter { it.isNotEmpty() }
        val allowedChars = ('a'..'z').plus('A'..'Z').plus('0'..'9')
        val names = java.lang.StringBuilder("")
        PacketPatternTypeTest().decryptStringFromHex(packetCorrected).toCharArray().forEach {
            if (it in allowedChars) {
                names.append(it)
            } else {
                names.append(',')
            }
        }
        val characterList = names.split(",").filter { it.isNotEmpty() }
        println("[Found: ${characterList.size} characters in account ]")
        characterList.forEach { charName ->
            println(charName)
        }
        println("[Character list end]")
    }

    fun decryptStringWithXORFromHex(input: List<String>, xorKey: Int): String {
        val output = StringBuilder("")

        input.forEach { packet ->
            val value1 = packet.toInt(16)
            val value2 = xorKey
            val xorValue = value1 xor value2
            output.append(xorValue.toChar().toString())
        }

        return output.toString()
    }

    fun decryptStringFromHex(input: List<String>): String {
        val output = StringBuilder("")

        input.forEach { packet ->
            val toAppend = packet.toInt(16).toChar()
            val allowedCharList = listOf(' ', '_', '@', '&', '*', '%')

            if (toAppend.isLetterOrDigit() || toAppend in allowedCharList) {
                output.append(toAppend)
            }
        }
        return output.toString()
    }

    private fun List<String>.checkForEmptyChar(): List<String> {
        return this.dropLastWhile { it != "00" }
    }
}