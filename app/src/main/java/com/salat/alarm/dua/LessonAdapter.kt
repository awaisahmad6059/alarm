package com.salat.alarm.dua

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LessonAdapter(private val list: List<Lesson>) :
    RecyclerView.Adapter<LessonAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.txtTitle)
        val text: TextView = view.findViewById(R.id.txtArabic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lesson, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.title.text = item.title
        holder.text.text = item.text

        val prefs =
            holder.itemView.context.getSharedPreferences("PrayerPrefs", Context.MODE_PRIVATE)
        val dark = prefs.getBoolean("dark_mode_enabled", false)

        val card = holder.itemView.findViewById<androidx.cardview.widget.CardView>(R.id.card)

        if (dark) {

            card.setCardBackgroundColor(Color.parseColor("#436399FF"))
            holder.title.setTextColor(Color.WHITE)
            holder.text.setTextColor(Color.WHITE)

        } else {

            card.setCardBackgroundColor(Color.WHITE)
            holder.title.setTextColor(Color.BLACK)
            holder.text.setTextColor(Color.BLACK)

        }
    }
}