package com.salat.alarm.dua


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SalahAdapter(
    private val items: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<SalahAdapter.SalahViewHolder>() {

    private var isDarkMode = false

    fun setDarkMode(enabled: Boolean) {
        isDarkMode = enabled
        notifyDataSetChanged()
    }
    class SalahViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtSalah: TextView = itemView.findViewById(R.id.txtZikr)
        val card: androidx.cardview.widget.CardView =
            itemView.findViewById(R.id.itemcard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalahViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_azkar, parent, false)
        return SalahViewHolder(view)
    }

    override fun onBindViewHolder(holder: SalahViewHolder, position: Int) {
        holder.txtSalah.text = items[position]
        if (isDarkMode) {
            holder.card.setCardBackgroundColor(Color.parseColor("#436399FF"))
            holder.txtSalah.setTextColor(Color.WHITE)
        } else {
            holder.card.setCardBackgroundColor(Color.WHITE)
            holder.txtSalah.setTextColor(Color.BLACK)
        }
        holder.itemView.setOnClickListener {
            onClick(items[position])
        }
    }

    override fun getItemCount(): Int = items.size
}