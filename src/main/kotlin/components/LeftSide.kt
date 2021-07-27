package components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import services.ProxyService
import kotlin.concurrent.thread

val defaultPadding = Modifier.padding(8.dp)

@Composable
fun leftSide(
    modifier: Modifier,
    proxyService: ProxyService
) {
    Column(
        modifier
            .background(MaterialTheme.colors.surface)
            .padding(start = 16.dp, end = 16.dp)
            .border(color = Color.Gray, width = 2.dp)
            .background(Color.Gray)
            .fillMaxHeight()
    ) {

        var remoteAddress by remember { mutableStateOf("") }
        var remotePort by remember { mutableStateOf("") }
        var localPort by remember { mutableStateOf("") }
        var statedState by remember { mutableStateOf(false) }

        OutlinedTextField(
            modifier = defaultPadding,
            value = remoteAddress,
            singleLine = true,
            placeholder = { Text("127.0.0.1") },
            onValueChange = { value -> remoteAddress = value },
            label = { Text("Remote IP") }
        )

        OutlinedTextField(
            modifier = defaultPadding,
            value = remotePort,
            singleLine = true,
            placeholder = { Text("5050") },
            onValueChange = { value -> remotePort = value },
            label = { Text("Remote Port") }
        )

        OutlinedTextField(
            modifier = defaultPadding,
            value = localPort,
            singleLine = true,
            placeholder = { Text("5051") },
            onValueChange = { value -> localPort = value },
            label = { Text("Local Port") }
        )

        Row {
            Button(
                modifier = defaultPadding,
                enabled = !statedState,
                onClick = {
                    statedState = true
                    proxyService.running = true
                    thread {
                        proxyService.setConnections(
                            localPort = localPort.toInt(),
                            remotePort = remotePort.toInt(),
                            remoteAddress = remoteAddress
                        )
                    }
                }
            ) {
                Text("Start Proxy")
            }

            Button(
                modifier = defaultPadding,
                enabled = statedState,
                onClick = {
                    statedState = false
                    proxyService.running = false
                }
            ) {
                Text("Stop Proxy")
            }

        }

    }
}
