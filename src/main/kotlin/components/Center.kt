package components

import OVER_LINE_STYLE
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import model.Log
import services.LogWriter
import kotlin.concurrent.thread

@Composable
fun center(
    modifier: Modifier,
    logWriter: LogWriter
) {
    var packetLogList by mutableStateOf(listOf<Log>())
    var messageLogList by mutableStateOf(listOf<String>())
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

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
                        .clickable { logWriter.packetToSend = packetLog },
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
                onClick = {
                    if (!listState.isScrollInProgress && packetLogList.isNotEmpty()) {
                        coroutineScope.launch {
                            listState.animateScrollToItem(index = packetLogList.size)
                        }
                    }
                }
            ) {
                Text("Go to first")
            }

            Button(
                onClick = {
                    if (!listState.isScrollInProgress && packetLogList.isNotEmpty()) {
                        coroutineScope.launch {
                            listState.animateScrollToItem(index = 0)
                        }
                    }
                }
            ) {
                Text("Go to Last")
            }
        }

        thread {
            do {
                if (logWriter.logList.isEmpty()) {
                    Thread.sleep(500)
                } else if (packetLogList.size != logWriter.logList.size) {
                    packetLogList = logWriter.logList.map { it }.reversed()
                }
            } while (true)
        }

        thread {
            do {
                if (logWriter.systemLogList.isEmpty()) {
                    Thread.sleep(1000)
                } else if (messageLogList.size != logWriter.systemLogList.size) {
                    messageLogList = logWriter.systemLogList.map { it }.takeLast(6)
                }
            } while (true)
        }
    }
}