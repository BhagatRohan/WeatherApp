package com.example.weatherapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.createOpenWeatherMapService
import com.example.weatherapp.models.CurrentWeather
import com.example.weatherapp.models.WeeklyForecast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForecastRepository {

    fun loadWeeklyForecast(zipcode: String, _weeklyForecast: MutableLiveData<WeeklyForecast>) {
        val zipcodeWithCountryCode = "$zipcode,in"
        val call = createOpenWeatherMapService().currentWeather(
            zipcodeWithCountryCode, "Metric",
            BuildConfig.OPEN_WEATHER_MAP_API_KEY
        )

        call.enqueue(object : Callback<CurrentWeather> {
            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Log.e(ForecastRepository::class.java.simpleName, "error loading location weekly forecast", t)
            }

            override fun onResponse(call: Call<CurrentWeather>, response: Response<CurrentWeather>) {
                val weatherResponse = response.body()
                if (weatherResponse != null) {
                    val forecastCall = createOpenWeatherMapService().sevenDayForecast(
                            lat = weatherResponse.coord.lat,
                            lon = weatherResponse.coord.lon,
                            exclude = "current,minutely,hourly",
                            units = "Metric",
                            apiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY
                        )

                    forecastCall.enqueue(object : Callback<WeeklyForecast> {
                        override fun onFailure(call: Call<WeeklyForecast>, t: Throwable) {
                            Log.e(ForecastRepository::class.java.simpleName, "error loading weekly forecast", t)
                        }

                        override fun onResponse(call: Call<WeeklyForecast>, response: Response<WeeklyForecast>) {
                            val weeklyForecastResponse = response.body()
                            if (weeklyForecastResponse != null) {
                                _weeklyForecast.value = weeklyForecastResponse
                            }
                        }
                    })
                }
            }
        })
    }

    fun loadCurrentForecast(zipcode: String, _currentWeather: MutableLiveData<CurrentWeather>) {
        val zipcodeWithCountryCode = "$zipcode,in"
        val call = createOpenWeatherMapService().currentWeather(
            zipcodeWithCountryCode, "Metric",
            BuildConfig.OPEN_WEATHER_MAP_API_KEY
        )

        call.enqueue(object : Callback<CurrentWeather> {
            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Log.e(ForecastRepository::class.java.simpleName, "error loading current weather", t)
            }

            override fun onResponse(call: Call<CurrentWeather>, response: Response<CurrentWeather>) {
                val weatherResponse = response.body()
                if (weatherResponse != null) {
                    _currentWeather.value = weatherResponse
                }
            }
        })
    }
}