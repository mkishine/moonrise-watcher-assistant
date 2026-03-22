package name.kishinevsky.michael.moonriseassistant.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import name.kishinevsky.michael.moonriseassistant.model.SavedLocation
import name.kishinevsky.michael.moonriseassistant.repository.LocationRepository

sealed interface LocationSelectorUiState {
    data object Loading : LocationSelectorUiState
    data class Content(
        val locations: List<SavedLocation>,
        val activeLocationId: String,
    ) : LocationSelectorUiState
}

class LocationSelectorViewModel(
    private val locationRepository: LocationRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<LocationSelectorUiState>(LocationSelectorUiState.Loading)
    val uiState: StateFlow<LocationSelectorUiState> = _uiState

    init {
        viewModelScope.launch {
            combine(
                locationRepository.getAllLocations(),
                locationRepository.getActiveLocation(),
            ) { locations, active ->
                if (active == null) {
                    LocationSelectorUiState.Loading
                } else {
                    LocationSelectorUiState.Content(
                        locations = locations,
                        activeLocationId = active.id,
                    )
                }
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun selectLocation(location: SavedLocation) {
        viewModelScope.launch {
            locationRepository.setActive(location.id)
        }
    }

    fun deleteLocation(location: SavedLocation) {
        viewModelScope.launch {
            locationRepository.deleteLocation(location.id)
        }
    }

    class Factory(
        private val locationRepository: LocationRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LocationSelectorViewModel(locationRepository) as T
        }
    }
}
