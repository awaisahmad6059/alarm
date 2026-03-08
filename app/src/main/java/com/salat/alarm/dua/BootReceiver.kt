package com.salat.alarm.dua

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == "android.intent.action.TIME_SET") {
            val prefs = context.getSharedPreferences("PrayerPrefs", Context.MODE_PRIVATE)
            val prayers = listOf("Fajr","Zuhr","Asar","Maghrib","Isha")
            for(prayer in prayers){
                if(prefs.getBoolean("${prayer}_switch", false)) {
                    val time = prefs.getString("${prayer}_time", "05:00 AM")!!
                    AlarmHelper.schedulePrayer(context, prayer, time)
                }
            }
        }
    }
}