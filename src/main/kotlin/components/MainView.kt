package components

import OVER_LINE_STYLE
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import model.Indicator
import model.Log
import services.LogWriterService
import services.ProxyService
import kotlin.concurrent.thread

val defaultPadding = Modifier.padding(8.dp)


@Composable
fun leftSide(
    modifier: Modifier,
    proxyService: ProxyService,
    logWriterService: LogWriterService
) {
    val defaultButtonColor = buttonColors(backgroundColor = Color.DarkGray, contentColor = Color.White)

    var remoteAddress by remember { mutableStateOf("127.0.0.1") }
    var remotePort by remember { mutableStateOf("2020") }
    var localPort by remember { mutableStateOf("2021") }
    var statedState by remember { mutableStateOf(false) }

    var packetLogList by mutableStateOf(listOf<Log>())
    var messageLogList by mutableStateOf(listOf<String>())
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    var packetToServer by remember { mutableStateOf(" ") }
    var packetToClient by remember { mutableStateOf(" ") }

    //Proxy Setup
    Column(
        modifier
            .padding(start = 16.dp, end = 16.dp, top = 36.dp)
            .width(300.dp)
    ) {

        Spacer(
            modifier = Modifier
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

        Row(
            modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                modifier = defaultPadding,
                enabled = !statedState,
                colors = defaultButtonColor,
                onClick = {
                    statedState = true
                    proxyService.running = true
                    thread(name = "Main-Process") {
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
                colors = defaultButtonColor,
                onClick = {
                    statedState = false
                    proxyService.running = false
                    logWriterService.systemLog("Sending close connection signal ...")
                }
            ) {
                Text("Stop Proxy")
            }

        }

    }

    //Center
    Column(
        modifier
            .padding(start = 16.dp, end = 16.dp, top = 36.dp)
            .width(600.dp)
    ) {
        Spacer(
            modifier = Modifier
                .padding(16.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "System Information",
                modifier = Modifier
                    .padding(start = 2.dp, end = 8.dp),
                style = OVER_LINE_STYLE
            )
            Divider(
                color = Color.Gray,
                modifier = Modifier
                    .padding(end = 2.dp)

            )
        }
        Spacer(
            modifier = Modifier
                .padding(10.dp)
        )

        LazyColumn(
            modifier
                .height(110.dp)
                .width(600.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(5.dp)
        ) {
            items(messageLogList) { message ->
                Text(message, style = MaterialTheme.typography.body2)
            }
        }

        Spacer(
            modifier = Modifier
                .padding(16.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Packets",
                modifier = Modifier
                    .padding(start = 2.dp, end = 8.dp),
                style = OVER_LINE_STYLE
            )
            Divider(
                color = Color.Gray,
                modifier = Modifier
                    .padding(end = 2.dp)

            )
        }
        Spacer(
            modifier = Modifier
                .padding(10.dp)
        )

        LazyColumn(
            modifier
                .height(540.dp)
                .width(600.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
            state = listState,
            reverseLayout = false
        ) {
            items(packetLogList) { packetLog ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clickable {
                            if (packetLog.type == Indicator.Server.name) {
                                packetToClient = packetLog.message
                            } else if (packetLog.type == Indicator.Client.name) {
                                packetToServer = packetLog.message
                            }
                        },
                    elevation = 10.dp
                ) {
                    Text(packetLog.index.toString())
                    Column(
                        modifier = Modifier.padding(start = 25.dp)
                    ) {
                        Text(packetLog.type, fontWeight = FontWeight.Bold)
                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                            Text(packetLog.message, style = MaterialTheme.typography.body2)
                        }
                    }
                }
            }
        }

        Row(
            modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                colors = defaultButtonColor,
                onClick = {
                    if (!listState.isScrollInProgress && packetLogList.isNotEmpty()) {
                        coroutineScope.launch {
                            listState.animateScrollToItem(index = packetLogList.size)
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                )
            }

            Button(
                colors = defaultButtonColor,
                modifier = Modifier.width(180.dp),
                onClick = {
                    if (logWriterService.logList.isNotEmpty()) {
                        logWriterService.dumpToFile()
                    }
                }
            ) {
                Text("Dump log to file")
            }

            Button(
                colors = defaultButtonColor,
                onClick = {
                    if (!listState.isScrollInProgress && packetLogList.isNotEmpty()) {
                        coroutineScope.launch {
                            listState.animateScrollToItem(index = 0)
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                )
            }
        }
    }

    //Packet Injector
    Column(
        modifier
            .padding(start = 16.dp, end = 16.dp, top = 36.dp)
            .width(300.dp)
    ) {

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
            enabled = statedState,
            singleLine = true,
            onValueChange = { value -> packetToServer = value },
            label = { Text("Send to Server") }
        )
        Button(
            modifier = defaultPadding,
            enabled = statedState,
            colors = defaultButtonColor,
            onClick = {
                coroutineScope.launch {
                    proxyService.sendPacket2Server(packetToServer, 0L)
                }
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
            enabled = statedState,
            singleLine = true,
            onValueChange = { value -> packetToClient = value },
            label = { Text("Send to Client") }
        )

        Button(
            modifier = defaultPadding,
            enabled = statedState,
            colors = defaultButtonColor,
            onClick = {
                coroutineScope.launch {
                    proxyService.sendPacket2Client(packetToClient, 0L)
                }
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

    }

    if (packetLogList.size != logWriterService.logList.size) {
        packetLogList = logWriterService.logList.map { it }.reversed()
    }

    if (messageLogList.size != logWriterService.systemLogList.size) {
        messageLogList = logWriterService.systemLogList.map { it }.takeLast(6)
    }
}
