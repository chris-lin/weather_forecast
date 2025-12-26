package com.example.weatherforecast.domain.usecase

import com.example.weatherforecast.domain.model.City
import com.example.weatherforecast.domain.repository.CityRepository
import javax.inject.Inject

class AddCityUseCase @Inject constructor(
    private val cityRepository: CityRepository
) {
    suspend operator fun invoke(city: City): Result<Unit> {
        return cityRepository.addCity(city)
    }
}
