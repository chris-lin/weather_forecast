package com.example.weatherforecast.domain.usecase

import com.example.weatherforecast.domain.repository.WeatherRepository
import javax.inject.Inject

class RefreshWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(cityId: Int): Result<Unit> {
        return weatherRepository.refreshWeatherData(cityId)
    }
}
