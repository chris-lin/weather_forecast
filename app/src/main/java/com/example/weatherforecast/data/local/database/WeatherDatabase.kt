package com.example.weatherforecast.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherforecast.data.local.dao.CityDao
import com.example.weatherforecast.data.local.dao.DailyForecastDao
import com.example.weatherforecast.data.local.dao.WeatherDao
import com.example.weatherforecast.data.local.entity.CityEntity
import com.example.weatherforecast.data.local.entity.DailyForecastEntity
import com.example.weatherforecast.data.local.entity.WeatherEntity

@Database(
    entities = [
        WeatherEntity::class,
        DailyForecastEntity::class,
        CityEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun dailyForecastDao(): DailyForecastDao
    abstract fun cityDao(): CityDao
}
