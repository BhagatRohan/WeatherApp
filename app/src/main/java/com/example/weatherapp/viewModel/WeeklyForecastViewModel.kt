package com.example.weatherapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.models.WeeklyForecast
import com.example.weatherapp.repository.ForecastRepository

class WeeklyForecastViewModelFactory(private val repository: ForecastRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeeklyForecastViewModel::class.java)) {
            return WeeklyForecastViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class WeeklyForecastViewModel(private val repository: ForecastRepository) : ViewModel() {

    private var _weeklyWeather = MutableLiveData<WeeklyForecast>()
    val weeklyWeather: LiveData<WeeklyForecast> = _weeklyWeather

    fun loadWeeklyForecast(zipCode: String) = repository.loadWeeklyForecast(zipCode, _weeklyWeather)


}