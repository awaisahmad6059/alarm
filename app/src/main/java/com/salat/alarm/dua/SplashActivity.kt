package com.salat.alarm.dua


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.logoImage)

        val zoom = ScaleAnimation(
            0.8f, 1.2f,
            0.8f, 1.2f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        zoom.duration = 4000
        zoom.fillAfter = true
        logo.startAnimation(zoom)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MenuScreen::class.java)
            startActivity(intent)
            finish()
        }, 4000)
    }
}