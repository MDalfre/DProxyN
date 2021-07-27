import androidx.compose.desktop.Window
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

const val DEFAULT_WIDTH = 900
const val DEFAULT_HEIGHT = 500

fun main() = Window(
    title = "DProxyN",
    size = IntSize(DEFAULT_WIDTH, DEFAULT_HEIGHT),
) {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        val mainOutput = remember { mutableStateOf(TextFieldValue("0")) }
        Row {
            leftSide(
                modifier = Modifier
            )
            center(
                modifier = Modifier
            )
            rightSide(
                modifier = Modifier
            )
        }
    }
}