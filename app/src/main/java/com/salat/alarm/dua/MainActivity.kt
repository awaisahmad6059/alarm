package com.salat.alarm.dua

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var prefs: SharedPreferences
    private val PREFS_NAME = "PrayerPrefs"
    private val DARK_MODE_KEY = "dark_mode_enabled"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val isDarkMode = prefs.getBoolean(DARK_MODE_KEY, false)
        applyDarkMode(isDarkMode)

        checkExactAlarmPermission()


        setupPrayer(R.id.fajar, R.id.switchFajr, R.id.fajrtime, R.id.txtFajrStatus, "Fajr")
        setupPrayer(R.id.zuhr, R.id.switchzuhr, R.id.zuhrtime, R.id.txtzuhrStatus, "Zuhr")
        setupPrayer(R.id.asar, R.id.switchasar, R.id.asartime, R.id.txtasarStatus, "Asar")
        setupPrayer(R.id.maghrib, R.id.switchmaghrib, R.id.maghribtime, R.id.txtmaghribStatus, "Maghrib")
        setupPrayer(R.id.isha, R.id.switchisha, R.id.ishatime, R.id.txtishaStatus, "Isha")
    }

    private fun applyDarkMode(enable: Boolean) {


        val cardIds = listOf(R.id.fajar, R.id.zuhr, R.id.asar, R.id.maghrib, R.id.isha)

        val timeIds = listOf(
            R.id.fajrtime, R.id.zuhrtime, R.id.asartime,
            R.id.maghribtime, R.id.ishatime
        )

        val statusIds = listOf(
            R.id.txtFajrStatus, R.id.txtzuhrStatus,
            R.id.txtasarStatus, R.id.txtmaghribStatus,
            R.id.txtishaStatus
        )

        // 🔹 Only Prayer Name Texts
        val nameIds = listOf(
            R.id.fajrtxt,
            R.id.zuhrtxt,
            R.id.asrtxt,
            R.id.maghribtxt,
            R.id.ishatxt
        )

        val label = findViewById<TextView>(R.id.label)

        if (enable) {


            // Cards
            for (id in cardIds) {
                findViewById<androidx.cardview.widget.CardView>(id)
                    .setCardBackgroundColor(Color.parseColor("#436399FF"))
            }

            // Time Text
            for (id in timeIds) {
                findViewById<TextView>(id)
                    .setTextColor(Color.parseColor("#B0BEC5"))
            }

            // Status Text
            for (id in statusIds) {
                findViewById<TextView>(id)
                    .setTextColor(Color.parseColor("#4CAF50"))
            }

            // 🔹 Prayer Names (White)
            for (id in nameIds) {
                findViewById<TextView>(id)
                    .setTextColor(Color.WHITE)
            }

            // 🔥 LABEL Separate Color
            label.setTextColor(Color.WHITE)

        } else {


            for (id in cardIds) {
                findViewById<androidx.cardview.widget.CardView>(id)
                    .setCardBackgroundColor(Color.WHITE)
            }

            for (id in timeIds) {
                findViewById<TextView>(id)
                    .setTextColor(Color.BLACK)
            }

            for (id in statusIds) {
                findViewById<TextView>(id)
                    .setTextColor(Color.BLACK)
            }

            // 🔹 Prayer Names
            for (id in nameIds) {
                findViewById<TextView>(id)
                    .setTextColor(Color.BLACK)
            }

            // 🔥 LABEL Separate
            label.setTextColor(Color.BLACK)
        }
        val back = findViewById<ImageView>(R.id.back)

        back.setOnClickListener {
            finish()
        }
    }

    private fun checkExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(AlarmManager::class.java)
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent)
            }
        }
    }

    private fun setupPrayer(cardId: Int, switchId: Int, timeId: Int, statusId: Int, key: String) {
        val card = findViewById<androidx.cardview.widget.CardView>(cardId)
        val prayerSwitch = findViewById<Switch>(switchId)
        val timeText = findViewById<TextView>(timeId)
        val statusText = findViewById<TextView>(statusId)

        // Load saved data
        timeText.text = prefs.getString("${key}_time", defaultTime(key))
        val savedState = prefs.getBoolean("${key}_switch", false)
        prayerSwitch.isChecked = savedState
        updateStatus(statusText, savedState)

        prayerSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("${key}_switch", isChecked).apply()
            updateStatus(statusText, isChecked)

            if (isChecked) {
                val time = timeText.text.toString()
                AlarmHelper.schedulePrayer(this, key, time)
            } else {
                AlarmHelper.cancelPrayer(this, key)
            }
        }

        card.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(
                this,
                { _, selectedHour, selectedMinute ->
                    val amPm = if (selectedHour >= 12) "PM" else "AM"
                    val hour12 = if (selectedHour > 12) selectedHour - 12 else if (selectedHour == 0) 12 else selectedHour
                    val minuteFormatted = if (selectedMinute < 10) "0$selectedMinute" else "$selectedMinute"
                    val finalTime = "$hour12:$minuteFormatted $amPm"
                    timeText.text = finalTime
                    prefs.edit().putString("${key}_time", finalTime).apply()

                    if (prayerSwitch.isChecked) {
                        AlarmHelper.schedulePrayer(this, key, finalTime)
                    }

                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            ).show()
        }
    }

    private fun updateStatus(statusText: TextView, isOn: Boolean) {
        if (isOn) {
            statusText.text = "Alarm On"
            statusText.setTextColor(Color.GREEN)
        } else {
            statusText.text = "Alarm Off"
            statusText.setTextColor(Color.RED)
        }
    }

    private fun defaultTime(prayer: String): String {
        return when(prayer) {
            "Fajr" -> "04:45 AM"
            "Zuhr" -> "12:30 PM"
            "Asar" -> "04:00 PM"
            "Maghrib" -> "06:15 PM"
            "Isha" -> "07:30 PM"
            else -> "05:00 AM"
        }
    }
}