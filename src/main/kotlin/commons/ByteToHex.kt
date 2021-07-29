package commons

class ByteToHex {

    fun toHex(bytes: ByteArray): java.lang.StringBuilder {
        val stringBuilder = StringBuilder()
        for (b in bytes) {
            stringBuilder.append(String.format("%02X ", b))
        }
        return stringBuilder
    }
}