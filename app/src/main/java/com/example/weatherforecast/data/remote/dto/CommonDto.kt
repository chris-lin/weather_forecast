package com.example.weatherforecast.data.remote.dto

data class CoordDto(
    val lon: Double,
    val lat: Double
)

data class WeatherDto(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class MainDto(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val sea_level: Int? = null,
    val grnd_level: Int? = null
)

data class WindDto(
    val speed: Double,
    val deg: Int,
    val gust: Double? = null
)

data class CloudsDto(
    val all: Int
)
