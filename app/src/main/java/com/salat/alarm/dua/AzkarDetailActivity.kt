package com.salat.alarm.dua

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AzkarDetailActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences
    private val PREFS_NAME = "PrayerPrefs"
    private val DARK_MODE_KEY = "dark_mode_enabled"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_azkar)
        val back = findViewById<ImageView>(R.id.back)

        back.setOnClickListener {
            finish()
        }

        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean(DARK_MODE_KEY, false)
        val recyclerView = findViewById<RecyclerView>(R.id.azkarRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        val salahName = intent.getStringExtra("salah_name") ?: return

        val azkarList = when(salahName) {
            "الفجر" -> listOf(
                "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلّٰهِ وَالْحَمْدُ لِلّٰهِ",
                "اللّٰهُمَّ أَنْتَ رَبِّي لا إِلٰهَ إِلَّا أَنْتَ خَلَقْتَنِي وَأَنَا عَبْدُكَ",
                "سُبْحَانَ اللَّهِ وَالْحَمْدُ لِلَّهِ وَاللَّهُ أَكْبَرُ",
                "لَا إِلٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ",
                "أَسْتَغْفِرُ اللَّهَ لِي وَلِوَالِدَيَّ",
                "اللَّـهُمَّ إِنِّي أَسْتَغْفِرُكَ وَأَتُوبُ إِلَيْكَ",
                "اللَّهُمَّ أَعِنِّي عَلَى ذِكْرِكَ وَشُكْرِكَ وَحُسْنِ عِبَادَتِكَ",
                "اللّهُمَّ بَارِكْ لِي فِي يَوْمِي هذَا",
                "اللّهُمَّ إِنِّي أَسْأَلُكَ خَيْرَ مَا فِي هذَا الْيَوْمِ وَخَيْرَ مَا بَعْدَهُ",
                "وَأَعُوذُ بِكَ مِنْ شَرِّ مَا فِي هذَا الْيَوْمِ وَشَرِّ مَا بَعْدَهُ"
            )
            "الظهر" -> listOf(
                "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ",
                "سُبْحَانَ اللَّهِ الْعَظِيمِ",
                "اللَّهُمَّ اغْفِرْ لِي وَلِوَالِدَيَّ",
                "اللَّهُمَّ اغْفِرْ لِي وَلِمَنْ يَسْتَغْفِرُ لِي",
                "اللّهُمَّ إِنِّي أَسْأَلُكَ عِلْماً نَافِعاً",
                "اللَّهُمَّ اجْعَلْنِي مِنَ التَّوَّابِينَ",
                "اللَّهُمَّ اجْعَلْنِي مِنَ الْمُتَطَهِّرِينَ",
                "اللّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ الْفَقْرِ وَالْقِلَّةِ",
                "اللَّهُمَّ أَعِنِّي عَلَى ذِكْرِكَ وَشُكْرِكَ",
                "اللَّهُمَّ اجْعَلْنِي مِنَ الْمُحْسِنِينَ"
            )
            "العصر" -> listOf(
                "سُبْحَانَ اللَّهِ وَالْحَمْدُ لِلَّهِ وَاللَّهُ أَكْبَرُ",
                "أَسْتَغْفِرُ اللَّهَ لِي وَلِوَالِدَيَّ",
                "اللَّهُمَّ اجْعَلْنِي مِنَ الْمُحْسِنِينَ",
                "اللّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ",
                "اللَّهُمَّ إِنّي أَسْأَلُكَ الصِّحَّةَ وَالْعَافِيَةَ",
                "اللَّهُمَّ اغْفِرْ لِي ذُنُوبِي كُلَّهَا",
                "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ عَذَابِ الْقَبْرِ",
                "اللَّهُمَّ أَعِنِّي عَلَى أَدَاءِ فَرَائِضِكَ",
                "اللّهُمَّ اجْعَلْنَا مِنْ أَهْلِ الْجَنَّةِ",
                "اللَّهُمَّ أَعْطِنَا قَلْباً خَاشِعاً"
            )
            "المغرب" -> listOf(
                "سُبْحَانَ اللَّهِ وَالْحَمْدُ لِلَّهِ وَاللَّهُ أَكْبَرُ",
                "أَسْتَغْفِرُ اللَّهَ",
                "اللَّهُمَّ اغْفِرْ لِي وَلِوَالِدَيَّ",
                "اللّهُمَّ إِنِّي أَسْأَلُكَ الْعَفْوَ وَالْعَافِيَةَ",
                "اللَّهُمَّ أَعْفُ عَنَّا وَاعْفُ عَنِ الْمُسْلِمِينَ",
                "اللّهُمَّ احْفَظْنَا مِنْ شَرِّ مَا خَلَقْتَ",
                "اللَّهُمَّ اجْعَلْنِي مِنَ الْمُتَوَكِّلِينَ عَلَيْكَ",
                "اللَّهُمَّ رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً",
                "وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ",
                "اللَّهُمَّ اجْعَلْنَا مِنْ أَهْلِ الْجَنَّةِ"
            )
            "العشاء" -> listOf(
                "سُبْحَانَ اللَّهِ وَالْحَمْدُ لِلَّهِ وَاللَّهُ أَكْبَرُ",
                "أَسْتَغْفِرُ اللَّهَ لِي وَلِوَالِدَيَّ",
                "اللَّهُمَّ اغْفِرْ لِي ذُنُوبِي كُلَّهَا",
                "اللَّهُمَّ احْفَظْنَا فِي لَيْلَتِنَا هَذِهِ",
                "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ شَرِّ نَفْسِي",
                "اللَّهُمَّ أَعْوِذُ بِكَ مِنَ الْكَبَرِ وَالْحَسَدِ",
                "اللَّهُمَّ اجْعَلْنَا مِنْ عِبَادِكَ الصَّالِحِينَ",
                "اللَّهُمَّ اجْعَلْنَا مِنْ عِبَادِكَ الْمُقَرَّبِينَ",
                "اللَّهُمَّ أَعْطِنَا قَلْباً خَاشِعاً وَعِلْماً نَافِعاً",
                "اللَّهُمَّ ثَبِّتْنَا عَلَى دِينِكَ"
            )

            else -> listOf()
        }

        val adapter = AzkarListAdapter(azkarList)
        recyclerView.adapter = adapter

        // 🔹 Apply dark mode to adapter
        adapter.setDarkMode(isDarkMode)

        // 🔹 Set root layout background


    }

}