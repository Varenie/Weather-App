package com.example.weather_app

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.Adapters.RecyclerDailyAdapter
import com.example.weather_app.Adapters.RecyclerHourlyAdapter
import com.example.weather_app.Models.Weather
import com.example.weather_app.Models.WeatherAll
import com.example.weather_app.Retrofit.Common
import com.example.weather_app.Retrofit.RetrofitService
import com.example.weather_app.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.contracts.Returns


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var mService: RetrofitService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        mService = Common.retrofitService

        val recyclerHourly = binding.rvHourlyWeather
        val recyclerDaily = binding.rvDailyTemp

        val adapterHourly = RecyclerHourlyAdapter()
        val adapterDaily = RecyclerDailyAdapter()

        recyclerHourly.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerDaily.layoutManager = LinearLayoutManager(this)
        recyclerHourly.setHasFixedSize(true)
        recyclerDaily.setHasFixedSize(true)
        recyclerHourly.adapter = adapterHourly
        recyclerDaily.adapter = adapterDaily

    }

    private fun getWeather() {
        val apiKey = this.resources.getString(R.string.api_key)


        mService.getCurrentWeather("москва", apiKey).enqueue(object : Callback<WeatherAll> {
            override fun onFailure(call: Call<WeatherAll>, t: Throwable) {
                Log.e("MYTAG", t.message.toString())
            }

            override fun onResponse(call: Call<WeatherAll>, response: Response<WeatherAll>) {
                Log.e("MYTAG", response.body()?.main.toString())
            }
        })


    }
}

