package com.example.weatherforecast.data.remote.dto

data class CurrentWeatherDto(
    val coord: CoordDto,
    val weather: List<WeatherDto>,
    val base: String,
    val main: MainDto,
    val visibility: Int,
    val wind: WindDto,
    val clouds: CloudsDto,
    val dt: Long,
    val sys: SysDto,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
)

data class SysDto(
    val type: Int? = null,
    val id: Int? = null,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)
