package kz.nu.connectionphoneapp.vital_sign_irt.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.request.MessageRequest
import kz.nu.connectionphoneapp.vital_sign_irt.data.network.request.RegisterRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class NotificationViewModel : ViewModel() {

    private val api: FcmApi = Retrofit.Builder()
        .baseUrl("http://172.20.10.13:8080/") // Change this to your Flask server IP
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create()

    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId

    private val _token = MutableStateFlow("")
    val token: StateFlow<String> = _token

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _status = MutableStateFlow("")
    val status: StateFlow<String> = _status

    private val _isSending = MutableStateFlow(false)
    val isSending: StateFlow<Boolean> = _isSending

    private val _isAlarming = MutableLiveData(false)
    val isAlarming: LiveData<Boolean> get() = _isAlarming

    fun updateUserId(id: String) {
        _userId.value = id
    }

    fun updateToken(newToken: String) {
        _token.value = newToken
    }

    fun updateMessage(text: String) {
        _message.value = text
    }

    fun updateAlarm(boolean: Boolean){
        _isAlarming.value = boolean
    }

    fun registerUser() {
        viewModelScope.launch {
            try {
                val response = api.registerUser(RegisterRequest(_userId.value, _token.value))
                _status.value = response.message
                Log.d("NotificationViewModel", "User registered: ${response.message}")
            } catch (e: Exception) {
                _status.value = "Failed to register user"
                Log.e("NotificationViewModel", "Error: ${e.message}")
            }
        }
    }

    fun sendMessage() {
        viewModelScope.launch {
            _isSending.value = true
            try {
                val response = api.sendMessageBack(MessageRequest(_userId.value, _message.value))
                _status.value = response.message
                Log.d("NotificationViewModel", "Response: ${response.message}")
            } catch (e: Exception) {
                _status.value = "Failed to send message"
                Log.e("NotificationViewModel", "Error: ${e.message}")
            }
            _isSending.value = false
        }
    }
}
