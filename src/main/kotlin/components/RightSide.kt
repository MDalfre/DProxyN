package components

import OVER_LINE_STYLE
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.Indicator
import model.Log
import services.LogWriter
import services.ProxyService
import kotlin.concurrent.thread

@Composable
fun rightSide(
    modifier: Modifier,
    proxyService: ProxyService,
    logWriter: LogWriter
) {
    Column(
        modifier
            .padding(start = 16.dp, end = 16.dp, top= 36.dp)
    ) {

        var packetToServer by remember { mutableStateOf(" ") }
        var packetToClient by remember { mutableStateOf(" ") }

        Spacer(
            modifier = Modifier
                .padding(16.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Server injection",
                modifier = Modifier
                    .padding(start = 2.dp, end = 8.dp),
                style = OVER_LINE_STYLE
            )
            Divider(
                color = Color.Gray,
                modifier = Modifier
                    .padding(end = 2.dp)

            )
            Spacer(
                modifier = Modifier
                    .padding(10.dp)
            )

        }

        OutlinedTextField(
            modifier = defaultPadding,
            value = packetToServer,
            singleLine = true,
            onValueChange = { value -> packetToServer = value },
            label = { Text("Send to Server") }
        )
        Button(
            modifier = defaultPadding,
            onClick = {
                logWriter.systemLog("Injecting packet to server.")
                proxyService.sendPacket2Server(packetToServer, 1L)
            }
        ) {
            Text("To server")
        }

        Spacer(
            modifier = Modifier
                .padding(10.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Client injection",
                modifier = Modifier
                    .padding(start = 2.dp, end = 8.dp),
                style = OVER_LINE_STYLE
            )
            Divider(
                color = Color.Gray,
                modifier = Modifier
                    .padding(end = 2.dp)

            )
            Spacer(
                modifier = Modifier
                    .padding(10.dp)
            )

        }

        OutlinedTextField(
            modifier = defaultPadding,
            value = packetToClient,
            singleLine = true,
            onValueChange = { value -> packetToClient = value },
            label = { Text("Send to Client") }
        )

        Button(
            modifier = defaultPadding,
            onClick = {
                logWriter.systemLog("Injecting packet to client.")
                proxyService.sendPacket2Client(packetToClient, 1L)
            }
        ) {
            Text("To client")
        }

        Row(
            modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,

            ) {

        }

        Row(
            modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,

            ) {
        }

        thread {
            do {
                if (logWriter.packetToSend == null) {
                    Thread.sleep(1000)
                } else if (logWriter.packetToSend?.type == Indicator.Server.name && logWriter.packetToSend?.message != packetToServer) {
                    packetToServer = logWriter.packetToSend!!.message
                    Thread.sleep(1000)
                } else if (logWriter.packetToSend?.type == Indicator.Client.name && logWriter.packetToSend?.message != packetToServer) {
                    packetToClient = logWriter.packetToSend!!.message
                    Thread.sleep(1000)
                }
            } while (true)
        }

    }
}