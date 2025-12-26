package com.example.weatherforecast.data.mapper

import com.example.weatherforecast.data.local.entity.CityEntity
import com.example.weatherforecast.data.remote.dto.GeocodingDto
import com.example.weatherforecast.domain.model.City

object CityMapper {
    fun CityEntity.toDomain(): City {
        return City(
            id = id,
            name = name,
            countryCode = countryCode,
            latitude = latitude,
            longitude = longitude,
            isPredefined = isPredefined
        )
    }

    fun City.toEntity(): CityEntity {
        return CityEntity(
            id = id,
            name = name,
            countryCode = countryCode,
            latitude = latitude,
            longitude = longitude,
            isPredefined = isPredefined,
            isUserAdded = !isPredefined
        )
    }

    fun GeocodingDto.toEntity(): CityEntity {
        return CityEntity(
            id = "${name}_${lat}_${lon}".hashCode(),
            name = name,
            countryCode = country,
            latitude = lat,
            longitude = lon,
            isPredefined = false,
            isUserAdded = true
        )
    }
}
