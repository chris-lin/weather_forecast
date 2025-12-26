package com.example.weatherforecast.domain.usecase

import com.example.weatherforecast.domain.model.DailyForecast
import com.example.weatherforecast.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeeklyForecastUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    operator fun invoke(cityId: Int): Flow<Result<List<DailyForecast>>> {
        return weatherRepository.getWeeklyForecast(cityId)
    }
}
