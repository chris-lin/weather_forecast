package com.example.weatherforecast.presentation.ui.citylist

import com.example.weatherforecast.domain.model.City

sealed interface CityListUiState {
    object Loading : CityListUiState
    data class Success(val cities: List<City>) : CityListUiState
    data class Error(val message: String) : CityListUiState
}
