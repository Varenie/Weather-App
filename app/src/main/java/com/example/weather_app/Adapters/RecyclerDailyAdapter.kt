package com.example.weather_app.Adapters

import Daily
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app.databinding.RecyclerItemDailyTempBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class RecyclerDailyAdapter(val daily: List<Daily>): RecyclerView.Adapter<RecyclerDailyAdapter.MyViewHolder>() {
    private var binding: RecyclerItemDailyTempBinding? = null

    class MyViewHolder(binding: RecyclerItemDailyTempBinding): RecyclerView.ViewHolder(binding.root) {
        val date = binding.tvDate
        val icon = binding.ivIconDaily
        val temp = binding.tvDailyTemp

        private val BASE_URL_ICON = "https://openweathermap.org/img/wn/"
        private val dateFormat = SimpleDateFormat("EE, MMM dd", Locale.ENGLISH)

        fun bind(daily: Daily) {
            date.text = dateFormat.format(Date(daily.dt.toLong() * 1000))

            val url = "$BASE_URL_ICON${daily.weather[0].icon}.png"
            Picasso.get().load(url).into(icon)

            temp.text = daily.temp.day.toInt().toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = RecyclerItemDailyTempBinding.inflate(inflater, parent, false)

        return MyViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(daily[position])
    }

    override fun getItemCount(): Int {
        return daily.size
    }
}