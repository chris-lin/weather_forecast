package com.example.weatherforecast.presentation.ui.home

import com.example.weatherforecast.domain.model.DailyForecast
import com.example.weatherforecast.domain.model.Weather

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(
        val weather: Weather,
        val forecast: List<DailyForecast>
    ) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
