package com.example.weatherforecast.domain.usecase

import com.example.weatherforecast.domain.model.Weather
import com.example.weatherforecast.domain.repository.LocationRepository
import com.example.weatherforecast.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrentLocationWeatherUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(): Flow<Result<Weather>> = flow {
        val locationResult = locationRepository.getCurrentLocation()
        if (locationResult.isSuccess) {
            val location = locationResult.getOrThrow()
            emitAll(weatherRepository.getCurrentWeatherByLocation(location.latitude, location.longitude))
        } else {
            emit(Result.failure(locationResult.exceptionOrNull() ?: Exception("Unknown error")))
        }
    }
}
