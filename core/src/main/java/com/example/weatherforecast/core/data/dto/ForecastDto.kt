package com.example.weatherforecast.core.data.remote.dto

data class ForecastDto(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<ForecastItemDto>,
    val city: CityDto
)

data class ForecastItemDto(
    val dt: Long,
    val main: MainDto,
    val weather: List<WeatherDto>,
    val clouds: CloudsDto,
    val wind: WindDto,
    val visibility: Int,
    val pop: Double,
    val sys: SysForecastDto,
    val dt_txt: String
)

data class SysForecastDto(
    val pod: String
)

data class CityDto(
    val id: Int,
    val name: String,
    val coord: CoordDto,
    val country: String,
    val population: Int? = null,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)
