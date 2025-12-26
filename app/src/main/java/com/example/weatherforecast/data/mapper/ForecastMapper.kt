package com.example.weatherforecast.data.mapper

import com.example.weatherforecast.data.local.entity.DailyForecastEntity
import com.example.weatherforecast.data.remote.dto.ForecastDto
import com.example.weatherforecast.domain.model.DailyForecast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ForecastMapper {
    fun ForecastDto.toEntities(cityId: Int? = null, cityName: String? = null): List<DailyForecastEntity> {
        val dailyForecasts = mutableMapOf<String, MutableList<com.example.weatherforecast.data.remote.dto.ForecastItemDto>>()

        list.forEach { item ->
            val date = item.dt_txt.split(" ")[0]
            dailyForecasts.getOrPut(date) { mutableListOf() }.add(item)
        }

        return dailyForecasts.map { (date, items) ->
            val tempMin = items.minOf { it.main.temp_min }
            val tempMax = items.maxOf { it.main.temp_max }
            val avgHumidity = items.map { it.main.humidity }.average().toInt()
            val avgWindSpeed = items.map { it.wind.speed }.average()
            val avgPressure = items.map { it.main.pressure }.average().toInt()
            val maxPop = items.maxOf { it.pop }

            val weatherConditions = items.map { it.weather.firstOrNull()?.main ?: "" }
            val mostCommonWeather = weatherConditions.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key ?: ""
            val weatherDescription = items.first().weather.firstOrNull()?.description ?: ""
            val weatherIcon = items.first().weather.firstOrNull()?.icon ?: ""

            DailyForecastEntity(
                cityId = cityId ?: city.id,
                cityName = cityName ?: city.name,
                date = date,
                tempMin = tempMin,
                tempMax = tempMax,
                weatherMain = mostCommonWeather,
                weatherDescription = weatherDescription,
                weatherIcon = weatherIcon,
                humidity = avgHumidity,
                windSpeed = avgWindSpeed,
                pressure = avgPressure,
                pop = maxPop,
                timestamp = System.currentTimeMillis()
            )
        }.take(7)
    }

    fun DailyForecastEntity.toDomain(): DailyForecast {
        return DailyForecast(
            date = date,
            tempMin = tempMin,
            tempMax = tempMax,
            weatherCondition = weatherMain,
            weatherDescription = weatherDescription,
            weatherIcon = weatherIcon,
            humidity = humidity,
            windSpeed = windSpeed,
            pressure = pressure,
            precipitationProbability = pop
        )
    }
}
