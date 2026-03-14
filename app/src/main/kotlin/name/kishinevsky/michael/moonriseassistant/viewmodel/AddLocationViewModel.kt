package name.kishinevsky.michael.moonriseassistant.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import name.kishinevsky.michael.moonriseassistant.repository.LocationRepository

sealed interface AddLocationUiState {
    data object Idle : AddLocationUiState
    data object Saving : AddLocationUiState
    data class Error(val message: String) : AddLocationUiState
    data object Success : AddLocationUiState
}

class AddLocationViewModel(
    private val locationRepository: LocationRepository,
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

    fun resetState() {
        _uiState.value = AddLocationUiState.Idle
    }

    class Factory(
        private val locationRepository: LocationRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddLocationViewModel(locationRepository) as T
        }
    }
}
