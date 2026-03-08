package com.salat.alarm.dua

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val prayer = intent.getStringExtra("PRAYER_NAME") ?: "Prayer"
        val serviceIntent = Intent(context, AlarmService::class.java)
        serviceIntent.putExtra("PRAYER_NAME", prayer)
        ContextCompat.startForegroundService(context, serviceIntent)
    }
}