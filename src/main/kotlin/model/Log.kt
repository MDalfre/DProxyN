package model

data class Log(
    val index: Long,
    val type: String,
    val message: String
)