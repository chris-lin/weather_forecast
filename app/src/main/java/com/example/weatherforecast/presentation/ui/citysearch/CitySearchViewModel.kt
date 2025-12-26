package com.example.weatherforecast.presentation.ui.citysearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.domain.model.City
import com.example.weatherforecast.domain.usecase.AddCityUseCase
import com.example.weatherforecast.domain.usecase.SearchCitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitySearchViewModel @Inject constructor(
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val addCityUseCase: AddCityUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CitySearchUiState>(CitySearchUiState.Idle)
    val uiState: StateFlow<CitySearchUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            _uiState.value = CitySearchUiState.Idle
            return
        }

        search(query)
    }

    private fun search(query: String) {
        viewModelScope.launch {
            _uiState.value = CitySearchUiState.Loading
            try {
                searchCitiesUseCase(query).collect { cities ->
                    _uiState.value = CitySearchUiState.Success(cities)
                }
            } catch (e: Exception) {
                _uiState.value = CitySearchUiState.Error(e.message ?: "Search failed")
            }
        }
    }

    fun addCity(city: City, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val result = addCityUseCase(city)
            if (result.isSuccess) {
                onSuccess()
            }
        }
    }
}
