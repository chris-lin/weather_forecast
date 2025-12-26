package com.example.weatherforecast.domain.usecase

import com.example.weatherforecast.domain.model.Weather
import com.example.weatherforecast.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    operator fun invoke(cityId: Int): Flow<Result<Weather>> {
        return weatherRepository.getCurrentWeather(cityId)
    }
}
