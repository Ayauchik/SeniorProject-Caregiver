package kz.nu.connectionphoneapp.vital_sign_irt.presentation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import android.media.MediaPlayer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kz.nu.connectionphoneapp.R

@Composable
fun AlarmScreen(onStopAlarm: () -> Unit) {
    AlertDialog(
        modifier = Modifier.size(width = 400.dp, height = 340.dp),
        onDismissRequest = {}, // Prevent dismissing without pressing the button
        title = {
            Text(
                text = "EMERGENCY ALERT",
                color = Color.Red,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Critical health alert detected! Please take immediate action.",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp),
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            Button(
                // modifier = Modifier.padding(top = 40.dp),
                onClick = onStopAlarm
            ) {
                Text(
                    text = "Stop Alarm",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                    )
            }
        }
    )
}

@Preview
@Composable
fun AlarmScreenDialog() {
    AlarmScreen {

    }
}