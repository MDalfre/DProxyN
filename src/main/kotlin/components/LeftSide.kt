package components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

val defaultPadding = Modifier.padding(8.dp)

@Composable
fun leftSide(modifier: Modifier) {
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

        Button(
            modifier = defaultPadding,
            onClick = { }
        ) {
            Text("Start Proxy")
        }

    }
}

@Composable
fun center(modifier: Modifier) {
    val messages = listOf("Packet 1", "Packet 2")
    Column(
        modifier
        .width(300.dp)
    ) {
        LazyColumn {
            // Add a single item
            item {
                Text(text = "First item")
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

@Composable
fun rightSide(modifier: Modifier) {
    Column(
        modifier
            .background(MaterialTheme.colors.surface)
            .padding(start = 16.dp, end = 16.dp)
            .border(color = Color.Gray, width = 2.dp)
            .background(Color.Gray)
            .fillMaxHeight()
    ) {

        var packetToServer by remember { mutableStateOf("") }
        var packetToClient by remember { mutableStateOf("") }

        OutlinedTextField(
            modifier = defaultPadding,
            value = packetToServer,
            singleLine = true,
            placeholder = { Text("127.0.0.1") },
            onValueChange = { value -> packetToServer = value },
            label = { Text("Send to Server") }
        )

        OutlinedTextField(
            modifier = defaultPadding,
            value = packetToClient,
            singleLine = true,
            placeholder = { Text("5050") },
            onValueChange = { value -> packetToClient = value },
            label = { Text("Send to Client") }
        )

        Button(
            modifier = defaultPadding,
            onClick = { }
        ) {
            Text("Send Packet")
        }
    }
}
