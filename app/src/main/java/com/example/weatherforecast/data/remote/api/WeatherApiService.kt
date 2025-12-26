package com.example.weatherforecast.data.remote.api

import com.example.weatherforecast.data.remote.dto.CurrentWeatherDto
import com.example.weatherforecast.data.remote.dto.ForecastDto
import com.example.weatherforecast.data.remote.dto.GeocodingDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("id") cityId: Int,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): CurrentWeatherDto

    @GET("data/2.5/weather")
    suspend fun getCurrentWeatherByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): CurrentWeatherDto

    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("id") cityId: Int,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): ForecastDto

    @GET("data/2.5/forecast")
    suspend fun getForecastByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): ForecastDto

    @GET("geo/1.0/direct")
    suspend fun searchCities(
        @Query("q") query: String,
        @Query("limit") limit: Int = 5,
        @Query("appid") apiKey: String
    ): List<GeocodingDto>

    @GET("geo/1.0/reverse")
    suspend fun getCityByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("limit") limit: Int = 1,
        @Query("appid") apiKey: String
    ): List<GeocodingDto>
}
