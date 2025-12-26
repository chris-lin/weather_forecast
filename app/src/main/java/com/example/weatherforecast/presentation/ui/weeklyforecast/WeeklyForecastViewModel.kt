package com.example.weatherforecast.presentation.ui.weeklyforecast

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.domain.usecase.GetCurrentWeatherUseCase
import com.example.weatherforecast.domain.usecase.GetWeeklyForecastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeeklyForecastViewModel @Inject constructor(
    private val getWeeklyForecastUseCase: GetWeeklyForecastUseCase,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeeklyForecastUiState>(WeeklyForecastUiState.Loading)
    val uiState: StateFlow<WeeklyForecastUiState> = _uiState.asStateFlow()

    private val cityId: Int = savedStateHandle.get<String>("cityId")?.toIntOrNull() ?: 0

    init {
        loadForecast()
    }

    private fun loadForecast() {
        viewModelScope.launch {
            _uiState.value = WeeklyForecastUiState.Loading

            combine(
                getWeeklyForecastUseCase(cityId),
                getCurrentWeatherUseCase(cityId)
            ) { forecastResult, weatherResult ->
                when {
                    forecastResult.isSuccess && weatherResult.isSuccess -> {
                        WeeklyForecastUiState.Success(
                            forecasts = forecastResult.getOrThrow(),
                            cityName = weatherResult.getOrThrow().cityName
                        )
                    }
                    forecastResult.isFailure -> {
                        WeeklyForecastUiState.Error(
                            forecastResult.exceptionOrNull()?.message ?: "Failed to load forecast"
                        )
                    }
                    else -> {
                        WeeklyForecastUiState.Error("Unknown error")
                    }
                }
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun refresh() {
        loadForecast()
    }
}
