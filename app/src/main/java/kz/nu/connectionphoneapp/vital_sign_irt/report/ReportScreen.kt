package kz.nu.connectionphoneapp.vital_sign_irt.report

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aay.compose.baseComponents.model.GridOrientation
import com.aay.compose.baseComponents.model.LegendPosition
import com.aay.compose.lineChart.LineChart
import com.aay.compose.lineChart.model.LineParameters
import com.aay.compose.lineChart.model.LineType
import com.github.mikephil.charting.data.LineData

@Composable
fun ReportScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        //verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Report Screen",
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 16.dp),
            fontWeight = FontWeight.W500
        )
        Text(
            text = "Time period: Today 10.00 - 10.20",
            fontSize = 14.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            fontWeight = FontWeight.W400
        )
        Text(
            text = "Activity timeline",
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp),
            fontWeight = FontWeight.W400,
            // fontStyle = FontStyle.Italic,
            textDecoration = TextDecoration.Underline
        )
        ActivityTimelineScreen()
        //Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = "Vital Signs Graphs",
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp),
            fontWeight = FontWeight.W400,
            //fontStyle = FontStyle.Italic,
            textDecoration = TextDecoration.Underline

        )
        VitalSignsDashboard()
    }
}

@Composable
fun VitalSignsDashboard() {
    val xAxisLabels = listOf("10:00", "10:05", "10:10", "10:15", "10:20")

    val heartRateData = LineParameters(
        label = "Heart Rate (bpm)",
        data = listOf(72.0, 74.5, 78.0, 76.0, 75.5),
        lineColor = Color.Red,
        lineType = LineType.CURVED_LINE,
        lineShadow = true
    )

    val spo2Data = LineParameters(
        label = "SpO₂ (%)",
        data = listOf(98.0, 97.5, 98.0, 99.0, 98.5),
        lineColor = Color.Blue,
        lineType = LineType.CURVED_LINE,
        lineShadow = true
    )

    val temperatureData = LineParameters(
        label = "Temperature (°C)",
        data = listOf(36.4, 36.5, 36.7, 36.6, 36.8),
        lineColor = Color.Green,
        lineType = LineType.CURVED_LINE,
        lineShadow = true
    )

    val yAxisValues = listOf(
        0.9941520690917968,0.5256659984588623,0.24821312725543976,0.07082521170377731,0.1520467847585678,0.25860947370529175,0.31578946113586426,0.3469785451889038,0.3528265058994293,0.34892788529396057,0.35932424664497375,0.35867446660995483,0.38076671957969666,0.37556853890419006,0.37556853890419006,0.3898635506629944,0.3814164996147156,0.39311242103576655,0.38791424036026,0.376218318939209,0.3729694485664368,0.3820662796497345,0.38466536998748774,0.37816762924194336,0.3710201382637024,0.3768680989742279,0.3820662796497345,0.39311242103576655,0.3976608216762542,0.3983106017112732,0.3983106017112732,0.395061731338501,0.4048083126544952,0.41650423407554626,0.41390514373779297,0.41650423407554626,0.427550345659256,0.43924626708030706,0.4340480864048004,0.43469786643981934,0.44314488768577576,0.446393758058548,0.45419102907180786,0.4600389897823334,0.4411955773830414,0.4385964870452881,0.4230019450187683,0.4132553637027741,0.40740740299224854,0.38661468029022217,0.38401558995246887,0.3632228672504425,0.3625730872154236,0.36582195758819586,0.3606237769126892,0.34762832522392273,0.3554255962371826,0.3469785451889038,0.35412606596946716,0.3554255962371826,0.3495776355266571,0.34892788529396057,0.35607537627220154,0.3677712678909302,0.35867446660995483,0.36192333698272705,0.35607537627220154,0.36582195758819586,0.36582195758819586,0.35932424664497375,0.3547758162021637,0.3463287949562073,0.35932424664497375,0.3554255962371826,0.34892788529396057,0.34892788529396057,0.343729704618454,0.343729704618454,0.33983105421066284,0.3411306142807007,0.33398309350013733,0.33203378319740295,0.34567901492118835,0.33983105421066284,0.3365821838378906,0.3385315239429474,0.32878491282463074,0.35347628593444824,0.3528265058994293,0.3385315239429474,0.32683560252189636,0.33398309350013733,0.33723196387290955,0.3651721775531769,0.3710201382637024,0.3976608216762542,0.4126055836677551,0.4256010353565216,0.46523717045784,0.5107212662696838,0.5035737752914429,0.4970760345458984,0.4964262545108795,0.46718648076057434,0.4418453574180603,0.40935671329498285,0.34762832522392273,0.33528265357017517,0.3086419701576233,0.3034437894821167,0.29694607853889465,0.3092917501926422,0.3001949191093445,0.30604287981987,0.2975958287715912,0.30994153022766113,0.3008446991443634,0.5009746551513672,0.4567901194095611,0.7576348185539244,1.0,0.6718648672103882,0.26640674471855164,0.15984405577182767,0.0,0.22417153418064115,0.2742040157318115,0.31838855147361755,0.33203378319740295,0.32878491282463074,0.35347628593444824,0.3144899308681488,0.3274853825569153,0.35412606596946716,0.33723196387290955,0.35347628593444824,0.35672515630722046,0.3599739968776703,0.36192333698272705,0.3625730872154236,0.357374906539917,0.3606237769126892,0.37231969833374023,0.3677712678909302,0.357374906539917,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
    )
    val xLabels = yAxisValues.indices.map {
        if (it % 10 == 0) it.toString() else ""
    }

    val ecgData = LineParameters(
        data = yAxisValues,
        lineColor = Color.Red,
        lineType = LineType.CURVED_LINE,
        lineShadow = true,
        label = "ECG"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        LineChartCard(heartRateData, xAxisLabels, showAxis = true, gridOrientation = GridOrientation.VERTICAL)
        LineChartCard(spo2Data, xAxisLabels, showAxis = true, gridOrientation = GridOrientation.VERTICAL)
        LineChartCard(temperatureData, xAxisLabels, showAxis = true, gridOrientation = GridOrientation.VERTICAL)
        LineChartCard(ecgData, xLabels, showAxis = false, gridOrientation = GridOrientation.HORIZONTAL)
    }
}

