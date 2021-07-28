package components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import model.Log
import services.LogWriter
import javax.management.relation.Role
import kotlin.concurrent.thread
import kotlin.time.Duration

@Composable
fun center(
    modifier: Modifier,
    logWriter: LogWriter
) {
    var eventLogList by mutableStateOf(listOf<Log>())
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier
            .width(600.dp)
    ) {
        Text(
            text = "System Information:",
            style = TextStyle(
                fontSize = 22.sp
            )
        )
        LazyColumn(
            modifier
                .height(100.dp)
                .width(600.dp)
                .border(color = Color.Gray, width = 1.dp)
        ) {
            // Add a single item
            item {
                Text(text = "System")
            }
        }
        Spacer(modifier.height(10.dp))
        Text(
            text = "Packets:",
            style = TextStyle(
                fontSize = 22.sp
            )
        )
        LazyColumn(
            modifier
                .height(540.dp)
                .width(600.dp)
                .border(color = Color.Gray, width = 1.dp, shape = RoundedCornerShape(8.dp)),
            state = listState,
            reverseLayout = false
        ) {
            items(eventLogList) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clickable { },
                    elevation = 10.dp
                ) {
                    Text(it.index.toString())
                    Column(
                        modifier = Modifier.padding(start = 25.dp)
                    ) {
                        Text(it.type, fontWeight = FontWeight.Bold)
                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                            Text(it.message, style = MaterialTheme.typography.body2)
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
                    if (!listState.isScrollInProgress && eventLogList.isNotEmpty()) {
                        coroutineScope.launch {
                            listState.animateScrollToItem(index = eventLogList.size)
                        }
                    }
                }
            ) {
                Text("Go to first")
            }

            Button(
                onClick = {
                    if (!listState.isScrollInProgress && eventLogList.isNotEmpty()) {
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
                } else if (eventLogList.size != logWriter.logList.size) {
                    eventLogList = logWriter.logList.map { it }.reversed()
                }
            } while (true)
        }
    }
}