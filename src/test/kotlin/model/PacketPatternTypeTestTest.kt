package model

import org.junit.Test
import java.lang.StringBuilder


internal class PacketPatternTypeTestTest {

    @Test
    fun decryptStringWithXORFromHex() {
        val string = "C1 1A 00 74 65 73 74 30 45 6C 66 00 00 61 73 64 61 73 64 61 73 64 61 73 64 00".split(" ")
        val string2 =
            "C1 90 F3 00 00 00 04 00 00 74 65 73 74 30 44 6B 00 00 00 00 01 00 00 20 00 FF FF FF F3 00 00 00 F8 00 00 20 FF FF FF 00 00 FF 01 74 65 73 74 30 44 77 00 00 00 00 01 00 00 00 00 FF FF FF F3 00 00 00 F8 00 00 A0 FF FF FF 00 00 FF 02 74 65 73 74 30 45 6C 66 00 00 00 01 00 00 40 0F 00 AA AA A3 00 00 00 00 00 00 80 80 00 00 00 00 FF 03 74 65 73 74 30 44 6C 00 00 00 00 01 00 00 80 00 FF FF FF F3 00 00 00 F8 00 00 20 FF FF FF 00 00 FF".split(
                " "
            )
        val decyfer = PacketPatternTypeTest().decryptStringWithXORFromHex(string, 0x00)
        val result = PacketPatternTypeTest().processCharacterScreen(string2)


        println(decyfer)
        println(result)
    }
}