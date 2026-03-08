package com.salat.alarm.dua

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmStopReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val stopIntent = Intent(context, AlarmService::class.java)
        context.stopService(stopIntent)
    }
}