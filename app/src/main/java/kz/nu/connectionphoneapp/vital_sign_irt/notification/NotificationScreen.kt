package kz.nu.connectionphoneapp.vital_sign_irt.notification

import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kz.nu.connectionphoneapp.R
import kz.nu.connectionphoneapp.vital_sign_irt.presentation.AlarmScreen


@Composable
fun NotificationScreen(viewModel: NotificationViewModel = viewModel()) {
    val userId by viewModel.userId.collectAsState()
    val token by viewModel.token.collectAsState()
    val message by viewModel.message.collectAsState()
    val isSending by viewModel.isSending.collectAsState()
    val status by viewModel.status.collectAsState()

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

//    val isAlarmPlaying by viewModel.isAlarming.observeAsState(false)
//
//    val context = LocalContext.current
//    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
//
//    // Start alarm when outcome == 1 and isAlarmPlaying is false
//    LaunchedEffect(isAlarmPlaying) {
//        if (!isAlarmPlaying) {
//            mediaPlayer = MediaPlayer.create(context, R.raw.alarm_sound).apply {
//                isLooping = true
//                start()
//            }
//            viewModel.updateAlarm(true) // Update the ViewModel state
//            //state.isAlarmPlaying = true // Keep alarm playing even if outcome changes
//        }
//    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedButton(
            onClick = {
                scope.launch {
                    val localToken = Firebase.messaging.token.await()
                    clipboardManager.setText(AnnotatedString(localToken))

                    Toast.makeText(
                        context,
                        "Copied local token!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        ) {
            Text("Copy token")
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = userId,
            onValueChange = { viewModel.updateUserId(it) },
            label = { Text("Enter User ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = token,
            onValueChange = { viewModel.updateToken(it) },
            label = { Text("Enter FCM Token") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { viewModel.registerUser() },
            enabled = userId.isNotEmpty() && token.isNotEmpty()
        ) {
            Text("Register User")
        }

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = message,
            onValueChange = { viewModel.updateMessage(it) },
            label = { Text("Enter Message") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { viewModel.sendMessage() },
            enabled = message.isNotEmpty() && !isSending
        ) {
            Text(if (isSending) "Sending..." else "Send Message")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = status, color = MaterialTheme.colorScheme.surface)

    }
}
