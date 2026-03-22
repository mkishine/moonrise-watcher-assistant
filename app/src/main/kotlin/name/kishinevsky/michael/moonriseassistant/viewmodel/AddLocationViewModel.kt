package name.kishinevsky.michael.moonriseassistant.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import name.kishinevsky.michael.moonriseassistant.location.GeocodingResult
import name.kishinevsky.michael.moonriseassistant.location.Geocoding
import name.kishinevsky.michael.moonriseassistant.repository.LocationRepository

sealed interface AddLocationUiState {
    data object Idle : AddLocationUiState
    data object Saving : AddLocationUiState
    data class Error(val message: String) : AddLocationUiState
    data object Success : AddLocationUiState
}

class AddLocationViewModel(
    private val locationRepository: LocationRepository,
    private val geocodingService: Geocoding,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddLocationUiState>(AddLocationUiState.Idle)
    val uiState: StateFlow<AddLocationUiState> = _uiState

    fun saveLocation(name: String, cityState: String?, latitude: Double, longitude: Double) {
        if (name.isBlank()) {
            _uiState.value = AddLocationUiState.Error("Location name cannot be empty")
            return
        }
        if (latitude.isNaN() || latitude < -90 || latitude > 90) {
            _uiState.value = AddLocationUiState.Error("Latitude must be between -90 and 90")
            return
        }
        if (longitude.isNaN() || longitude < -180 || longitude > 180) {
            _uiState.value = AddLocationUiState.Error("Longitude must be between -180 and 180")
            return
        }

        _uiState.value = AddLocationUiState.Saving
        viewModelScope.launch {
            try {
                locationRepository.addLocation(
                    name = name,
                    cityState = cityState,
                    lat = latitude,
                    lng = longitude,
                )
                _uiState.value = AddLocationUiState.Success
            } catch (e: Exception) {
                _uiState.value = AddLocationUiState.Error(
                    e.message ?: "Failed to save location",
                )
            }
        }
    }

    fun resolveAndSaveLocation(cityQuery: String, customName: String) {
        if (cityQuery.isBlank()) {
            _uiState.value = AddLocationUiState.Error("City name cannot be empty")
            return
        }

        _uiState.value = AddLocationUiState.Saving
        viewModelScope.launch {
            when (val result = geocodingService.geocode(cityQuery)) {
                is GeocodingResult.Success -> {
                    val name = customName.ifBlank { result.displayName }
                    saveLocation(
                        name = name,
                        cityState = result.displayName,
                        latitude = result.lat,
                        longitude = result.lng,
                    )
                }
                is GeocodingResult.NotFound -> {
                    _uiState.value = AddLocationUiState.Error(
                        "Could not find location: $cityQuery",
                    )
                }
                is GeocodingResult.Error -> {
                    _uiState.value = AddLocationUiState.Error(result.message)
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = AddLocationUiState.Idle
    }

    class Factory(
        private val locationRepository: LocationRepository,
        private val geocodingService: Geocoding,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddLocationViewModel(locationRepository, geocodingService) as T
        }
    }
}
