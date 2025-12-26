package com.example.weatherforecast.feature.presentation.ui.citylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.domain.model.City
import com.example.weatherforecast.domain.usecase.GetCitiesUseCase
import com.example.weatherforecast.domain.usecase.RemoveCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityListViewModel @Inject constructor(
    private val getCitiesUseCase: GetCitiesUseCase,
    private val removeCityUseCase: RemoveCityUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CityListUiState>(CityListUiState.Loading)
    val uiState: StateFlow<CityListUiState> = _uiState.asStateFlow()

    init {
        loadCities()
    }

    private fun loadCities() {
        viewModelScope.launch {
            _uiState.value = CityListUiState.Loading
            try {
                getCitiesUseCase().collect { cities ->
                    _uiState.value = CityListUiState.Success(cities)
                }
            } catch (e: Exception) {
                _uiState.value = CityListUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun refresh() {
        loadCities()
    }

    fun removeCity(city: City) {
        viewModelScope.launch {
            removeCityUseCase(city)
        }
    }
}
