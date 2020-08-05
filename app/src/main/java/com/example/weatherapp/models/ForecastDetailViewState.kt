package com.example.weatherapp.models

data class ForecastDetailViewState(
    val temp: Float,
    val description: String,
    val date: String,
    val icons: String
)