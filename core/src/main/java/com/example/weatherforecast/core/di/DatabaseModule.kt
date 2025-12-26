package com.example.weatherforecast.core.di

import android.content.Context
import androidx.room.Room
import com.example.weatherforecast.core.data.local.dao.CityDao
import com.example.weatherforecast.core.data.local.dao.DailyForecastDao
import com.example.weatherforecast.core.data.local.dao.WeatherDao
import com.example.weatherforecast.core.data.local.database.WeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideWeatherDatabase(
        @ApplicationContext context: Context
    ): WeatherDatabase {
        return Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            "weather_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWeatherDao(database: WeatherDatabase): WeatherDao {
        return database.weatherDao()
    }

    @Provides
    @Singleton
    fun provideDailyForecastDao(database: WeatherDatabase): DailyForecastDao {
        return database.dailyForecastDao()
    }

    @Provides
    @Singleton
    fun provideCityDao(database: WeatherDatabase): CityDao {
        return database.cityDao()
    }
}
