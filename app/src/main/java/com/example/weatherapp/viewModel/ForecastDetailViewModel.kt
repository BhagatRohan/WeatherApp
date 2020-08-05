package com.example.weatherapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.models.ForecastDetailViewState
import com.example.weatherapp.ui.ForecastDetailsFragmentArgs
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*

private val DATE_FORMAT = SimpleDateFormat("MM-dd-yyyy")


class ForecastDetailsViewModelFactory(private val args: ForecastDetailsFragmentArgs): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForecastDetailViewModel::class.java)){
            return ForecastDetailViewModel(args) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


class ForecastDetailViewModel(args: ForecastDetailsFragmentArgs): ViewModel() {

    private val _viewState: MutableLiveData<ForecastDetailViewState> = MutableLiveData()
    val viewState: LiveData<ForecastDetailViewState>  = _viewState

    init {
        _viewState.value = ForecastDetailViewState(
            temp = args.temp,
            description = args.desc,
            date = DATE_FORMAT.format(Date(args.date * 1000)),
            icons = "http://openweathermap.org/img/wn/${args.icon}@2x.png"
        )
    }

}