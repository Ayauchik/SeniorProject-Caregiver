package kz.nu.connectionphoneapp.vital_sign_irt.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.SimulationAPI
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.request.MessageRequest
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.request.RegisterRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class NotificationViewModel(
   // private val api: SimulationAPI
) : ViewModel() {

//    private val api: FcmApi = Retrofit.Builder()
//        .baseUrl("http://172.20.10.13:8080/") // Change this to your Flask server IP
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//        .create()


    private val _token = MutableStateFlow("")
    val token: StateFlow<String> = _token

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _status = MutableStateFlow("")
    val status: StateFlow<String> = _status


    fun registerUser(token: String) {
        viewModelScope.launch {
//            try {
//                val response = api.registerToken(RegisterRequest(token = token, type = "caregiver"))
//                _status.value = response.message
//                Log.d("NotificationViewModel", "User registered: ${response.message}")
//            } catch (e: Exception) {
//                _status.value = "Failed to register user"
//                Log.e("NotificationViewModel", "Error: ${e.message}")
//            }
        }
    }
}
