package kz.nu.connectionphoneapp.vital_sign_irt.presentation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.nu.connectionphoneapp.vital_sign_irt.domain.GetLastVitalSignsUseCase
import kz.nu.connectionphoneapp.vital_sign_irt.domain.GetReportsUseCase
import kz.nu.connectionphoneapp.vital_sign_irt.domain.ReportModel
import kz.nu.connectionphoneapp.vital_sign_irt.domain.VitalSignModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class VitalSignsViewModel(
    private val getLastVitalSignsUseCase: GetLastVitalSignsUseCase,
    private val getReportsUseCase: GetReportsUseCase
) : ViewModel() {

    private val _state = MutableLiveData(State())
    val state: LiveData<State> get() = _state // Make it reactive



    init {

        viewModelScope.launch {
//            while (true) {
//                getLastVitalSigns()
//                getLastReport()
//                //delay(2000) // Fetch every 2 second
//            }
        }

    }

    private fun getLastVitalSigns() {
        viewModelScope.launch {
            try {
                Log.e("VitalSignsViewModel", "getLastVitalSigns:")
                val vitalSigns = getLastVitalSignsUseCase.invoke()
                Log.e("VitalSignsViewModel", "getLastVitalSigns: $vitalSigns")
                _state.postValue(_state.value?.copy(vitalSigns = vitalSigns, isLoading = false))

                Log.e("view model", "state ${_state.value}")
            } catch (e: Exception) {
                _state.postValue(
                    _state.value?.copy(error = e.message ?: "Unknown error", isLoading = false))
            }
        }
    }

    private fun getLastReport() {
        viewModelScope.launch {
            try {
                val lastReport = getReportsUseCase.getLastReport()
               // Log.e("VitalSignsViewModel", "getLastReport: $lastReport")
                _state.postValue(_state.value?.copy(reportItem = lastReport.reports[0], isLoading = false))
                //Log.e("view model", "state ${_state.value}")
                if(lastReport.reports[0].outcome == 1){
                    //_state.postValue(_state.value?.copy(isAlarmPlaying = true))
                }

            } catch (e: Exception) {
                _state.postValue(
                    _state.value?.copy(error = e.message ?: "Unknown error", isLoading = false))
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun startVitalSignsSimulation() {
        viewModelScope.launch {
            try {
                val today = getTodayDate()
                //val today = "2025-03-20"
                val reports = getReportsUseCase.getTodayReport(today).reports

                if (reports.isEmpty()) {
                    _state.postValue(_state.value?.copy(error = "No reports found", isLoading = false))
                    return@launch
                }

                _state.postValue(_state.value?.copy(isLoading = false))

                for (report in reports) {
                    _state.postValue(_state.value?.copy(reportItem = report, isLoading = false))
                    delay(2000) // Show each report for 2 seconds
                }

            } catch (e: Exception) {
                _state.postValue(_state.value?.copy(error = e.message ?: "Unknown error", isLoading = false))
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
fun getTodayDate(): String {
    return LocalDate.now().format(DateTimeFormatter.ISO_DATE) // "2025-03-17"
}

data class State(
    val vitalSigns: VitalSignModel? = null,
    val reportItem: ReportModel.ReportItem? = null,
    val isLoading: Boolean = true,
    val error: String = "",
   // val isAlarmPlaying: Boolean = false
)