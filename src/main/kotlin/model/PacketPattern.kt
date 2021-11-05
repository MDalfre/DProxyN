package model

data class PacketPattern(
    val header: String,
    val code: String,
    val length: String? = null,
    val subCode: String? = null
)
