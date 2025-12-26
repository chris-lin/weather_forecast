package com.example.weatherforecast.data.repository

import com.example.weatherforecast.data.local.dao.CityDao
import com.example.weatherforecast.data.mapper.CityMapper.toDomain
import com.example.weatherforecast.data.mapper.CityMapper.toEntity
import com.example.weatherforecast.data.remote.api.WeatherApiService
import com.example.weatherforecast.domain.model.City
import com.example.weatherforecast.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val cityDao: CityDao,
    private val weatherApiService: WeatherApiService,
    private val apiKey: String
) : CityRepository {

    override fun getAllCities(): Flow<List<City>> {
        return cityDao.getAllCities().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun searchCities(query: String): Flow<List<City>> {
        return kotlinx.coroutines.flow.flow {
            try {
                val geocodingResults = weatherApiService.searchCities(query, apiKey = apiKey)
                val cities = geocodingResults.map { dto ->
                    City(
                        id = "${dto.name}_${dto.lat}_${dto.lon}".hashCode(),
                        name = dto.name,
                        countryCode = dto.country,
                        latitude = dto.lat,
                        longitude = dto.lon,
                        isPredefined = false
                    )
                }
                emit(cities)
            } catch (e: Exception) {
                emit(emptyList())
            }
        }
    }

    override suspend fun addCity(city: City): Result<Unit> {
        return try {
            cityDao.insertCity(city.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeCity(city: City): Result<Unit> {
        return try {
            cityDao.deleteCity(city.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCityByCoordinates(lat: Double, lon: Double): Result<City> {
        return try {
            val geocodingResults = weatherApiService.getCityByCoordinates(lat, lon, apiKey = apiKey)
            if (geocodingResults.isNotEmpty()) {
                val geocodingDto = geocodingResults.first()
                val city = City(
                    id = "${geocodingDto.name}_${geocodingDto.lat}_${geocodingDto.lon}".hashCode(),
                    name = geocodingDto.name,
                    countryCode = geocodingDto.country,
                    latitude = geocodingDto.lat,
                    longitude = geocodingDto.lon,
                    isPredefined = false
                )
                Result.success(city)
            } else {
                Result.failure(Exception("No city found at coordinates"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
