package kz.nu.connectionphoneapp.vital_sign_irt.data.network

import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kz.nu.connectionphoneapp.vital_sign_irt.notification.AlarmBroadcastReceiver
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class PushNotificationService() : FirebaseMessagingService() {


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("FCM", "Refreshed token: $token")

        sendTokenToServer(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.e("FCM", "Message received: ${message.data}")

        // Extract JSON data
        val jsonData = JSONObject(message.data as Map<*, *>)

        var title = jsonData.optString("title", "🚨 Alert")
        var body = jsonData.optString("body", "An anomaly has been detected!")
        val userId = jsonData.optString("user_id", "Unknown")

        val data = message.data
        val alertType = data["alert_type"]
        Log.e("FCM", "Alert Type: $alertType")

        val anomalyId = data["anomaly_id"]
        Log.e("FCM", "Anomaly ID: $anomalyId")

        val sensorData = formatSensorData(data["sensor_data"])
        val details = data["details"]
        val timestamp = formatTimestamp(data["timestamp"])
        // Log.e("FCM", "Notification Data - Title: $title, Body: $body, User ID: $userId")


        if (alertType == "sensor_meta") {
            title = "🚨 Sensor Meta Alert"
            body =
                jsonData.optString("body", "$details\n\nTime: $timestamp\n\nSensor: \n$sensorData")
        } else if (alertType == "manual_sos") {
            title = "🚨 Manual SOS Alert"
            body = jsonData.optString(
                "body", "$details\n" +
                        "\n" +
                        "Time: $timestamp"
            )
        }

        val intent = Intent(this, AlarmBroadcastReceiver::class.java).apply {
            action = "SHOW_ALARM_DIALOG"
            putExtra("title", title)
            putExtra("body", body)
            putExtra("anomaly_id", anomalyId)
        }

        sendBroadcast(intent)
    }

    private fun formatSensorData(sensorDataStr: String?): String {
        if (sensorDataStr.isNullOrBlank()) return "No sensor data"

        return sensorDataStr
            .removePrefix("{")
            .removeSuffix("}")
            .replace("\"", "")
            .split(",")
            .joinToString("\n") { entry ->
                val parts = entry.split(":")
                val key = parts.getOrNull(0)?.trim()?.replace('_', ' ')
                    ?.replaceFirstChar { it.uppercase() } ?: "Unknown"
                val value = parts.getOrNull(1)?.trim() ?: "N/A"
                "$key = $value"
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


    private fun sendTokenToServer(token: String) {
        // Send this token to your Flask backend via Retrofit
        Log.e("FCM", "Token sent to server: $token")
    }

}