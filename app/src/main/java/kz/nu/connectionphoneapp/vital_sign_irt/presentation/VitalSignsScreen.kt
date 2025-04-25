package kz.nu.connectionphoneapp.vital_sign_irt.presentation

import VitalSignCard
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import kz.nu.connectionphoneapp.vital_sign_irt.report.LineChartCardECG
import org.koin.androidx.compose.koinViewModel

@Composable
fun VitalSignsScreen(viewModel: VitalSignsViewModel = koinViewModel()) {
    val state by viewModel.state.observeAsState(State())

    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    } else {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp)
                .verticalScroll(rememberScrollState())
        ) {
            state.vitalSigns?.let { vitalSignModel ->
                Text(
                    text = vitalSignModel.timestamp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 28.sp,
                )

                Spacer(modifier = Modifier.size(140.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    VitalSignCard("HEART RATE", vitalSignModel.heartRate.toDouble())
                    VitalSignCard("TEMPERATURE", vitalSignModel.temperature)
                }

                Spacer(modifier = Modifier.size(30.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    VitalSignCard("SpO2", vitalSignModel.spo2.toDouble())
                    VitalSignCard("SLEEP", 0.0)
                }

                Spacer(modifier = Modifier.size(36.dp))

                val textResult = if(state.reportItem?.outcome == 0) "OK" else "Anomaly!!!"

                Text(
                    text = "Outcome: $textResult",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 28.sp,
                )

                Spacer(modifier = Modifier.size(16.dp))
            }
            LineChartCardECG()

        }
    }
}

@Preview
@Composable
fun VitalSignsScreenPreview() {
    VitalSignsScreen()
}
