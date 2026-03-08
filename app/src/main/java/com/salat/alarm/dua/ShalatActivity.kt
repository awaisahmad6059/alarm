package com.salat.alarm.dua

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ShalatActivity : AppCompatActivity() {
    lateinit var prefs: SharedPreferences
    private val DARK_MODE_KEY = "dark_mode_enabled"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shalat)
        prefs = getSharedPreferences("PrayerPrefs", Context.MODE_PRIVATE)

        val label = findViewById<TextView>(R.id.label)

        val recycler = findViewById<RecyclerView>(R.id.recyclerLesson)
        recycler.layoutManager = LinearLayoutManager(this)

        val list = listOf(

            Lesson(
                "Takbeer-e-Tehreema",
                "اللّٰهُ أَكْبَرُ"
            ),

            Lesson(
                "Sana",
                "سُبْحَانَكَ اللّٰهُمَّ وَبِحَمْدِكَ وَتَبَارَكَ اسْمُكَ وَتَعَالَى جَدُّكَ وَلَا إِلٰهَ غَيْرُكَ"
            ),

            Lesson(
                "Tawooz",
                "أَعُوذُ بِاللّٰهِ مِنَ الشَّيْطَانِ الرَّجِيمِ"
            ),

            Lesson(
                "Tasmeiah",
                "بِسْمِ اللّٰهِ الرَّحْمٰنِ الرَّحِيمِ"
            ),

            Lesson(
                "Surah Fatiha",
                "الْحَمْدُ لِلّٰهِ رَبِّ الْعَالَمِينَ ۝ الرَّحْمٰنِ الرَّحِيمِ ۝ مَالِكِ يَوْمِ الدِّينِ ۝ إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ ۝ اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ ۝ صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ"
            ),

            Lesson(
                "Surah Ikhlas",
                "قُلْ هُوَ اللّٰهُ أَحَدٌ ۝ اللّٰهُ الصَّمَدُ ۝ لَمْ يَلِدْ وَلَمْ يُولَدْ ۝ وَلَمْ يَكُنْ لَهُ كُفُوًا أَحَدٌ"
            ),

            Lesson(
                "Tasbeeh e Rukuh",
                "سُبْحَانَ رَبِّيَ الْعَظِيمِ"
            ),

            Lesson(
                "Tasmee",
                "سَمِعَ اللّٰهُ لِمَنْ حَمِدَهُ"
            ),

            Lesson(
                "Tahmeed",
                "رَبَّنَا وَلَكَ الْحَمْدُ"
            ),

            Lesson(
                "Tasbeeh e Sajda",
                "سُبْحَانَ رَبِّيَ الْأَعْلَى"
            ),

            Lesson(
                "Attahiyaat",
                "التَّحِيَّاتُ لِلّٰهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ ۝ السَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللّٰهِ وَبَرَكَاتُهُ ۝ السَّلَامُ عَلَيْنَا وَعَلَىٰ عِبَادِ اللّٰهِ الصَّالِحِينَ ۝ أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللّٰهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ"
            ),

            Lesson(
                "Durood e Ibrahim",
                "اللّٰهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ ۝ اللّٰهُمَّ بَارِكْ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ كَمَا بَارَكْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ"
            ),

            Lesson(
                "Dua Masoora",
                "اللّٰهُمَّ إِنِّي ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا وَلَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ فَاغْفِرْ لِي مَغْفِرَةً مِّنْ عِنْدِكَ وَارْحَمْنِي إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ"
            ),

            Lesson(
                "Salam",
                "السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللّٰهِ"
            ),

            Lesson(
                "Dua e Qunoot",
                "اللّٰهُمَّ اهْدِنَا فِيمَنْ هَدَيْتَ وَعَافِنَا فِيمَنْ عَافَيْتَ وَتَوَلَّنَا فِيمَنْ تَوَلَّيْتَ وَبَارِكْ لَنَا فِيمَا أَعْطَيْتَ وَقِنَا وَاصْرِفْ عَنَّا شَرَّ مَا قَضَيْتَ فَإِنَّكَ تَقْضِي وَلَا يُقْضَىٰ عَلَيْكَ إِنَّهُ لَا يَذِلُّ مَنْ وَالَيْتَ وَلَا يَعِزُّ مَنْ عَادَيْتَ تَبَارَكْتَ رَبَّنَا وَتَعَالَيْتَ"
            )
        )

        recycler.adapter = LessonAdapter(list)

        val isDark = prefs.getBoolean(DARK_MODE_KEY,false)

        if(isDark){

            label.setTextColor(Color.WHITE)

        }else{

            label.setTextColor(Color.BLACK)

        }

        findViewById<ImageView>(R.id.back).setOnClickListener {
            finish()
        }
    }
}