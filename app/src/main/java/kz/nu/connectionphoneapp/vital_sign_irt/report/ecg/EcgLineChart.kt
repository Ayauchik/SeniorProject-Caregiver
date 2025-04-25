package kz.nu.connectionphoneapp.vital_sign_irt.report.ecg

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun EcgChartScreen(context: Context) {
    val ecgData = remember { mutableStateOf(emptyList<Float>()) }

    LaunchedEffect(true) {
        ecgData.value = loadEcgDataFromAssets(context, "C:\\Users\\ASUS\\AndroidStudioProjects\\connectionPhoneApp\\app\\src\\main\\assets\\ecg_data\\data_for_app.csv")
    }

}


fun loadEcgDataFromAssets(context: Context, filePath: String): List<Float> {
    return try {
        val inputStream = context.assets.open(filePath)
        val lines = inputStream.bufferedReader().readLines()
        // Assuming each row is one sample with 187 features, and we use the first one
        val firstLine = lines.firstOrNull() ?: return emptyList()
        Log.e("loading", firstLine)
        firstLine.split(",")
            .dropLast(1) // drop the label at the end
            .mapNotNull { it.toFloatOrNull() }
    } catch (e: Exception) {
        emptyList()
    }
}
