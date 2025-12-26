package com.example.weatherforecast.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.data.local.entity.DailyForecastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyForecastDao {
    @Query("SELECT * FROM daily_forecast WHERE cityId = :cityId ORDER BY date ASC")
    fun getForecastByCityId(cityId: Int): Flow<List<DailyForecastEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecasts(forecasts: List<DailyForecastEntity>)

    @Query("DELETE FROM daily_forecast WHERE cityId = :cityId")
    suspend fun deleteForecastsByCityId(cityId: Int)

    @Query("DELETE FROM daily_forecast WHERE timestamp < :threshold")
    suspend fun deleteOldForecasts(threshold: Long)
}
