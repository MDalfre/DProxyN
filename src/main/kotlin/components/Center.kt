package components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.asFlow
import model.Log
import services.LogWriter

@Composable
fun center(
    modifier: Modifier,
    logWriter: LogWriter
) {
    var eventLogList = remember { mutableStateListOf("")}

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
                .height(550.dp)
                .width(600.dp)
                .border(color = Color.Gray, width = 1.dp)

        ) {
            eventLogList.forEach {
                item { Text(it) }
            }
        }

        Button(
            onClick = {eventLogList.add(logWriter.logList.last().message)  }
        ) {
            Text("Atualizar")
        }

    }
}