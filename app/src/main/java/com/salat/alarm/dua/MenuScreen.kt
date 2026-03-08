package com.salat.alarm.dua

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MenuScreen : AppCompatActivity() {

    lateinit var prefs: SharedPreferences
    private val PREFS_NAME = "PrayerPrefs"
    private val DARK_MODE_KEY = "dark_mode_enabled"
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_screen)

        prefs = getSharedPreferences("PrayerPrefs", Context.MODE_PRIVATE)
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)



        val cardQibla = findViewById<CardView>(R.id.cardQibla)
        val cardpraertime = findViewById<CardView>(R.id.cardPryaerAlarm)
        val cardAzkar = findViewById<CardView>(R.id.cardAzkar)
        val cardshalat = findViewById<CardView>(R.id.cardshalat)
        cardQibla.setOnClickListener {
            val intent = Intent(this, QiblaActivity::class.java)
            startActivity(intent)
        }
        cardpraertime.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        cardAzkar.setOnClickListener {
            val intent = Intent(this, ZikrActivity::class.java)
            startActivity(intent)
        }
        cardshalat.setOnClickListener {
            val intent = Intent(this, ShalatActivity::class.java)
            startActivity(intent)
        }


        val btnDarkMode = findViewById<CardView>(R.id.carddarkode)
        // Load saved dark mode state
        val isDarkMode = prefs.getBoolean(DARK_MODE_KEY, false)
        applyDarkMode(isDarkMode)

        btnDarkMode.setOnClickListener {
            val currentMode = prefs.getBoolean(DARK_MODE_KEY, false)
            val newMode = !currentMode
            prefs.edit().putBoolean(DARK_MODE_KEY, newMode).apply()
            applyDarkMode(newMode)
        }


    }

    private fun applyDarkMode(enable: Boolean) {


        val cardIds = listOf(
            R.id.cardQibla,
            R.id.cardPryaerAlarm,
            R.id.cardAzkar,
            R.id.cardshalat,
            R.id.carddarkode
        )

        val goto = listOf(
            R.id.gotoqibla, R.id.gotoprayer, R.id.gotoazkar,
            R.id.gotoslat, R.id.gotodark
        )


        val nameIds = listOf(
            R.id.txtqibla,
            R.id.txtprayer,
            R.id.txtazkar,
            R.id.txtshalat,
            R.id.txtdark
        )


        if (enable) {


            // Cards
            for (id in cardIds) {
                findViewById<CardView>(id)
                    .setCardBackgroundColor(Color.parseColor("#436399FF"))
            }

            // Time Text
            for (id in goto) {
                findViewById<TextView>(id)
                    .setTextColor(Color.parseColor("#B0BEC5"))
            }



            // 🔹 Prayer Names (White)
            for (id in nameIds) {
                findViewById<TextView>(id)
                    .setTextColor(Color.WHITE)
            }



        } else {


            for (id in cardIds) {
                findViewById<CardView>(id)
                    .setCardBackgroundColor(Color.WHITE)
            }

            for (id in goto) {
                findViewById<TextView>(id)
                    .setTextColor(Color.BLACK)
            }



            // 🔹 Prayer Names
            for (id in nameIds) {
                findViewById<TextView>(id)
                    .setTextColor(Color.BLACK)
            }


        }
    }
    @SuppressLint("GestureBackNavigation")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}