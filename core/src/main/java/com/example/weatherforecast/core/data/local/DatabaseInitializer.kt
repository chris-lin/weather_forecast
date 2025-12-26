package com.example.weatherforecast.core.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.weatherforecast.core.data.local.dao.CityDao
import com.example.weatherforecast.core.data.local.dao.DailyForecastDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

@Singleton
class DatabaseInitializer @Inject constructor(
    private val context: Context,
    private val cityDao: CityDao,
    private val dailyForecastDao: DailyForecastDao
) {
    private val isInitializedKey = booleanPreferencesKey("database_initialized")

    suspend fun initialize() {
        val isInitialized = context.dataStore.data.map { preferences ->
            preferences[isInitializedKey] ?: false
        }.first()

        if (!isInitialized) {
            initializeDatabase()
            markAsInitialized()
        }

        cleanupOldData()
    }

    private suspend fun initializeDatabase() {
        cityDao.insertCities(PredefinedCities.cities)
    }

    private suspend fun markAsInitialized() {
        context.dataStore.edit { preferences ->
            preferences[isInitializedKey] = true
        }
    }

    private suspend fun cleanupOldData() {
        val sevenDaysAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)
        dailyForecastDao.deleteOldForecasts(sevenDaysAgo)
    }
}
