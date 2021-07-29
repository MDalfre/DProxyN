package components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun top() {

    TopAppBar(backgroundColor = Color.Black) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "  [DproxyN]  ",
                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp)
                    .background(Color.DarkGray, shape = RoundedCornerShape(8.dp)),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    letterSpacing = 2.5.sp
                )
            )
        }
    }
}