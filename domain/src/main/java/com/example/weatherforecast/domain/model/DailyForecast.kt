package com.example.weatherforecast.domain.model

data class DailyForecast(
    val date: String,
    val tempMin: Double,
    val tempMax: Double,
    val weatherCondition: String,
    val weatherDescription: String,
    val weatherIcon: String,
    val humidity: Int,
    val windSpeed: Double,
    val pressure: Int,
    val precipitationProbability: Double
)
