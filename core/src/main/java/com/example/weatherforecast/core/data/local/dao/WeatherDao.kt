package com.example.weatherforecast.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.core.data.local.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather WHERE cityId = :cityId")
    fun getWeatherByCityId(cityId: Int): Flow<WeatherEntity?>

    @Query("SELECT * FROM weather WHERE isCurrentLocation = 1 LIMIT 1")
    fun getCurrentLocationWeather(): Flow<WeatherEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("DELETE FROM weather WHERE cityId = :cityId")
    suspend fun deleteWeatherByCityId(cityId: Int)

    @Query("DELETE FROM weather WHERE isCurrentLocation = 1")
    suspend fun deleteCurrentLocationWeather()
}
