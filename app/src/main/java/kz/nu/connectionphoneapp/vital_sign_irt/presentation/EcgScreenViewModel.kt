package kz.nu.connectionphoneapp.vital_sign_irt.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.SimulationAPI
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.reponses.ReportX

class EcgScreenViewModel(
    private val api: SimulationAPI
) : ViewModel() {

    var reportList by mutableStateOf<List<ReportX?>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        getReportsByDate()
    }

    fun getReportsByDate() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = api.getEcgReport(true)
                reportList = response.report
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "An unexpected error occurred"
            } finally {
                isLoading = false
            }
        }
    }
}
