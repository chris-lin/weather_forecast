package com.example.weatherforecast.data.mapper

import com.example.weatherforecast.core.data.local.entity.WeatherEntity
import com.example.weatherforecast.core.data.remote.dto.CurrentWeatherDto
import com.example.weatherforecast.domain.model.Weather

object WeatherMapper {
    fun CurrentWeatherDto.toEntity(cityId: Int? = null, cityName: String? = null, isCurrentLocation: Boolean = false): WeatherEntity {
        return WeatherEntity(
            cityId = cityId ?: id,
            cityName = cityName ?: name,
            countryCode = sys.country,
            latitude = coord.lat,
            longitude = coord.lon,
            temperature = main.temp,
            feelsLike = main.feels_like,
            tempMin = main.temp_min,
            tempMax = main.temp_max,
            pressure = main.pressure,
            humidity = main.humidity,
            weatherMain = weather.firstOrNull()?.main ?: "",
            weatherDescription = weather.firstOrNull()?.description ?: "",
            weatherIcon = weather.firstOrNull()?.icon ?: "",
            windSpeed = wind.speed,
            windDegree = wind.deg,
            cloudiness = clouds.all,
            visibility = visibility,
            sunrise = sys.sunrise,
            sunset = sys.sunset,
            timestamp = System.currentTimeMillis(),
            isCurrentLocation = isCurrentLocation
        )
    }

    fun WeatherEntity.toDomain(): Weather {
        return Weather(
            cityId = cityId,
            cityName = cityName,
            countryCode = countryCode,
            temperature = temperature,
            feelsLike = feelsLike,
            tempMin = tempMin,
            tempMax = tempMax,
            pressure = pressure,
            humidity = humidity,
            weatherCondition = weatherMain,
            weatherDescription = weatherDescription,
            weatherIcon = weatherIcon,
            windSpeed = windSpeed,
            windDegree = windDegree,
            cloudiness = cloudiness,
            visibility = visibility,
            sunrise = sunrise,
            sunset = sunset,
            timestamp = timestamp
        )
    }
}
