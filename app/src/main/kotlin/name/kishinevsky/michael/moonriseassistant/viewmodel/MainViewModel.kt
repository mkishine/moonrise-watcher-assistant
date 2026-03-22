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
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

sealed interface MainUiState {
    data object Loading : MainUiState
    data class Content(
        val locationName: String,
        val today: ForecastDay?,
        val forecast: List<ForecastDay>,
        val maxMoonriseTime: LocalTime,
        val isRefreshing: Boolean = false,
        val lastUpdated: Instant = Instant.now(),
    ) : MainUiState
    data class Error(val locationName: String, val message: String) : MainUiState
    data object FirstTime : MainUiState
}

class MainViewModel(
    private val locationRepository: LocationRepository,
    private val forecastRepository: ForecastRepository,
    private val settingsRepository: SettingsRepository,
    private val today: () -> LocalDate = { LocalDate.now() },
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

        val currentState = _uiState.value
        if (currentState is MainUiState.Content) {
            _uiState.value = currentState.copy(isRefreshing = true)
        } else {
            _uiState.value = MainUiState.Loading
        }
        val settings = settingsRepository.getSettings().first()
        val todayDate = today()

        try {
            val forecast = forecastRepository.getForecast(
                location = location,
                settings = settings,
                today = todayDate,
            )
            val todayDay = forecast.firstOrNull { it.date == todayDate }
            val upcoming = forecast.filter { it.date.isAfter(todayDate) }
            _uiState.value = MainUiState.Content(
                locationName = location.name,
                today = todayDay,
                forecast = upcoming,
                maxMoonriseTime = settings.maxMoonriseTime,
                lastUpdated = Instant.now(),
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
