package kz.nu.connectionphoneapp.vital_sign_irt.report

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class ActivitySegment(
    val label: String,
    val color: Color,
    val durationMinutes: Int
)

@SuppressLint("NewApi")
@Composable
fun TimelineWithActivityChangeTimes(
    segments: List<ActivitySegment>,
    startTime: String = "10:00", // Start time in HH:mm format
    modifier: Modifier = Modifier
) {
    val totalDuration = segments.sumOf { it.durationMinutes }

    // Parse start time
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val start = LocalTime.parse(startTime, formatter)

    // Calculate change times
    val changeTimes = mutableListOf(start)
    var elapsedMinutes = 0
    for (segment in segments) {
        elapsedMinutes += segment.durationMinutes
        changeTimes += start.plusMinutes(elapsedMinutes.toLong())
    }


    Column(modifier = modifier.padding(horizontal = 16.dp)) {

        // Legend
        Row {
            segments.distinctBy { it.label }.forEach { segment ->
                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(segment.color, shape = RoundedCornerShape(2.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = segment.label, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }

        // Timeline Bar
        Row(modifier = Modifier.fillMaxWidth().height(40.dp)) {
            segments.forEach { segment ->
                val weight = segment.durationMinutes.toFloat() / totalDuration
                Box(
                    modifier = Modifier
                        .weight(weight)
                        .fillMaxHeight()
                        .padding(horizontal = 1.dp)
                        .background(segment.color, shape = RoundedCornerShape(4.dp))
                )
            }
        }

        // Time markers below timeline
        // Time markers under each segment
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
        ) {
            val segmentWeights = segments.map { it.durationMinutes.toFloat() / totalDuration }

            changeTimes.forEachIndexed { index, time ->
                if (index < segmentWeights.size) {
                    Box(
                        modifier = Modifier
                            .weight(segmentWeights[index])
                    ) {
                        Text(
                            text = time.format(formatter),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W400
                        )
                    }
                } else {
                    // For the last time, just align to the end
                    Text(
                        text = time.format(formatter),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W400
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewLegend(){
    val activitySegments = listOf(
        ActivitySegment("Sleeping", Color(0xFF90CAF9), 300),
        ActivitySegment("Walking", Color(0xFF81C784), 30))
   // TimelineChartWithLegend(activitySegments)

}