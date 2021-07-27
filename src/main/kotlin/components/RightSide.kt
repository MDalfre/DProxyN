package components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
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