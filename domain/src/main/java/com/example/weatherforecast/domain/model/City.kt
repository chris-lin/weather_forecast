package com.example.weatherforecast.domain.model

data class City(
    val id: Int,
    val name: String,
    val countryCode: String,
    val latitude: Double,
    val longitude: Double,
    val isPredefined: Boolean = true
)
