package com.example.weatherforecast.domain.repository

import com.example.weatherforecast.domain.model.Location

interface LocationRepository {
    suspend fun getCurrentLocation(): Result<Location>
    fun isLocationPermissionGranted(): Boolean
}
