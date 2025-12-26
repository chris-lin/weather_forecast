package com.example.weatherforecast.domain.model

data class Weather(
    val cityId: Int,
    val cityName: String,
    val countryCode: String,
    val temperature: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int,
    val weatherCondition: String,
    val weatherDescription: String,
    val weatherIcon: String,
    val windSpeed: Double,
    val windDegree: Int,
    val cloudiness: Int,
    val visibility: Int,
    val sunrise: Long,
    val sunset: Long,
    val timestamp: Long
)
