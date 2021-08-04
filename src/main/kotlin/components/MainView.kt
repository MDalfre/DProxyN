package components

import OVER_LINE_STYLE
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import commons.ConfigFileHandler
import commons.DarkBlue
import commons.DarkGreen
import kotlinx.coroutines.launch
import model.Indicator
import model.Log
import services.LogWriterService
import services.ProxyService
import kotlin.concurrent.thread

val defaultPadding = Modifier.padding(8.dp)

@Composable
fun mainView(
    modifier: Modifier,
    proxyService: ProxyService,
    logWriterService: LogWriterService,
    configFile: MutableList<String>
) {
    val (remoteAddressProp, remotePortProp, localPortProp) = configFile

    val defaultButtonColor = buttonColors(backgroundColor = Color.DarkGray, contentColor = Color.White)
    val defaultButtonAbortColor = buttonColors(backgroundColor = MaterialTheme.colors.error)
    val injectedTextColor = MaterialTheme.colors.primary.copy(alpha = ContentAlpha.high)
    val checkboxDefaultColor = CheckboxDefaults.colors(checkedColor = MaterialTheme.colors.primary)

    val defaultLoopValueRange = 1f..300f
    val defaultIntervalValueRange = 10f..3000f

    var remoteAddress by remember { mutableStateOf(remoteAddressProp) }
    var remotePort by remember { mutableStateOf(remotePortProp) }
    var localPort by remember { mutableStateOf(localPortProp) }
    var statedState by remember { mutableStateOf(false) }

    var packetLogList by mutableStateOf(listOf<Log>())
    var messageLogList by mutableStateOf(listOf<String>())
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var clientLogEnabled by remember { mutableStateOf(true) }
    var serverLogEnabled by remember { mutableStateOf(true) }

    var packetToServer by remember { mutableStateOf(" ") }
    var packetToClient by remember { mutableStateOf(" ") }
    var sendToServerSliderPacketAmount by remember { mutableStateOf(1f) }
    var sendToServerSliderPacketDelay by remember { mutableStateOf(150f) }
    var sendToClientSliderPacketAmount by remember { mutableStateOf(1f) }
    var sendToClientSliderPacketDelay by remember { mutableStateOf(150f) }
    var statedInjectionServerState by remember { mutableStateOf(false) }
    var statedInjectionClientState by remember { mutableStateOf(false) }

    /** Proxy Setup **/
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
                color = Color.Black,
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
                    ConfigFileHandler.createConfigFile(remoteAddress, remotePort, localPort)
                    logWriterService.systemLog("Sending close connection signal ...")
                }
            ) {
                Text("Stop Proxy")
            }
        }
    }

    /** Center **/
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
                color = Color.Black,
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
                .border(width = 0.5.dp, color = Color.DarkGray, shape = RoundedCornerShape(8.dp))
                .padding(start = 16.dp, end = 5.dp, top = 5.dp, bottom = 5.dp)
        ) {
            items(messageLogList) { message ->
                Text(message, style = MaterialTheme.typography.body2)
            }
        }

        Spacer(
            modifier = Modifier
                .padding(10.dp)
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
                color = Color.Black,
                modifier = Modifier
                    .padding(end = 2.dp)

            )
        }
        Row(
            modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Row {
                Text(
                    "Log server packets:",
                    style = OVER_LINE_STYLE,
                    modifier = modifier.padding(top = 2.dp, end = 2.dp)
                )
                Checkbox(
                    colors = checkboxDefaultColor,
                    checked = serverLogEnabled,
                    onCheckedChange = {
                        serverLogEnabled = it
                        proxyService.serverLog = it
                    }
                )
            }
            Row {
                Text(
                    "Log client packets:",
                    style = OVER_LINE_STYLE,
                    modifier = modifier.padding(top = 2.dp, end = 2.dp)
                )
                Checkbox(
                    colors = checkboxDefaultColor,
                    checked = clientLogEnabled,
                    onCheckedChange = {
                        clientLogEnabled = it
                        proxyService.clientLog = it
                    }
                )
            }
            Spacer(
                modifier = Modifier
                    .padding(6.dp)
            )
        }

        LazyColumn(
            modifier
                .height(540.dp)
                .width(600.dp),
            state = listState,
            reverseLayout = false
        ) {
            items(packetLogList) { packetLog ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 26.dp, end = 5.dp, top = 5.dp, bottom = 5.dp)
                        .clickable {
                            if (packetLog.type == Indicator.Server.name) {
                                packetToClient = packetLog.message
                            } else if (packetLog.type == Indicator.Client.name) {
                                packetToServer = packetLog.message
                            }
                        },
                    elevation = 10.dp
                ) {
                    Text(packetLog.index.toString(), style = TextStyle(fontSize = 13.sp))
                    Column(
                        modifier = Modifier.padding(start = 25.dp)
                    ) {
                        val textColor = when (packetLog.type) {
                            Indicator.Server.name -> DarkBlue
                            Indicator.Client.name -> DarkGreen
                            else -> injectedTextColor
                        }
                        Text(packetLog.type, color = textColor, fontWeight = FontWeight.Bold)
                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                            Text(packetLog.message, style = MaterialTheme.typography.body2)
                        }
                    }
                }
            }
        }
        Divider(
            color = Color.Black,
            modifier = Modifier
                .padding(end = 2.dp)
        )

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
                            listState.animateScrollToItem(index = 0)
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
                            listState.animateScrollToItem(index = packetLogList.size)
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

    /** Packet Injector **/
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
                color = Color.Black,
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
        Row(
            modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                modifier = defaultPadding,
                enabled = (statedState && packetToServer != " " && !statedInjectionServerState),
                colors = defaultButtonColor,
                onClick = {
                    coroutineScope.launch {
                        proxyService.runningServerInjection = true
                        proxyService.sendPacket2Server(
                            packet = packetToServer,
                            loop = sendToServerSliderPacketAmount.toInt(),
                            interval = sendToServerSliderPacketDelay.toLong()
                        )
                    }
                }
            ) {
                Text("To server")
            }
            Button(
                modifier = defaultPadding,
                enabled = statedInjectionServerState,
                colors = defaultButtonAbortColor,
                onClick = { proxyService.runningServerInjection = false }
            ) {
                Text("Abort")
            }
        }

        Text(
            "Loop: ${sendToServerSliderPacketAmount.toInt()} times",
            style = OVER_LINE_STYLE,
            modifier = modifier.padding(start = 16.dp, 2.dp)
        )
        Slider(
            enabled = (packetToServer != " "),
            modifier = defaultPadding.height(10.dp),
            value = sendToServerSliderPacketAmount,
            onValueChange = { sendToServerSliderPacketAmount = it },
            valueRange = defaultLoopValueRange
        )
        Text(
            "Interval: ${sendToServerSliderPacketDelay.toLong()}ms",
            style = OVER_LINE_STYLE,
            modifier = modifier.padding(start = 16.dp, 2.dp)
        )
        Slider(
            enabled = (packetToServer != " "),
            modifier = defaultPadding.height(10.dp),
            value = sendToServerSliderPacketDelay,
            onValueChange = { sendToServerSliderPacketDelay = it },
            valueRange = defaultIntervalValueRange
        )

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
                color = Color.Black,
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

        Row(
            modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                modifier = defaultPadding,
                enabled = (statedState && packetToClient != " " && !statedInjectionClientState),
                colors = defaultButtonColor,
                onClick = {
                    coroutineScope.launch {
                        proxyService.sendPacket2Client(
                            packet = packetToClient,
                            loop = sendToClientSliderPacketAmount.toInt(),
                            interval = sendToClientSliderPacketDelay.toLong()
                        )
                    }
                }
            ) {
                Text("To client")
            }

            Button(
                modifier = defaultPadding,
                enabled = statedInjectionClientState,
                colors = defaultButtonAbortColor,
                onClick = { proxyService.runningClientInjection = false }
            ) {
                Text("Abort")
            }
        }
        Text(
            "Loop: ${sendToClientSliderPacketAmount.toInt()} times",
            style = OVER_LINE_STYLE,
            modifier = modifier.padding(start = 16.dp, 2.dp)
        )
        Slider(
            enabled = (packetToClient != " "),
            modifier = defaultPadding.height(10.dp),
            value = sendToClientSliderPacketAmount,
            onValueChange = { sendToClientSliderPacketAmount = it },
            valueRange = defaultLoopValueRange
        )
        Text(
            "Interval: ${sendToClientSliderPacketDelay.toLong()}ms",
            style = OVER_LINE_STYLE,
            modifier = modifier.padding(start = 16.dp, 2.dp)
        )
        Slider(
            enabled = (packetToClient != " "),
            modifier = defaultPadding.height(10.dp),
            value = sendToClientSliderPacketDelay,
            onValueChange = { sendToClientSliderPacketDelay = it },
            valueRange = defaultIntervalValueRange
        )
    }

    /** Updaters **/

    if (packetLogList.size != logWriterService.logList.size) {
        packetLogList = packetLogList + logWriterService.logList.map { it }
            .reversed()
            .takeLast(logWriterService.logList.size - packetLogList.size)
    }

    if (messageLogList.size != logWriterService.systemLogList.size) {
        messageLogList = logWriterService.systemLogList.map { it }.takeLast(6)
    }

    if (proxyService.runningServerInjection != statedInjectionServerState) {
        statedInjectionServerState = proxyService.runningServerInjection
    }

    if (proxyService.runningClientInjection != statedInjectionClientState) {
        statedInjectionClientState = proxyService.runningClientInjection
    }
}
