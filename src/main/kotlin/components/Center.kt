package components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.FlowPreview
import services.LogWriter

@Composable
fun center(
    modifier: Modifier,
    logWriter: LogWriter
) {
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
                .border(color = Color.Gray, width = 1.dp),
        ) {
            // Add a single item
            item{
                logWriter.file.readLines().forEach {
                    Text(text = it)
                }
            }

            // Add 5 items
            items(50) { index ->
                Text(text = "Item: $index")
            }

            // Add another single item
            item {
                Text(text = "Last item")
            }
        }
    }
}