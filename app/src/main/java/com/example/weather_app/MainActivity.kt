package com.example.weather_app

import Daily
import Hourly
import HourlyDaily
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.Adapters.RecyclerDailyAdapter
import com.example.weather_app.Adapters.RecyclerHourlyAdapter
import com.example.weather_app.Models.Coord
import com.example.weather_app.Models.Main
import com.example.weather_app.Models.Weather
import com.example.weather_app.Models.WeatherAll
import com.example.weather_app.Retrofit.Common
import com.example.weather_app.Retrofit.RetrofitService
import com.example.weather_app.databinding.ActivityMainBinding
import com.google.android.gms.location.*
import com.squareup.picasso.Picasso
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var mService: RetrofitService
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest

    private val PERMISSION_ID = 101
    private val TAG = "MYCHECK"
    private val BASE_URL_ICON = "https://openweathermap.org/img/wn/"
    private val DEGREE: String = "°"

    private var apiKey = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        mService = Common.retrofitService
        apiKey = this.resources.getString(R.string.api_key)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()

        val searchView = binding.searchView

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                query?.let {
                    getWeatherByName(it)
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

    }

    //пока нет геолокации, по дефолту Мосвка
    private fun getWeatherByName(city: String = "москва") {
        mService.getCurrentWeatherByCityName(city, apiKey).enqueue(object : Callback<WeatherAll> {
            override fun onFailure(call: Call<WeatherAll>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

            override fun onResponse(call: Call<WeatherAll>, response: Response<WeatherAll>) {
                Log.d(TAG, response.body()?.main.toString())

                val weatherAll = response.body()

                weatherAll?.let {
                    setCurrentWeather(it.weather, it.main, it.name)

                    getHourlyDailyWeather(it.coord)
                }


            }
        })
    }

    private fun getWeatherByCoord(lat: Double, lon: Double) {
        mService.getCurrentWeatherByCoord(lat, lon, apiKey).enqueue(object : Callback<WeatherAll> {
            override fun onFailure(call: Call<WeatherAll>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

            override fun onResponse(call: Call<WeatherAll>, response: Response<WeatherAll>) {
                Log.d(TAG, response.body()?.main.toString())

                val weatherAll = response.body()

                weatherAll?.let {
                    setCurrentWeather(it.weather, it.main, it.name)

                    getHourlyDailyWeather(it.coord)
                }


            }
        })
    }



    private fun getHourlyDailyWeather(coord: Coord) {
        mService.getHourlyDailyWeather(lat = coord.lat, lon = coord.lon, apiKey = apiKey).enqueue(object: Callback<HourlyDaily> {
            override fun onResponse(call: Call<HourlyDaily>, response: Response<HourlyDaily>) {
                val hourlyDaily = response.body()

                hourlyDaily?.let {
                    setRecyclers(it.hourly, it.daily)
                }
            }

            override fun onFailure(call: Call<HourlyDaily>, t: Throwable) {
                Log.e("${TAG}HOURLY", t.message.toString())
            }

        })
    }

    fun setRecyclers(hourly: List<Hourly>, daily: List<Daily>) {
        val recyclerHourly = binding.rvHourlyWeather
        val recyclerDaily = binding.rvDailyTemp

        val adapterHourly = RecyclerHourlyAdapter(hourly)
        val adapterDaily = RecyclerDailyAdapter(daily)

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

    private fun checkPermissions(): Boolean {

        if ( ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }

        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //for debug
        if(requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "You have permissiond")
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location: Location = task.result

                    if (location == null) {
                        getNewLocation()
                    } else {
                        getWeatherByCoord(location.latitude, location.longitude)
                    }
                }
            } else {
                Toast.makeText(this, "Enable Location", Toast.LENGTH_SHORT).show()
            }
        } else {
            requestPermission()

            getLastLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getNewLocation() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 2


        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    private val locationCallback = object: LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            val lastLocation: Location? = p0.lastLocation
            lastLocation?.let {
                getWeatherByCoord(it.latitude, it.longitude)
            }

        }
    }

    override fun onBackPressed() {
        getLastLocation()
    }

}

