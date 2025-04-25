package kz.nu.connectionphoneapp.vital_sign_irt.notification

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Button
import android.widget.TextView
import com.example.yourapp.Screen
import kz.nu.connectionphoneapp.MainActivity
import kz.nu.connectionphoneapp.R

class AlarmActivity : Activity() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        val titleText = findViewById<TextView>(R.id.alarmTitle)
        val bodyText = findViewById<TextView>(R.id.alarmBody)
        val dismissButton = findViewById<Button>(R.id.dismissButton)

        // Retrieve text from intent
        titleText.text = intent.getStringExtra("title") ?: "🚨 Alert!"
        bodyText.text = intent.getStringExtra("body") ?: "Anomaly detected!"

        // Play alarm sound
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()

        // Vibrate the phone
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(5000, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(5000)
        }

        // Stop alarm when button is pressed
        dismissButton.setOnClickListener {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            vibrator.cancel()


            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra("navigate_to", Screen.WebRTC.route)
                putExtra("anomaly_id", intent.getStringExtra("anomaly_id")) // Forward it
            }
            startActivity(intent)


            finish() // Close the activity
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        vibrator.cancel()
    }
}
