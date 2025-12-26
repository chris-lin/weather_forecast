package com.example.weatherforecast.feature.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.domain.usecase.GetCurrentLocationWeatherUseCase
import com.example.weatherforecast.domain.usecase.GetCurrentWeatherUseCase
import com.example.weatherforecast.domain.usecase.GetWeeklyForecastUseCase
import com.example.weatherforecast.domain.usecase.RefreshWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getWeeklyForecastUseCase: GetWeeklyForecastUseCase,
    private val getCurrentLocationWeatherUseCase: GetCurrentLocationWeatherUseCase,
    private val refreshWeatherUseCase: RefreshWeatherUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var currentCityId: Int? = null

    init {
        loadWeatherForCurrentLocation()
    }

    fun loadWeatherForCity(cityId: Int) {
        currentCityId = cityId
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            combine(
                getCurrentWeatherUseCase(cityId),
                getWeeklyForecastUseCase(cityId)
            ) { weatherResult, forecastResult ->
                when {
                    weatherResult.isSuccess && forecastResult.isSuccess -> {
                        HomeUiState.Success(
                            weather = weatherResult.getOrThrow(),
                            forecast = forecastResult.getOrThrow()
                        )
                    }
                    weatherResult.isFailure -> {
                        HomeUiState.Error(
                            weatherResult.exceptionOrNull()?.message ?: "Unknown error"
                        )
                    }
                    else -> {
                        HomeUiState.Error(
                            forecastResult.exceptionOrNull()?.message ?: "Unknown error"
                        )
                    }
                }
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun loadWeatherForCurrentLocation() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            getCurrentLocationWeatherUseCase().collect { weatherResult ->
                if (weatherResult.isSuccess) {
                    val weather = weatherResult.getOrThrow()
                    loadWeatherForCity(weather.cityId)
                } else {
                    _uiState.value = HomeUiState.Error(
                        weatherResult.exceptionOrNull()?.message ?: "Failed to get location"
                    )
                }
            }
        }
    }

    fun refresh() {
        currentCityId?.let { cityId ->
            viewModelScope.launch {
                refreshWeatherUseCase(cityId)
            }
        } ?: loadWeatherForCurrentLocation()
    }
}
