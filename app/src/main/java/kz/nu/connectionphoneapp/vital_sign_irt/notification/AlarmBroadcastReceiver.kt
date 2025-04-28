package kz.nu.connectionphoneapp.vital_sign_irt.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "SHOW_ALARM_DIALOG") {
            val alarmIntent = Intent(context, AlarmActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra("title", intent.getStringExtra("title"))
                putExtra("body", intent.getStringExtra("body"))
                putExtra("anomaly_id", intent.getStringExtra("anomaly_id"))
                putExtra("alertType", intent.getStringExtra("alertType"))
            }
            context.startActivity(alarmIntent)
            Log.e("ALARM_RECEIVER", "Alarm Activity Launched")
        }
    }
}
