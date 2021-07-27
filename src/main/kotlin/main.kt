import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntSize
import components.center
import components.leftSide
import components.rightSide
import components.top
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import services.LogWriter
import services.ProxyService
import kotlin.concurrent.thread

const val DEFAULT_WIDTH = 1200
const val DEFAULT_HEIGHT = 800
val logWriter = LogWriter()

fun main() = Window(
    title = "DProxyN",
    size = IntSize(DEFAULT_WIDTH, DEFAULT_HEIGHT),
) {

    val proxyService = ProxyService(logWriter)


    MaterialTheme {
        val mainOutput = remember { mutableStateOf(TextFieldValue("0")) }
        Column {
            top(modifier = Modifier)
        }
        Row {
            leftSide(
                modifier = Modifier,
                proxyService = proxyService
            )
            center(
                modifier = Modifier,
                logWriter = logWriter
            )
            rightSide(
                modifier = Modifier
            )
        }
    }
}