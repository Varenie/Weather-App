package com.example.weather_app.Models

import com.google.gson.annotations.SerializedName

data class WeatherAll (

	@SerializedName("coord")
	val coord: Coord,
	@SerializedName("weather")
	val weather : List<Weather>,
	@SerializedName("main")
	val main : Main,
	@SerializedName("visibility")
	val visibility : Int,
	@SerializedName("wind")
	val wind : Wind,
	@SerializedName("clouds")
	val clouds : Clouds,
	@SerializedName("name")
	val name : String,
)

data class Coord (

	@SerializedName("lon")
	val lon : Double,
	@SerializedName("lat")
	val lat : Double
)

data class Weather (

	@SerializedName("id")
	val id : Int,
	@SerializedName("main")
	val main : String,
	@SerializedName("description")
	val description : String,
	@SerializedName("icon")
	val icon : String
)

data class Main (

	@SerializedName("temp")
	val temp : Double,
	@SerializedName("pressure")
	val pressure : Int,
	@SerializedName("humidity")
	val humidity : Int,
	@SerializedName("temp_min")
	val temp_min : Double,
	@SerializedName("temp_max")
	val temp_max : Double
)

data class Wind (

	@SerializedName("speed")
	val speed : Double,
	@SerializedName("deg")
	val deg : Int
)

data class Clouds (
	@SerializedName("all")
	val all : Int
)