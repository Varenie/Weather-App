package com.example.weather_app.Retrofit

object Common {
    private val BASE_URL = "https://api.openweathermap.org/"

    val retrofitService: RetrofitService
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitService::class.java)
}