package name.kishinevsky.michael.moonriseassistant.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import name.kishinevsky.michael.moonriseassistant.model.ForecastDay
import name.kishinevsky.michael.moonriseassistant.repository.ForecastRepository
import name.kishinevsky.michael.moonriseassistant.repository.LocationRepository
import name.kishinevsky.michael.moonriseassistant.repository.SettingsRepository
import java.time.ZoneId

sealed interface MainUiState {
    data object Loading : MainUiState
    data class Content(
        val locationName: String,
        val today: ForecastDay?,
        val forecast: List<ForecastDay>,
    ) : MainUiState
    data class Error(val locationName: String, val message: String) : MainUiState
    data object FirstTime : MainUiState
}

class MainViewModel(
    private val locationRepository: LocationRepository,
    private val forecastRepository: ForecastRepository,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        viewModelScope.launch {
            val count = locationRepository.getLocationCount()
            if (count == 0) {
                _uiState.value = MainUiState.FirstTime
            } else {
                loadForecast()
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            loadForecast()
        }
    }

    private suspend fun loadForecast() {
        val location = locationRepository.getActiveLocation().first()
        if (location == null) {
            _uiState.value = MainUiState.FirstTime
            return
        }

        _uiState.value = MainUiState.Loading
        val settings = settingsRepository.getSettings().first()

        try {
            val forecast = forecastRepository.getForecast(
                location = location,
                settings = settings,
                zone = ZoneId.systemDefault(),
            )
            val today = forecast.firstOrNull()
            val upcoming = if (forecast.size > 1) forecast.drop(1) else emptyList()
            _uiState.value = MainUiState.Content(
                locationName = location.name,
                today = today,
                forecast = upcoming,
            )
        } catch (e: Exception) {
            _uiState.value = MainUiState.Error(
                locationName = location.name,
                message = e.message ?: "Unknown error",
            )
        }
    }

    class Factory(
        private val locationRepository: LocationRepository,
        private val forecastRepository: ForecastRepository,
        private val settingsRepository: SettingsRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(locationRepository, forecastRepository, settingsRepository) as T
        }
    }
}
