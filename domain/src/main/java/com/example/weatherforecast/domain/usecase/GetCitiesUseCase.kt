package com.example.weatherforecast.domain.usecase

import com.example.weatherforecast.domain.model.City
import com.example.weatherforecast.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCitiesUseCase @Inject constructor(
    private val cityRepository: CityRepository
) {
    operator fun invoke(): Flow<List<City>> {
        return cityRepository.getAllCities()
    }
}
