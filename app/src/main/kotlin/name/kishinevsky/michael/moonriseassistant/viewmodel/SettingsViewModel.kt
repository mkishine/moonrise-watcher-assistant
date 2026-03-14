package name.kishinevsky.michael.moonriseassistant.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import name.kishinevsky.michael.moonriseassistant.model.AppSettings
import name.kishinevsky.michael.moonriseassistant.repository.SettingsRepository
import java.time.LocalTime

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Content(val settings: AppSettings) : SettingsUiState
}

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SettingsUiState>(SettingsUiState.Loading)
    val uiState: StateFlow<SettingsUiState> = _uiState

    init {
        viewModelScope.launch {
            settingsRepository.getSettings().collect { settings ->
                _uiState.value = SettingsUiState.Content(settings)
            }
        }
    }

    fun updateDaysBefore(value: Int) {
        updateSettings { it.copy(daysBeforeFullMoon = value) }
    }

    fun updateDaysAfter(value: Int) {
        updateSettings { it.copy(daysAfterFullMoon = value) }
    }

    fun updateForecastPeriod(value: Int) {
        updateSettings { it.copy(forecastPeriodMonths = value) }
    }

    fun updateMaxMoonriseTime(hour: Int, minute: Int) {
        updateSettings { it.copy(maxMoonriseTime = LocalTime.of(hour, minute)) }
    }

    fun updateTolerance(value: Int) {
        updateSettings { it.copy(beforeSunsetToleranceMin = value) }
    }

    fun updateUseMetric(value: Boolean) {
        updateSettings { it.copy(useMetric = value) }
    }

    private fun updateSettings(transform: (AppSettings) -> AppSettings) {
        val current = (_uiState.value as? SettingsUiState.Content)?.settings ?: return
        val updated = transform(current)
        _uiState.value = SettingsUiState.Content(updated)
        viewModelScope.launch {
            settingsRepository.updateSettings(updated)
        }
    }

    class Factory(
        private val settingsRepository: SettingsRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(settingsRepository) as T
        }
    }
}
