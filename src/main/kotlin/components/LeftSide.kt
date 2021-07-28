package components

import OVER_LINE_STYLE
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import services.LogWriter
import services.ProxyService
import kotlin.concurrent.thread

val defaultPadding = Modifier.padding(8.dp)

@Composable
fun leftSide(
    modifier: Modifier,
    proxyService: ProxyService,
    logWriter: LogWriter
) {
    Column(
        modifier
            .padding(start = 16.dp, end = 16.dp, top= 36.dp)
            .width(300.dp)
    ) {

        var remoteAddress by remember { mutableStateOf("127.0.0.1") }
        var remotePort by remember { mutableStateOf("2020") }
        var localPort by remember { mutableStateOf("2021") }
        var statedState by remember { mutableStateOf(false) }

        Spacer(modifier = Modifier
            .padding(16.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Proxy Setup",
                modifier = Modifier
                    .padding(start = 2.dp, end = 8.dp),
                style = OVER_LINE_STYLE
            )
            Divider(
                color =  Color.Gray,
                modifier = Modifier
                    .padding(end = 2.dp)

            )
            Spacer(modifier = Modifier
                .padding(10.dp)
            )

        }

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
                    logWriter.systemLog("Sending close connection signal ...")
                }
            ) {
                Text("Stop Proxy")
            }

        }

    }
}
