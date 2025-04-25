package kz.nu.connectionphoneapp.vital_sign_irt.data.network.request

import com.google.gson.annotations.SerializedName

data class TokenRequest(
    @SerializedName("token")
    val token: String
)
