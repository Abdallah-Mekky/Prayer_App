package com.abdallah.alalamiyaalhuratask.ui.prayerTimes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.abdallah.alalamiyaalhuratask.R
import com.abdallah.alalamiyaalhuratask.model.CustomPrayerTiming

class PrayerTimesAdapter : Adapter<PrayerTimesAdapter.PrayerTimesViewHolder>() {


    private var prayerTimesList: List<CustomPrayerTiming>? = null

    public fun refreshAdapter(timingList: List<CustomPrayerTiming>) {
        this.prayerTimesList = timingList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrayerTimesViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.prayer_item, parent, false)
        return PrayerTimesViewHolder(view)
    }

    override fun onBindViewHolder(holder: PrayerTimesViewHolder, position: Int) {
        val currentPrayerTiming = prayerTimesList?.get(position)

        holder.prayerName.text = currentPrayerTiming?.prayerName
        holder.prayerTime.text = currentPrayerTiming?.prayerTime
    }

    override fun getItemCount(): Int {

        return prayerTimesList?.size ?: 0
    }


    class PrayerTimesViewHolder(itemView: View) : ViewHolder(itemView) {

        val prayerName: TextView = itemView.findViewById(R.id.prayerName)
        val prayerTime: TextView = itemView.findViewById(R.id.prayerTime)

    }
}