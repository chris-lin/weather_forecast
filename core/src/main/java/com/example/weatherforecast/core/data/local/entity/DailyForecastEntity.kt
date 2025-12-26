package com.example.weatherforecast.core.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "daily_forecast",
    primaryKeys = ["cityId", "date"]
)
data class DailyForecastEntity(
    val cityId: Int,
    val cityName: String,
    val date: String,
    val tempMin: Double,
    val tempMax: Double,
    val weatherMain: String,
    val weatherDescription: String,
    val weatherIcon: String,
    val humidity: Int,
    val windSpeed: Double,
    val pressure: Int,
    val pop: Double,
    val timestamp: Long
)
