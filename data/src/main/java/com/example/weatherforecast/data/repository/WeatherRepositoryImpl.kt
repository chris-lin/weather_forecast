package com.example.weatherforecast.data.repository

import com.example.weatherforecast.core.data.local.dao.CityDao
import com.example.weatherforecast.core.data.local.dao.DailyForecastDao
import com.example.weatherforecast.core.data.local.dao.WeatherDao
import com.example.weatherforecast.data.mapper.ForecastMapper
import com.example.weatherforecast.data.mapper.ForecastMapper.toDomain
import com.example.weatherforecast.data.mapper.ForecastMapper.toEntities
import com.example.weatherforecast.data.mapper.WeatherMapper.toDomain
import com.example.weatherforecast.data.mapper.WeatherMapper.toEntity
import com.example.weatherforecast.core.data.remote.api.WeatherApiService
import com.example.weatherforecast.domain.model.DailyForecast
import com.example.weatherforecast.domain.model.Weather
import com.example.weatherforecast.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApiService: WeatherApiService,
    private val weatherDao: WeatherDao,
    private val dailyForecastDao: DailyForecastDao,
    private val cityDao: CityDao,
    private val apiKey: String
) : WeatherRepository {

    private val cacheTtlMillis = 30 * 60 * 1000L

    override fun getCurrentWeather(cityId: Int): Flow<Result<Weather>> = flow {
        try {
            val cachedWeather = weatherDao.getWeatherByCityId(cityId).first()
            if (cachedWeather != null) {
                val isCacheStale = System.currentTimeMillis() - cachedWeather.timestamp > cacheTtlMillis
                if (isCacheStale) {
                    val refreshResult = refreshWeatherData(cityId)
                    if (refreshResult.isSuccess) {
                        val freshWeather = weatherDao.getWeatherByCityId(cityId).first()
                        emit(Result.success(freshWeather?.toDomain() ?: cachedWeather.toDomain()))
                    } else {
                        emit(Result.success(cachedWeather.toDomain()))
                    }
                } else {
                    emit(Result.success(cachedWeather.toDomain()))
                }
            } else {
                val refreshResult = refreshWeatherData(cityId)
                if (refreshResult.isSuccess) {
                    val freshWeather = weatherDao.getWeatherByCityId(cityId).first()
                    if (freshWeather != null) {
                        emit(Result.success(freshWeather.toDomain()))
                    } else {
                        emit(Result.failure(Exception("Failed to load weather")))
                    }
                } else {
                    emit(Result.failure(refreshResult.exceptionOrNull() ?: Exception("Failed to load weather")))
                }
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun getCurrentWeatherByLocation(lat: Double, lon: Double): Flow<Result<Weather>> = flow {
        try {
            val cachedWeather = weatherDao.getCurrentLocationWeather().first()
            if (cachedWeather != null) {
                val isCacheStale = System.currentTimeMillis() - cachedWeather.timestamp > cacheTtlMillis
                if (isCacheStale) {
                    val refreshResult = refreshWeatherByLocation(lat, lon)
                    if (refreshResult.isSuccess) {
                        val freshWeather = weatherDao.getCurrentLocationWeather().first()
                        emit(Result.success(freshWeather?.toDomain() ?: cachedWeather.toDomain()))
                    } else {
                        emit(Result.success(cachedWeather.toDomain()))
                    }
                } else {
                    emit(Result.success(cachedWeather.toDomain()))
                }
            } else {
                val refreshResult = refreshWeatherByLocation(lat, lon)
                if (refreshResult.isSuccess) {
                    val freshWeather = weatherDao.getCurrentLocationWeather().first()
                    if (freshWeather != null) {
                        emit(Result.success(freshWeather.toDomain()))
                    } else {
                        emit(Result.failure(Exception("Failed to load weather")))
                    }
                } else {
                    emit(Result.failure(refreshResult.exceptionOrNull() ?: Exception("Failed to load weather")))
                }
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun getWeeklyForecast(cityId: Int): Flow<Result<List<DailyForecast>>> = flow {
        try {
            val cachedForecasts = dailyForecastDao.getForecastByCityId(cityId).first()
            if (cachedForecasts.isNotEmpty()) {
                val isCacheStale = System.currentTimeMillis() - cachedForecasts.first().timestamp > cacheTtlMillis
                if (isCacheStale) {
                    val refreshResult = refreshForecastData(cityId)
                    if (refreshResult.isSuccess) {
                        val freshForecasts = dailyForecastDao.getForecastByCityId(cityId).first()
                        emit(Result.success(freshForecasts.map { it.toDomain() }))
                    } else {
                        emit(Result.success(cachedForecasts.map { it.toDomain() }))
                    }
                } else {
                    emit(Result.success(cachedForecasts.map { it.toDomain() }))
                }
            } else {
                val refreshResult = refreshForecastData(cityId)
                if (refreshResult.isSuccess) {
                    val freshForecasts = dailyForecastDao.getForecastByCityId(cityId).first()
                    emit(Result.success(freshForecasts.map { it.toDomain() }))
                } else {
                    emit(Result.failure(refreshResult.exceptionOrNull() ?: Exception("Failed to load forecast")))
                }
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun refreshWeatherData(cityId: Int): Result<Unit> {
        return try {
            val city = cityDao.getCityById(cityId)
            val weatherDto = if (city != null) {
                weatherApiService.getCurrentWeatherByCoordinates(city.latitude, city.longitude, apiKey)
            } else {
                weatherApiService.getCurrentWeather(cityId, apiKey)
            }
            val weatherEntity = weatherDto.toEntity(
                cityId = cityId,
                cityName = city?.name,
                isCurrentLocation = false
            )
            weatherDao.insertWeather(weatherEntity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun refreshWeatherByLocation(lat: Double, lon: Double): Result<Unit> {
        return try {
            weatherDao.deleteCurrentLocationWeather()
            val weatherDto = weatherApiService.getCurrentWeatherByCoordinates(lat, lon, apiKey)
            val weatherEntity = weatherDto.toEntity(isCurrentLocation = true)
            weatherDao.insertWeather(weatherEntity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun refreshForecastData(cityId: Int): Result<Unit> {
        return try {
            val city = cityDao.getCityById(cityId)
            val forecastDto = if (city != null) {
                weatherApiService.getForecastByCoordinates(city.latitude, city.longitude, apiKey)
            } else {
                weatherApiService.getForecast(cityId, apiKey)
            }
            val forecastEntities = forecastDto.toEntities(
                cityId = cityId,
                cityName = city?.name
            )
            dailyForecastDao.deleteForecastsByCityId(cityId)
            dailyForecastDao.insertForecasts(forecastEntities)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
