package com.example.weatherforecast.presentation.ui.citysearch

import com.example.weatherforecast.domain.model.City

sealed interface CitySearchUiState {
    object Idle : CitySearchUiState
    object Loading : CitySearchUiState
    data class Success(val cities: List<City>) : CitySearchUiState
    data class Error(val message: String) : CitySearchUiState
}
