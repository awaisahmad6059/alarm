package com.salat.alarm.dua

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import androidx.core.app.NotificationCompat

class AlarmService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vibrator: Vibrator

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val prayer = intent?.getStringExtra("PRAYER_NAME") ?: "Prayer"

        // Alarm running flag
        val prefs = getSharedPreferences("PrayerPrefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("ALARM_RUNNING", true).apply()

        startForeground(1, buildNotification(prayer))

        // Start media + vibration
        mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0,1000,1000),0))
        } else {
            vibrator.vibrate(longArrayOf(0,1000,1000),0)
        }

        return START_NOT_STICKY
    }

    private fun buildNotification(prayer: String): Notification {
        val channelId = "alarm_channel"
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Alarm Channel", NotificationManager.IMPORTANCE_HIGH)
            nm.createNotificationChannel(channel)
        }

        // Stop action
        val stopIntent = Intent(this, AlarmStopReceiver::class.java)
        val pendingStop = PendingIntent.getBroadcast(
            this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(prayer)
            .setContentText("Prayer Alarm")
            .setSmallIcon(R.drawable.ic_alarm)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .addAction(R.drawable.ic_stop, "Stop", pendingStop)
            .setOngoing(true) // Prevent swipe
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
        vibrator.cancel()

        // Reset flag
        val prefs = getSharedPreferences("PrayerPrefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("ALARM_RUNNING", false).apply()
    }
}