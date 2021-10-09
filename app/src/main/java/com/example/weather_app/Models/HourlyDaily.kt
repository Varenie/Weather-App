import com.example.weather_app.Models.Weather
import com.google.gson.annotations.SerializedName



data class HourlyDaily (

	@SerializedName("lat")
	val lat : Double,
	@SerializedName("lon")
	val lon : Double,
	@SerializedName("timezone")
	val timezone : String,
	@SerializedName("timezone_offset")
	val timezone_offset : Int,
	@SerializedName("hourly")
	val hourly : List<Hourly>,
	@SerializedName("daily")
	val daily : List<Daily>
)

data class Temp (

	@SerializedName("day")
	val day : Double,
	@SerializedName("min")
	val min : Double,
	@SerializedName("max")
	val max : Double,
	@SerializedName("night")
	val night : Double,
	@SerializedName("eve")
	val eve : Double,
	@SerializedName("morn")
	val morn : Double
)

data class Hourly (

	@SerializedName("dt")
	val dt : Int,
	@SerializedName("temp")
	val temp : Double,
	@SerializedName("feels_like")
	val feels_like : Double,
	@SerializedName("pressure")
	val pressure : Int,
	@SerializedName("humidity")
	val humidity : Int,
	@SerializedName("dew_point")
	val dew_point : Double,
	@SerializedName("clouds")
	val clouds : Int,
	@SerializedName("visibility")
	val visibility : Int,
	@SerializedName("wind_speed")
	val wind_speed : Double,
	@SerializedName("wind_deg")
	val wind_deg : Int,
	@SerializedName("weather")
	val weather : List<Weather>,
)

data class Daily (

	@SerializedName("dt")
	val dt : Int,
	@SerializedName("sunrise")
	val sunrise : Int,
	@SerializedName("sunset")
	val sunset : Int,
	@SerializedName("moonrise")
	val moonrise : Int,
	@SerializedName("moonset")
	val moonset : Int,
	@SerializedName("moon_phase")
	val moon_phase : Double,
	@SerializedName("temp")
	val temp : Temp,
	@SerializedName("pressure")
	val pressure : Int,
	@SerializedName("humidity")
	val humidity : Int,
	@SerializedName("dew_point")
	val dew_point : Double,
	@SerializedName("wind_speed")
	val wind_speed : Double,
	@SerializedName("wind_deg")
	val wind_deg : Int,
	@SerializedName("weather")
	val weather : List<Weather>,
	@SerializedName("clouds")
	val clouds : Int,
)