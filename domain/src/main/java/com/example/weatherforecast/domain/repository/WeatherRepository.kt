package com.example.weatherforecast.domain.repository

import com.example.weatherforecast.domain.model.DailyForecast
import com.example.weatherforecast.domain.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getCurrentWeather(cityId: Int): Flow<Result<Weather>>
    fun getCurrentWeatherByLocation(lat: Double, lon: Double): Flow<Result<Weather>>
    fun getWeeklyForecast(cityId: Int): Flow<Result<List<DailyForecast>>>
    suspend fun refreshWeatherData(cityId: Int): Result<Unit>
    suspend fun refreshWeatherByLocation(lat: Double, lon: Double): Result<Unit>
}
