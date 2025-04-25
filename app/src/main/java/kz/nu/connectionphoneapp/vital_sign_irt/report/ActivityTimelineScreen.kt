package kz.nu.connectionphoneapp.vital_sign_irt.report

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ActivityTimelineScreen() {
    val segments = listOf(
        ActivitySegment("Sleeping", Color(0xFF90CAF9), 15),
        ActivitySegment("Walking", Color(0xFF81C784), 5)
    )

    TimelineWithActivityChangeTimes(
        segments = segments,
        startTime = "10:00"
    )
}
