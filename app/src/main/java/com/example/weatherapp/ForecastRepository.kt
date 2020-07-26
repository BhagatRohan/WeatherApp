package com.example.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.random.Random

class ForecastRepository {

    private val _weeklyForecast = MutableLiveData<List<DailyForecast>>()
    val weeklyForecast: LiveData<List<DailyForecast>>
        get() = _weeklyForecast

    fun loadForecast(zipcode: String) {
        val randomValues = List(10) { Random.nextFloat().rem(100) * 100 }

        val forecastItems = randomValues.map {
            DailyForecast(it, getDescription(it))
        }

        _weeklyForecast.value = forecastItems
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