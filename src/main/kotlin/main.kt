import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import components.leftSide
import components.top
import services.LogWriterService
import services.ProxyService

const val DEFAULT_WIDTH = 1300
const val DEFAULT_HEIGHT = 930
val OVER_LINE_STYLE = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 13.sp,
    letterSpacing = 1.5.sp
)
val logWriterService = LogWriterService()

fun main() = Window(
    title = "DProxyN - Sniffer Tunneling",
    size = IntSize(DEFAULT_WIDTH, DEFAULT_HEIGHT),
    resizable = false
) {
    val proxyService = ProxyService(logWriterService)

    MaterialTheme {


        Row {
            leftSide(
                modifier = Modifier,
                proxyService = proxyService,
                logWriterService = logWriterService
            )
        }
        Column {
            top()
        }
    }
}