package com.example.weather_app.Retrofit

object Common {
    private val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    val retrofitService: RetrofitService
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitService::class.java)
}