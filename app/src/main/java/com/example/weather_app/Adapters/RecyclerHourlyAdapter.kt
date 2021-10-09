package com.example.weather_app.Adapters

import Hourly
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app.databinding.RecyclerItemHourlyTempBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class RecyclerHourlyAdapter(val hourly: List<Hourly>): RecyclerView.Adapter<RecyclerHourlyAdapter.MyViewHolder>() {
    private var binding: RecyclerItemHourlyTempBinding? = null

    class MyViewHolder(binding: RecyclerItemHourlyTempBinding): RecyclerView.ViewHolder(binding.root) {
        val time = binding.tvTime
        val icon = binding.ivIconHourly
        val temp = binding.tvHourlyTemp

        private val BASE_URL_ICON = "https://openweathermap.org/img/wn/"
        private val hourFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)

        fun bind(hourly: Hourly) {
            time.text = hourFormat.format(hourly.dt)

            val url  ="$BASE_URL_ICON${hourly.weather[0].icon}.png"
            Picasso.get().load(url).into(icon)

            temp.text = hourly.temp.toInt().toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = RecyclerItemHourlyTempBinding.inflate(inflater, parent, false)

        return MyViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(hourly[position])
    }

    override fun getItemCount(): Int {
        return hourly.size
    }

}