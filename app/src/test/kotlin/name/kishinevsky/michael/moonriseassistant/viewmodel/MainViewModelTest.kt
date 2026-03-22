package name.kishinevsky.michael.moonriseassistant.viewmodel

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import name.kishinevsky.michael.moonriseassistant.domain.AstroCalculator
import name.kishinevsky.michael.moonriseassistant.domain.VerdictEngine
import name.kishinevsky.michael.moonriseassistant.model.AppSettings
import name.kishinevsky.michael.moonriseassistant.model.CheckResult
import name.kishinevsky.michael.moonriseassistant.model.ForecastDay
import name.kishinevsky.michael.moonriseassistant.model.SavedLocation
import name.kishinevsky.michael.moonriseassistant.model.Verdict
import name.kishinevsky.michael.moonriseassistant.model.VerdictChecks
import name.kishinevsky.michael.moonriseassistant.model.WeatherCondition
import name.kishinevsky.michael.moonriseassistant.network.VisualCrossingApi
import name.kishinevsky.michael.moonriseassistant.repository.ForecastRepository
import name.kishinevsky.michael.moonriseassistant.repository.LocationRepository
import name.kishinevsky.michael.moonriseassistant.repository.SettingsRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

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
    fun `emits FirstTime when no location exists`() = runTest(testDispatcher) {
        // Given
        val locationRepo = FakeLocationRepository(locationCount = 0)
        val forecastRepo = FakeForecastRepository()
        val settingsRepo = FakeSettingsRepository()

        // When
        val vm = MainViewModel(locationRepo, forecastRepo, settingsRepo)
        advanceUntilIdle()

        // Then
        assertThat(vm.uiState.value).isEqualTo(MainUiState.FirstTime)
    }

    @Test
    fun `emits Loading then Content on successful forecast load`() = runTest(testDispatcher) {
        // Given
        val location = SavedLocation("1", "Seattle", "WA", 47.6, -122.3)
        val todayDate = SAMPLE_FORECAST_DAY.date
        val forecastDays = listOf(SAMPLE_FORECAST_DAY, SAMPLE_FORECAST_DAY.copy(date = todayDate.plusDays(1)))
        val locationRepo = FakeLocationRepository(locationCount = 1, activeLocation = location)
        val forecastRepo = FakeForecastRepository(forecast = forecastDays)
        val settingsRepo = FakeSettingsRepository()

        // When
        val vm = MainViewModel(locationRepo, forecastRepo, settingsRepo, today = { todayDate })

        // Then — initial state is Loading
        assertThat(vm.uiState.value).isEqualTo(MainUiState.Loading)

        advanceUntilIdle()

        // Then — after loading, Content with today + forecast
        val state = vm.uiState.value
        assertThat(state).isInstanceOf(MainUiState.Content::class.java)
        val content = state as MainUiState.Content
        assertThat(content.locationName).isEqualTo("Seattle")
        assertThat(content.today).isNotNull
        assertThat(content.forecast).hasSize(1)
    }

    @Test
    fun `emits Error when repository fails`() = runTest(testDispatcher) {
        // Given
        val location = SavedLocation("1", "Seattle", "WA", 47.6, -122.3)
        val locationRepo = FakeLocationRepository(locationCount = 1, activeLocation = location)
        val forecastRepo = FakeForecastRepository(error = RuntimeException("Network error"))
        val settingsRepo = FakeSettingsRepository()

        // When
        val vm = MainViewModel(locationRepo, forecastRepo, settingsRepo)
        advanceUntilIdle()

        // Then
        val state = vm.uiState.value
        assertThat(state).isInstanceOf(MainUiState.Error::class.java)
        val error = state as MainUiState.Error
        assertThat(error.locationName).isEqualTo("Seattle")
        assertThat(error.message).isEqualTo("Network error")
    }

    @Test
    fun `refresh reloads forecast`() = runTest(testDispatcher) {
        // Given
        val location = SavedLocation("1", "Seattle", "WA", 47.6, -122.3)
        val todayDate = SAMPLE_FORECAST_DAY.date
        val forecastDays = listOf(SAMPLE_FORECAST_DAY)
        val locationRepo = FakeLocationRepository(locationCount = 1, activeLocation = location)
        val forecastRepo = FakeForecastRepository(forecast = forecastDays)
        val settingsRepo = FakeSettingsRepository()
        val vm = MainViewModel(locationRepo, forecastRepo, settingsRepo, today = { todayDate })
        advanceUntilIdle()

        // When
        vm.refresh()
        advanceUntilIdle()

        // Then — still Content after refresh
        val state = vm.uiState.value
        assertThat(state).isInstanceOf(MainUiState.Content::class.java)
        assertThat(forecastRepo.fetchCount).isEqualTo(2)
    }

    // ── Fakes ────────────────────────────────────────────────

    private class FakeLocationRepository(
        private val locationCount: Int = 0,
        private val activeLocation: SavedLocation? = null,
    ) : LocationRepository(StubLocationDao()) {
        override fun getActiveLocation(): Flow<SavedLocation?> = MutableStateFlow(activeLocation)
        override suspend fun getLocationCount(): Int = locationCount
    }

    private class FakeForecastRepository(
        private val forecast: List<ForecastDay> = emptyList(),
        private val error: Exception? = null,
    ) : ForecastRepository(
        api = VisualCrossingApi(HttpClient(CIO), "unused"),
        weatherCacheDao = StubWeatherCacheDao(),
        astroCalculator = AstroCalculator(),
        verdictEngine = VerdictEngine(),
    ) {
        var fetchCount = 0
            private set

        override suspend fun getForecast(
            location: SavedLocation,
            settings: AppSettings,
            zone: ZoneId,
            today: LocalDate,
        ): List<ForecastDay> {
            fetchCount++
            if (error != null) throw error
            return forecast
        }
    }

    private class FakeSettingsRepository : SettingsRepository(StubSettingsDao()) {
        override fun getSettings(): Flow<AppSettings> = MutableStateFlow(AppSettings())
    }

    companion object {
        private val SAMPLE_FORECAST_DAY = ForecastDay(
            date = LocalDate.of(2026, 3, 3),
            sunset = LocalTime.of(17, 50),
            moonrise = LocalTime.of(18, 30),
            azimuthDegrees = 90,
            azimuthCardinal = "E",
            weather = WeatherCondition.CLEAR,
            verdict = Verdict.GOOD,
            verdictChecks = VerdictChecks(
                phaseWindow = CheckResult.PASS,
                moonriseAfterSunset = CheckResult.PASS,
                moonriseBeforeBedtime = CheckResult.PASS,
                skyClear = CheckResult.PASS,
            ),
        )
    }
}
