package com.example.weatherforecast.domain.repository

import com.example.weatherforecast.domain.model.City
import kotlinx.coroutines.flow.Flow

interface CityRepository {
    fun getAllCities(): Flow<List<City>>
    fun searchCities(query: String): Flow<List<City>>
    suspend fun addCity(city: City): Result<Unit>
    suspend fun removeCity(city: City): Result<Unit>
    suspend fun getCityByCoordinates(lat: Double, lon: Double): Result<City>
}
