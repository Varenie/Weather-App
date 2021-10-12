package com.example.weather_app.Retrofit

import HourlyDaily
import com.example.weather_app.Models.WeatherAll
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {
    @GET("weather")
    fun getCurrentWeatherByCityName(@Query("q") city: String,
                                @Query("appid") apiKey: String,
                                @Query("units") units: String = "metric",
                                @Query("lang") lang: String = "ru"
    ): Call<WeatherAll>

    @GET("weather")
    fun getCurrentWeatherByCoord(@Query("lat") lat: Double,
                             @Query("lon") lon: Double,
                             @Query("appid") apiKey: String,
                             @Query("units") units: String = "metric",
                             @Query("lang") lang: String = "ru"
    ): Call<WeatherAll>

    @GET("onecall")
    fun getHourlyDailyWeather(@Query("lat") lat: Double,
                         @Query("lon") lon: Double,
                         @Query("exclude") exclude: String = "minutely,current",
                         @Query("appid") apiKey: String,
                         @Query("units") units: String = "metric",
                         @Query("lang") lang: String = "ru"
    ): Call<HourlyDaily>


}