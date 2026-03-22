package name.kishinevsky.michael.moonriseassistant.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import name.kishinevsky.michael.moonriseassistant.model.AppSettings
import name.kishinevsky.michael.moonriseassistant.repository.SettingsRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `emits current settings on load`() = runTest(testDispatcher) {
        // Given
        val settings = AppSettings(daysBeforeFullMoon = 3, daysAfterFullMoon = 7)
        val repo = FakeSettingsRepository(settings)

        // When
        val vm = SettingsViewModel(repo)
        advanceUntilIdle()

        // Then
        val state = vm.uiState.value
        assertThat(state).isInstanceOf(SettingsUiState.Content::class.java)
        val content = state as SettingsUiState.Content
        assertThat(content.settings.daysBeforeFullMoon).isEqualTo(3)
        assertThat(content.settings.daysAfterFullMoon).isEqualTo(7)
    }

    @Test
    fun `persists changes when updating days before`() = runTest(testDispatcher) {
        // Given
        val repo = FakeSettingsRepository(AppSettings())
        val vm = SettingsViewModel(repo)
        advanceUntilIdle()

        // When
        vm.updateDaysBefore(4)
        advanceUntilIdle()

        // Then
        assertThat(repo.lastSaved?.daysBeforeFullMoon).isEqualTo(4)
        val content = vm.uiState.value as SettingsUiState.Content
        assertThat(content.settings.daysBeforeFullMoon).isEqualTo(4)
    }

    @Test
    fun `persists changes when updating use metric`() = runTest(testDispatcher) {
        // Given
        val repo = FakeSettingsRepository(AppSettings(useMetric = false))
        val vm = SettingsViewModel(repo)
        advanceUntilIdle()

        // When
        vm.updateUseMetric(true)
        advanceUntilIdle()

        // Then
        assertThat(repo.lastSaved?.useMetric).isTrue()
    }

    // ── Fakes ────────────────────────────────────────────────

    private class FakeSettingsRepository(
        initialSettings: AppSettings = AppSettings(),
    ) : SettingsRepository(StubSettingsDao()) {
        private val settingsFlow = MutableStateFlow(initialSettings)
        var lastSaved: AppSettings? = null
            private set

        override fun getSettings(): Flow<AppSettings> = settingsFlow

        override suspend fun updateSettings(settings: AppSettings) {
            lastSaved = settings
            settingsFlow.value = settings
        }
    }
}
