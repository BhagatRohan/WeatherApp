package com.example.weatherapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.models.CurrentWeather
import com.example.weatherapp.repository.ForecastRepository


class CurrentForecastViewModelFactory(private val repository: ForecastRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrentForecastViewModel::class.java)) {
            return CurrentForecastViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class CurrentForecastViewModel(private val repository: ForecastRepository) : ViewModel() {

    private var _currentWeather = MutableLiveData<CurrentWeather>()
    val currentWeather: LiveData<CurrentWeather> = _currentWeather

    fun loadCurrentForecast(zipCode: String) = repository.loadCurrentForecast(zipCode, _currentWeather)


}