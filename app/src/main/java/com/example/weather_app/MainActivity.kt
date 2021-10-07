package com.example.weather_app

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.Adapters.RecyclerDailyAdapter
import com.example.weather_app.Adapters.RecyclerHourlyAdapter
import com.example.weather_app.Models.Main
import com.example.weather_app.Models.Weather
import com.example.weather_app.Models.WeatherAll
import com.example.weather_app.Retrofit.Common
import com.example.weather_app.Retrofit.RetrofitService
import com.example.weather_app.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.contracts.Returns


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var mService: RetrofitService

    private val TAG = "MYCHECK"
    private val BASE_URL_ICON = "https://openweathermap.org/img/wn/"
    private val DEGREE: String = "°"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        mService = Common.retrofitService

        setRecyclers()
        getWeather()

        val searchView = binding.searchView

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                query?.let {
                    getWeather(it)
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

    }

    //пока нет геолокации, по дефолту Мосвка
    private fun getWeather(city: String = "москва") {
        val apiKey = this.resources.getString(R.string.api_key)


        mService.getCurrentWeather(city, apiKey).enqueue(object : Callback<WeatherAll> {
            override fun onFailure(call: Call<WeatherAll>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

            override fun onResponse(call: Call<WeatherAll>, response: Response<WeatherAll>) {
                Log.e(TAG, response.body()?.main.toString())

                val weatherAll = response.body()

                weatherAll?.let {
                    setCurrentWeather(it.weather, it.main, it.name)
                }

//                getHourlyWeather()
//                getDailyWeather()
            }
        })



    }

    fun setRecyclers() {
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

    private fun setCurrentWeather(weather: List<Weather>, main: Main, city: String) {
        val tvCity = binding.tvCity

        val icon = binding.ivWeatherIcon
        val description = binding.tvWeatherDescription

        val currentTemp = binding.tvCurrentTemp
        val tempMinMax = binding.tvMinMaxTemp

        val url = "$BASE_URL_ICON${weather[0].icon}.png"
        Log.d(TAG, url)

        tvCity.text = city

        Picasso.get().load(url).fit().error(R.drawable.ic_launcher_background).into(icon)
        description.text = weather[0].description

        currentTemp.text = "${main.temp.toInt()}$DEGREE"
        tempMinMax.text = "${main.temp_max.toInt()}$DEGREE/${main.temp_min.toInt()}$DEGREE"
    }
}