@Composable
fun ECGBroadcastDashboard() {
    LineChartCardECG()
}

@Composable
fun LineChartCardECG(yAxisValues: List<Double> = emptyList()) {

    val xLabels = yAxisValues.indices.map {
        if (it % 10 == 0) it.toString() else ""
    }

    val ecgData = LineParameters(
        data = yAxisValues,
        lineColor = Color.Red,
        lineType = LineType.CURVED_LINE,
        lineShadow = true,
        label = "ECG"
    )
    LineChart(
        modifier = Modifier
            //.fillMaxWidth(),
            .height(300.dp),
        linesParameters = listOf(ecgData),
        xAxisData = xLabels,
        showXAxis = false,
        animateChart = true,
        isGrid = true,
        gridColor = Color.LightGray,
        showGridWithSpacer = true,
        yAxisStyle = TextStyle(color = Color.DarkGray, fontSize = 12.sp),
        xAxisStyle = TextStyle(color = Color.DarkGray, fontSize = 12.sp),
        gridOrientation = GridOrientation.HORIZONTAL,
    )

}



@Composable
fun LineChartCard(
    data: LineParameters,
    xAxis: List<String>,
    showAxis: Boolean = true,
    gridOrientation: GridOrientation
) {
    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        linesParameters = listOf(data),
        xAxisData = xAxis,
        showXAxis = showAxis,
        animateChart = true,
        isGrid = true,
        gridColor = Color.LightGray,
        showGridWithSpacer = true,
        yAxisStyle = TextStyle(color = Color.DarkGray, fontSize = 12.sp),
        xAxisStyle = TextStyle(color = Color.DarkGray, fontSize = 12.sp),
        gridOrientation = gridOrientation,
    )
}

@Preview
@Composable
fun PreviewReportScreen() {
    ECGBroadcastDashboard()
//ReportScreen()
}