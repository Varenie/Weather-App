package com.example.weather_app.Retrofit

import com.example.weather_app.Models.WeatherAll
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {
    @GET("data/2.5/weather")
    fun getCurrentWeather(@Query("q") city: String,
                          @Query("appid") apiKey: String,
                          @Query("units") units: String = "metric",
                          @Query("lang") lang: String = "ru"
    ): Call<WeatherAll>
}