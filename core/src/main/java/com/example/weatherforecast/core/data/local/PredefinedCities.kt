package com.example.weatherforecast.core.data.local

import com.example.weatherforecast.core.data.local.entity.CityEntity

object PredefinedCities {
    val cities = listOf(
        CityEntity(2643743, "London", "GB", 51.5074, -0.1278, isPredefined = true),
        CityEntity(5128581, "New York", "US", 40.7143, -74.006, isPredefined = true),
        CityEntity(1850147, "Tokyo", "JP", 35.6895, 139.6917, isPredefined = true),
        CityEntity(2988507, "Paris", "FR", 48.8534, 2.3488, isPredefined = true),
        CityEntity(1816670, "Beijing", "CN", 39.9075, 116.3972, isPredefined = true),
        CityEntity(2147714, "Sydney", "AU", -33.8679, 151.2073, isPredefined = true),
        CityEntity(524901, "Moscow", "RU", 55.7558, 37.6173, isPredefined = true),
        CityEntity(3451190, "Rio de Janeiro", "BR", -22.9068, -43.1729, isPredefined = true),
        CityEntity(1668341, "Taipei", "TW", 25.0330, 121.5654, isPredefined = true)
    )
}
