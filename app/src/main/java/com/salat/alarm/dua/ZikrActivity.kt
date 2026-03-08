package com.salat.alarm.dua

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ZikrActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences
    private val PREFS_NAME = "PrayerPrefs"
    private val DARK_MODE_KEY = "dark_mode_enabled"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zikr)

        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)


        val prayerNames = listOf("الفجر", "الظهر", "العصر", "المغرب", "العشاء")

        val recyclerView = findViewById<RecyclerView>(R.id.azkarRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SalahAdapter(prayerNames) { selectedSalah ->
            // Open Azkar details for clicked Salah
            val intent = Intent(this, AzkarDetailActivity::class.java)
            intent.putExtra("salah_name", selectedSalah)
            startActivity(intent)
        }

        val isDarkMode = prefs.getBoolean(DARK_MODE_KEY, false)
        applyDarkMode(isDarkMode)
    }

    private fun applyDarkMode(enable: Boolean) {

        val recyclerView = findViewById<RecyclerView>(R.id.azkarRecyclerView)

        if (enable) {

            // Background Dark

            // Tell adapter dark mode is ON
            (recyclerView.adapter as? SalahAdapter)?.setDarkMode(true)

        } else {

            // Background Light

            // Tell adapter dark mode is OFF
            (recyclerView.adapter as? SalahAdapter)?.setDarkMode(false)
        }
        val back = findViewById<ImageView>(R.id.back)

        back.setOnClickListener {
            finish()
        }
    }

}