package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.random.Random

class ForecastRepository {

    private val _currentWeather = MutableLiveData<CurrentWeather>()
    val currentWeather: LiveData<CurrentWeather> = _currentWeather

    private val _weeklyForecast = MutableLiveData<WeeklyForecast>()
    val weeklyForecast: LiveData<WeeklyForecast> = _weeklyForecast



    fun loadWeeklyForecast(zipcode: String) {
        val updatedZipWithcountry = "$zipcode,in"
        val call = createOpenWeatherMapService().currentWeather(updatedZipWithcountry,"imperial", BuildConfig.OPEN_WEATHER_MAP_API_KEY)

        call.enqueue(object : Callback<CurrentWeather>{
            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Log.e(ForecastRepository::class.java.simpleName, "error loading location weekly forecast" , t)
            }

            override fun onResponse(
                call: Call<CurrentWeather>,
                response: Response<CurrentWeather>
            ) {
                val weatherResponse = response.body()
                if (weatherResponse !=null){
                    val forecastCall = createOpenWeatherMapService().sevenDayForecast(
                        lat = weatherResponse.coord.lat,
                        lon = weatherResponse.coord.lon,
                        exclude = "current,minutely,hourly",
                        units = "imperial",
                        apiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY
                    )
                    forecastCall.enqueue(object : Callback<WeeklyForecast>{
                        override fun onFailure(call: Call<WeeklyForecast>, t: Throwable) {
                            Log.e(ForecastRepository::class.java.simpleName, "error loading weekly forecast" , t)
                        }

                        override fun onResponse(
                            call: Call<WeeklyForecast>,
                            response: Response<WeeklyForecast>
                        ) {
                            val weeklyForecastResponse = response.body()
                            if (weeklyForecastResponse!=null){
                                _weeklyForecast.value = weeklyForecastResponse
                            }
                        }

                    })
                }
            }

        })

    }

    fun loadCurrentForecast(zipcode: String){
        val updatedZipWithcountry = "$zipcode,in"
        val call = createOpenWeatherMapService().currentWeather(updatedZipWithcountry, "imperial", BuildConfig.OPEN_WEATHER_MAP_API_KEY)
        call.enqueue(object : Callback<CurrentWeather>{
            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
               Log.e(ForecastRepository::class.java.simpleName, "error loading current weather" , t)
            }

            override fun onResponse(
                call: Call<CurrentWeather>,
                response: Response<CurrentWeather>
            ) {
                val weatherResponse = response.body()
                if (weatherResponse !=null){
                    _currentWeather.value = weatherResponse
                }
            }

        })
    }

    private fun getDescription(temperature: Float): String {
        return when (temperature) {
            in Float.MIN_VALUE..0f -> "Anything below 0 doesn't make sense"
            in 0f..32f -> "Way too cold"
            in 32f..55f -> "Colder than I would prefer"
            in 55f..65f -> "Getting Better"
            in 65f..80f -> "That's the sweet spot!"
            in 80f..90f -> "Getting a little warm"
            in 90f..100f -> "Where is the A/C?"
            in 100f..Float.MAX_VALUE -> "What is this, Arizona?"
            else -> "Does not compute"

        }
    }
}