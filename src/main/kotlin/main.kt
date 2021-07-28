import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.center
import components.leftSide
import components.rightSide
import components.top
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import model.Log
import services.LogWriter
import services.ProxyService
import kotlin.concurrent.thread

const val DEFAULT_WIDTH = 1250
const val DEFAULT_HEIGHT = 930
val OVER_LINE_STYLE = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 13.sp,
    letterSpacing = 1.5.sp
)
val logWriter = LogWriter()

fun main() = Window(
    title = "DProxyN - Sniffer Tunneling",
    size = IntSize(DEFAULT_WIDTH, DEFAULT_HEIGHT),
    resizable = false
) {
    val proxyService = ProxyService(logWriter)

    MaterialTheme {

        Column {
            top()
        }
        Row {
            leftSide(
                modifier = Modifier,
                proxyService = proxyService,
                logWriter = logWriter
            )
            center(
                modifier = Modifier,
                logWriter = logWriter
            )
            rightSide(
                modifier = Modifier,
                proxyService = proxyService,
                logWriter = logWriter
            )
        }
    }
}