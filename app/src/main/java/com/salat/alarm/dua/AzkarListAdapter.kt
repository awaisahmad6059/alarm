package com.salat.alarm.dua

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AzkarListAdapter(private val azkar: List<String>) :
    RecyclerView.Adapter<AzkarListAdapter.AzkarViewHolder>() {
    private var isDarkMode = false

    fun setDarkMode(enabled: Boolean) {
        isDarkMode = enabled
        notifyDataSetChanged()
    }

    class AzkarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtZikr: TextView = itemView.findViewById(R.id.txtZikr)
        val card: androidx.cardview.widget.CardView = itemView.findViewById(R.id.itemcard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AzkarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_azkar_detail, parent, false)
        return AzkarViewHolder(view)
    }

    override fun onBindViewHolder(holder: AzkarViewHolder, position: Int) {
        val zikrText = "${position + 1}. ${azkar[position]}"
        holder.txtZikr.apply {
            text = zikrText
            isSingleLine = false
            setHorizontallyScrolling(false)
            ellipsize = null
            setLineSpacing(4f, 1f)
        }
        if (isDarkMode) {
            holder.card.setCardBackgroundColor(Color.parseColor("#436399FF"))
            holder.txtZikr.setTextColor(Color.WHITE)
        } else {
            holder.card.setCardBackgroundColor(Color.WHITE)
            holder.txtZikr.setTextColor(Color.BLACK)
        }
    }

    override fun getItemCount(): Int = azkar.size
}