package com.example.weatherforecast.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val countryCode: String,
    val latitude: Double,
    val longitude: Double,
    val isPredefined: Boolean = true,
    val isUserAdded: Boolean = false,
    val addedTimestamp: Long = System.currentTimeMillis()
)
