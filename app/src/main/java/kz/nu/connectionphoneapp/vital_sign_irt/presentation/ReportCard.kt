package kz.nu.connectionphoneapp.vital_sign_irt.presentation

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses.ReportX
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses.TopHar
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


@Composable
fun ECGScreen(viewModel: EcgScreenViewModel = koinViewModel()){
//    LaunchedEffect(Unit) {
//        viewModel.getReportsByDate("", "", includeEcg = false)
//    }

    if (viewModel.isLoading) {
        CircularProgressIndicator()
    } else if (viewModel.errorMessage != null) {
        Text("Error: ${viewModel.errorMessage}")
    } else {
        LazyColumn {
            items(viewModel.reportList) { report ->
                if(report != null){
                    ReportCard(report)
                }
            }
        }
    }
}


@Composable
fun ReportCard(report: ReportX) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(500.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text("Timestamp: ${formatTimestamp(report.timestamp)}", fontWeight = FontWeight.Bold)
            Text("Decision: ${report.decision_source}, Outcome: ${report.final_outcome}")
            Text("1st stage prediction: ${report.sensor_prediction}, Meta model prediction: ${report.meta_prediction}")
            Text("Feedback Received: ${if (report.feedback_received) "Yes" else "No"}")
            Spacer(Modifier.height(6.dp))
            Text("Top HAR:")

            report.top_har.forEach {
                Text("- ${it.activity}: ${"%.3f".format(it.probability)}")
            }
            Spacer(Modifier.height(26.dp))
            ECGGraph(yAxisValues = report.ecg_data ?: emptyList())

        }

    }
}

fun formatTimestamp(isoString: String?): String {
    return try {
        if (isoString == null) return "Unknown time"

        // 🔪 Trim microseconds if present
        val trimmedIso = isoString.replace(Regex("""\.(\d{3})\d+"""), ".$1")

        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
        format.timeZone = TimeZone.getTimeZone("UTC")
        val date = format.parse(trimmedIso) ?: return "Unknown time"

        val outputFormat = SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault())
        outputFormat.format(date)
    } catch (e: Exception) {
        Log.e("FCM", "Time parse error: ${e.message}")
        "Unknown time"
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewReportCard() {
    val mockReport = ReportX(
        timestamp = "2023-10-27T11:35:10.123456+00:00",
        anomaly_id = "uuid-1234",
        sensor_prediction = 1,
        meta_prediction = 1,
        final_outcome = 1,
        decision_source = "caregiver_feedback",
        feedback_received = true,
        top_har = listOf(
            TopHar("fall", 0.752),
            TopHar("laying", 0.110)
        ),
        ecg_data = listOf(0.9941520690917968,0.5256659984588623,0.24821312725543976,0.07082521170377731,0.1520467847585678,0.25860947370529175,0.31578946113586426,0.3469785451889038,0.3528265058994293,0.34892788529396057,0.35932424664497375,0.35867446660995483,0.38076671957969666,0.37556853890419006,0.37556853890419006,0.3898635506629944,0.3814164996147156,0.39311242103576655,0.38791424036026,0.376218318939209,0.3729694485664368,0.3820662796497345,0.38466536998748774,0.37816762924194336,0.3710201382637024,0.3768680989742279,0.3820662796497345,0.39311242103576655,0.3976608216762542,0.3983106017112732,0.3983106017112732,0.395061731338501,0.4048083126544952,0.41650423407554626,0.41390514373779297,0.41650423407554626,0.427550345659256,0.43924626708030706,0.4340480864048004,0.43469786643981934,0.44314488768577576,0.446393758058548,0.45419102907180786,0.4600389897823334,0.4411955773830414,0.4385964870452881,0.4230019450187683,0.4132553637027741,0.40740740299224854,0.38661468029022217,0.38401558995246887,0.3632228672504425,0.3625730872154236,0.36582195758819586,0.3606237769126892,0.34762832522392273,0.3554255962371826,0.3469785451889038,0.35412606596946716,0.3554255962371826,0.3495776355266571,0.34892788529396057,0.35607537627220154,0.3677712678909302,0.35867446660995483,0.36192333698272705,0.35607537627220154,0.36582195758819586,0.36582195758819586,0.35932424664497375,0.3547758162021637,0.3463287949562073,0.35932424664497375,0.3554255962371826,0.34892788529396057,0.34892788529396057,0.343729704618454,0.343729704618454,0.33983105421066284,0.3411306142807007,0.33398309350013733,0.33203378319740295,0.34567901492118835,0.33983105421066284,0.3365821838378906,0.3385315239429474,0.32878491282463074,0.35347628593444824,0.3528265058994293,0.3385315239429474,0.32683560252189636,0.33398309350013733,0.33723196387290955,0.3651721775531769,0.3710201382637024,0.3976608216762542,0.4126055836677551,0.4256010353565216,0.46523717045784,0.5107212662696838,0.5035737752914429,0.4970760345458984,0.4964262545108795,0.46718648076057434,0.4418453574180603,0.40935671329498285,0.34762832522392273,0.33528265357017517,0.3086419701576233,0.3034437894821167,0.29694607853889465,0.3092917501926422,0.3001949191093445,0.30604287981987,0.2975958287715912,0.30994153022766113,0.3008446991443634,0.5009746551513672,0.4567901194095611,0.7576348185539244,1.0,0.6718648672103882,0.26640674471855164,0.15984405577182767,0.0,0.22417153418064115,0.2742040157318115,0.31838855147361755,0.33203378319740295,0.32878491282463074,0.35347628593444824,0.3144899308681488,0.3274853825569153,0.35412606596946716,0.33723196387290955,0.35347628593444824,0.35672515630722046,0.3599739968776703,0.36192333698272705,0.3625730872154236,0.357374906539917,0.3606237769126892,0.37231969833374023,0.3677712678909302,0.357374906539917,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0),
        user_responded = true
    )

    ReportCard(report = mockReport)
}

