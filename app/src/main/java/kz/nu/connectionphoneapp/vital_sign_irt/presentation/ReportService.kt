package kz.nu.connectionphoneapp.vital_sign_irt.presentation

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import kz.nu.connectionphoneapp.R

class ReportService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private val CHANNEL_ID = "VitalSignsAlarm"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        Log.d("ReportService", "Service started")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.getBooleanExtra("STOP_ALARM", false) == true) {
            stopAlarm()
            return START_NOT_STICKY
        }

        startForeground(1, createNotification())

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound).apply {
                isLooping = true
                start()
            }
            Log.d("ReportService", "Alarm started")
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAlarm()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun stopAlarm() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
                release()
            }
        }
        mediaPlayer = null
        stopForeground(true)
        stopSelf()
        Log.d("ReportService", "Alarm stopped")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Vital Signs Alarm",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(): Notification {
        return Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("Emergency Detected")
            .setContentText("High-risk vital signs detected!")
            .setSmallIcon(R.drawable.heart_rate)
            .build()
    }
}
