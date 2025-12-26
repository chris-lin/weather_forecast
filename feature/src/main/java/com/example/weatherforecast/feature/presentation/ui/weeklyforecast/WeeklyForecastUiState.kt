package com.example.weatherforecast.feature.presentation.ui.weeklyforecast

import com.example.weatherforecast.domain.model.DailyForecast

sealed interface WeeklyForecastUiState {
    object Loading : WeeklyForecastUiState
    data class Success(val forecasts: List<DailyForecast>, val cityName: String) : WeeklyForecastUiState
    data class Error(val message: String) : WeeklyForecastUiState
}
