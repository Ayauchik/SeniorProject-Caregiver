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
fun NotificationScreen(
    viewModel: NotificationViewModel = viewModel(),
    ) {

    val token by viewModel.token.collectAsState()
    val message by viewModel.message.collectAsState()
    val status by viewModel.status.collectAsState()

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

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

                    viewModel.registerUser(localToken)
                }
            }
        ) {
            Text("Copy token")
        }

        Spacer(modifier = Modifier.height(10.dp))

    }
}
