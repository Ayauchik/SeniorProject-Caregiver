import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kz.nu.connectionphoneapp.R
import kz.nu.connectionphoneapp.ui.theme.heartRate
import kz.nu.connectionphoneapp.ui.theme.oxygenSaturation
import kz.nu.connectionphoneapp.ui.theme.sleep
import kz.nu.connectionphoneapp.ui.theme.temperature

@Composable
fun VitalSignCard(
    vitalType: String = "",
    value: Double = 0.0,
) {

    val id = when (vitalType) {
        "HEART RATE" -> R.drawable.heart_rate
        "SpO2" -> R.drawable.spo2
        "TEMPERATURE" -> R.drawable.body_temp
        "SLEEP" -> R.drawable.sleep
        else -> {
            R.drawable.heart_rate
        }
    }

    val backgroundColor = when (vitalType) {
        "HEART RATE" -> heartRate
        "SpO2" -> oxygenSaturation
        "TEMPERATURE" -> temperature
        "SLEEP" -> sleep
        else -> {
            Color.White
        }
    }

    var valueOfSign = value.toString()
    if (vitalType == "SLEEP") {
        valueOfSign = if (valueOfSign == "0.0") {
            "OFF"
        } else {
            "ON"
        }
    } else if (vitalType == "SpO2") {
        valueOfSign = "${value.toInt()}%" // Converts to percentage
    } else if (vitalType == "TEMPERATURE") {
        valueOfSign = "${value}°F"
    } else if (vitalType == "HEART RATE") {
        valueOfSign = "$value bpm"
    }

    Card(
        modifier = Modifier.size(160.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    vertical = 12.dp,
                    horizontal = 20.dp
                )
                .fillMaxWidth(),
            // .background(backgroundColor.copy(0.85F)),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = vitalType,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.size(18.dp))

            Image(
                painter = painterResource(id = id),
                contentDescription = "vital sign type",
                modifier = Modifier.size(64.dp)
            )


            Text(
                text = valueOfSign,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                fontSize = 20.sp

            )
        }
    }
}


@Preview
@Composable
fun VitalSignCardPreview() {
    VitalSignCard(
        vitalType = "HEART RATE",
        value = 80.0
    )
}
