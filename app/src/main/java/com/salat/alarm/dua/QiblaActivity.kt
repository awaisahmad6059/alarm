package com.salat.alarm.dua

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import java.util.*
import kotlin.math.*

class QiblaActivity : AppCompatActivity(), SensorEventListener, LocationListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var locationManager: LocationManager
    private lateinit var prefs: SharedPreferences
    private var txtCalibration: TextView? = null
    private lateinit var arrow: ImageView
    private lateinit var txtAngle: TextView
    private lateinit var txtQiblaInfo: TextView

    private lateinit var vibrator: Vibrator
    private val PREFS_NAME = "PrayerPrefs"
    private val DARK_MODE_KEY = "dark_mode_enabled"
    private var qiblaDirection = 0f
    private var currentDegree = 0f
    private var userLat = 0.0
    private var userLon = 0.0
    private var locationReady = false
    private var cityName = ""
    private var hasVibrated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qibla)

        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        txtCalibration = findViewById(R.id.txtCalibration)
        arrow = findViewById(R.id.compassArrow)
        txtAngle = findViewById(R.id.txtAngle)
        txtQiblaInfo = findViewById(R.id.txtQiblaInfo)
        val back = findViewById<ImageView>(R.id.back)

        back.setOnClickListener { finish() }

        applyDarkMode(prefs.getBoolean(DARK_MODE_KEY, false))

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission mango
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            // Permission hai, ab check karo GPS on hai ya nahi
            checkGpsStatus()
        }
    }

    private fun checkGpsStatus() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Agar GPS band hai toh har bar Settings pe bhejo
            Toast.makeText(this, "Please enable Location/GPS", Toast.LENGTH_LONG).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        } else {
            startLocationUpdates()
        }
    }

    private fun startLocationUpdates() {
        try {
            // Dono providers request karein taaki indoor/outdoor dono kaam karein
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L, 1f, this)
            }

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 1f, this)
            }

            // Sabse pehle purani location utha lein taaki arrow foran set ho jaye
            val lastKnown = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            lastKnown?.let { onLocationChanged(it) }

        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun onLocationChanged(location: Location) {
        userLat = location.latitude
        userLon = location.longitude
        locationReady = true
        cityName = getCityName(userLat, userLon)
        calculateQiblaDirection()
    }

    private fun calculateQiblaDirection() {
        if (!locationReady) return
        val kaabaLat = Math.toRadians(21.4225)
        val kaabaLon = Math.toRadians(39.8262)
        val uLat = Math.toRadians(userLat)
        val uLon = Math.toRadians(userLon)
        val deltaLon = kaabaLon - uLon
        val y = sin(deltaLon)
        val x = cos(uLat) * tan(kaabaLat) - sin(uLat) * cos(deltaLon)
        var bearing = Math.toDegrees(atan2(y, x))
        if (bearing < 0) bearing += 360
        qiblaDirection = bearing.toFloat()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || !locationReady) return

        val azimuth = event.values[0]
        val rotationDegree = qiblaDirection - azimuth

        // Aapka "Sate" animation logic
        val ra = RotateAnimation(
            currentDegree,
            rotationDegree,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        ra.duration = 210
        ra.fillAfter = true
        arrow.startAnimation(ra)

        currentDegree = rotationDegree
        updateLiveInfo(rotationDegree)
    }

    private fun updateLiveInfo(rotation: Float) {
        var normalizedRotation = (rotation % 360)
        if (normalizedRotation < 0) normalizedRotation += 360

        if (normalizedRotation <= 2 || normalizedRotation >= 358) {
            txtAngle.text = "0°"
            findViewById<TextView>(R.id.txtTitle).text = "Qibla Matched!"
            txtQiblaInfo.text = "Success! You are facing Qibla"
            txtQiblaInfo.setTextColor(Color.GREEN)
            txtCalibration?.visibility = View.GONE

            if (!hasVibrated) {
                triggerVibration()
                hasVibrated = true
            }
        } else {
            findViewById<TextView>(R.id.txtTitle).text = "Qibla Direction"
            txtAngle.text = "${normalizedRotation.toInt()}°"
            txtQiblaInfo.text = "At $cityName → Qibla is ${qiblaDirection.toInt()}°"
            val isDarkMode = prefs.getBoolean(DARK_MODE_KEY, false)
            txtQiblaInfo.setTextColor(if (isDarkMode) Color.WHITE else Color.BLACK)
            hasVibrated = false
        }
    }

    override fun onProviderDisabled(provider: String) {
        super.onProviderDisabled(provider)
        if (provider == LocationManager.GPS_PROVIDER) {
            locationReady = false
            // User ko batayein ke location band ho gayi hai
            txtQiblaInfo.text = "GPS is turned off"
            checkGpsStatus()
        }
    }

    override fun onResume() {
        super.onResume()
        // 1. Pehle sensors register karein
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)
        } else {
            Toast.makeText(this, "Magnetic sensor not found!", Toast.LENGTH_SHORT).show()
        }

        // 2. Permission check karke location updates restart karein
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                startLocationUpdates()
            } else {
                checkGpsStatus() // Agar off hai toh settings dikhaye
            }
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        locationManager.removeUpdates(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkGpsStatus()
            } else {
                // Agar deny kiya toh Activity finish
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    // Baqi methods (applyDarkMode, getCityName, triggerVibration, onAccuracyChanged) same raheinge
    private fun getCityName(lat: Double, lon: Double): String {
        return try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if (!addresses.isNullOrEmpty()) addresses[0].locality ?: "Unknown City" else "Unknown"
        } catch (e: Exception) { "Unknown" }
    }

    private fun applyDarkMode(enable: Boolean) {
        val title = findViewById<TextView>(R.id.txtTitle)
        val txtrotate = findViewById<TextView>(R.id.txtCalibration)
        if (enable) {
            title.setTextColor(Color.WHITE)
            txtrotate.setTextColor(Color.WHITE)
            txtAngle.setTextColor(Color.WHITE)
            txtQiblaInfo.setTextColor(Color.WHITE)
        } else {
            title.setTextColor(Color.BLACK)
            txtrotate.setTextColor(Color.BLACK)
            txtAngle.setTextColor(Color.BLACK)
            txtQiblaInfo.setTextColor(Color.BLACK)
        }
    }

    private fun triggerVibration() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(150)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        if (sensor?.type == Sensor.TYPE_ORIENTATION) {
            txtCalibration?.let { view ->
                if (accuracy <= SensorManager.SENSOR_STATUS_ACCURACY_LOW) {
                    view.text = "Rotate phone in ∞ shape"
                    view.visibility = View.VISIBLE
                } else {
                    view.visibility = View.GONE
                }
            }
        }
    }

    override fun onProviderEnabled(provider: String) {
        super.onProviderEnabled(provider)
        if (provider == LocationManager.GPS_PROVIDER) {
            startLocationUpdates()
            Toast.makeText(this, "GPS Enabled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
}