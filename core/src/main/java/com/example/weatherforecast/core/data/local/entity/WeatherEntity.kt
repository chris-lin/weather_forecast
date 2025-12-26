package com.example.weatherforecast.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey
    val cityId: Int,
    val cityName: String,
    val countryCode: String,
    val latitude: Double,
    val longitude: Double,
    val temperature: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int,
    val weatherMain: String,
    val weatherDescription: String,
    val weatherIcon: String,
    val windSpeed: Double,
    val windDegree: Int,
    val cloudiness: Int,
    val visibility: Int,
    val sunrise: Long,
    val sunset: Long,
    val timestamp: Long,
    val isCurrentLocation: Boolean = false
)
