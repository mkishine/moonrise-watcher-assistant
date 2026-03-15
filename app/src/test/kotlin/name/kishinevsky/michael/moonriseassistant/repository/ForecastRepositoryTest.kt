package name.kishinevsky.michael.moonriseassistant.repository

import kotlinx.serialization.json.Json
import name.kishinevsky.michael.moonriseassistant.domain.AstroCalculator
import name.kishinevsky.michael.moonriseassistant.domain.VerdictEngine
import name.kishinevsky.michael.moonriseassistant.model.AppSettings
import name.kishinevsky.michael.moonriseassistant.model.SavedLocation
import name.kishinevsky.michael.moonriseassistant.model.Verdict
import name.kishinevsky.michael.moonriseassistant.model.WeatherCondition
import name.kishinevsky.michael.moonriseassistant.network.VisualCrossingApi
import name.kishinevsky.michael.moonriseassistant.network.model.DayResponse
import name.kishinevsky.michael.moonriseassistant.network.model.TimelineResponse
import name.kishinevsky.michael.moonriseassistant.storage.dao.WeatherCacheDao
import name.kishinevsky.michael.moonriseassistant.storage.entity.WeatherCacheEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.LocalDate
import java.time.ZoneId

/**
 * Tests ForecastRepository.buildForecast() — the pure function that assembles
 * ForecastDay objects from weather data + astro calculations + verdict evaluation.
 * Uses the recorded API fixture for weather data.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ForecastRepositoryTest {

    private val json = Json { ignoreUnknownKeys = true }
    private val astroCalculator = AstroCalculator()
    private val verdictEngine = VerdictEngine()

    // New York, NY — matches the fixture
    private val location = SavedLocation(
        id = "1",
        name = "New York",
        cityState = "New York, NY",
        latitude = 40.7146,
        longitude = -74.0071,
    )
    private val zone = ZoneId.of("America/New_York")
    private val settings = AppSettings()

    // Fixture starts on 2026-02-16
    private val today = LocalDate.of(2026, 2, 16)

    private lateinit var weatherByDate: Map<LocalDate, DayResponse>
    private lateinit var repo: ForecastRepository
    private lateinit var forecast: List<name.kishinevsky.michael.moonriseassistant.model.ForecastDay>

    @BeforeAll
    fun setUp() {
        // Given: recorded weather fixture parsed into a date map
        val fixtureText = javaClass.classLoader!!
            .getResource("fixtures/visual-crossing-timeline-response.json")!!
            .readText()
        val response = json.decodeFromString<TimelineResponse>(fixtureText)
        weatherByDate = response.days.associateBy { LocalDate.parse(it.datetime) }

        // The repo needs an API and cache DAO for construction, but buildForecast doesn't use them
        repo = ForecastRepository(
            api = StubVisualCrossingApi(),
            weatherCacheDao = StubWeatherCacheDao(),
            astroCalculator = astroCalculator,
            verdictEngine = verdictEngine,
        )

        // When: we build the forecast
        forecast = repo.buildForecast(weatherByDate, location, settings, zone, today)
    }

    @Test
    fun `produces a non-empty forecast list`() {
        // Then: the forecast has entries (phase window days with moonrise)
        assertThat(forecast).isNotEmpty()
    }

    @Test
    fun `today is always the first entry in forecast`() {
        // Then: the first forecast day is today, regardless of phase window
        assertThat(forecast.first().date)
            .describedAs("First forecast entry should be today")
            .isEqualTo(today)
    }

    @Test
    fun `all days after today are within phase window`() {
        // Then: every day after today is within the phase window (today itself may be outside)
        val upcoming = forecast.filter { it.date.isAfter(today) }
        for (day in upcoming) {
            assertThat(astroCalculator.isInPhaseWindow(
                day.date, settings.daysBeforeFullMoon, settings.daysAfterFullMoon
            )).describedAs("Date ${day.date} should be in phase window")
                .isTrue()
        }
    }

    @Test
    fun `days outside phase window are excluded except today`() {
        // Then: the forecast date range spans multiple months but only includes phase window days
        //       (today is always included regardless of phase window)
        val forecastDates = forecast.map { it.date }.toSet()
        val endDate = today.plusMonths(settings.forecastPeriodMonths.toLong())

        // Count all days after today that are NOT in the phase window — none should appear
        var outsideCount = 0
        var date = today.plusDays(1)
        while (!date.isAfter(endDate)) {
            if (!astroCalculator.isInPhaseWindow(date, settings.daysBeforeFullMoon, settings.daysAfterFullMoon)) {
                assertThat(forecastDates).doesNotContain(date)
                outsideCount++
            }
            date = date.plusDays(1)
        }
        assertThat(outsideCount)
            .describedAs("Many days after today should be outside the phase window")
            .isGreaterThan(50)
    }

    @Test
    fun `days within weather range have weather data populated`() {
        // Then: days that fall within the fixture's date range have non-null weather fields
        val weatherDates = weatherByDate.keys
        val daysWithWeather = forecast.filter { it.date in weatherDates }

        assertThat(daysWithWeather).isNotEmpty()
        for (day in daysWithWeather) {
            assertThat(day.weather)
                .describedAs("Day ${day.date} should have known weather")
                .isNotEqualTo(WeatherCondition.UNKNOWN)
            assertThat(day.temperatureF)
                .describedAs("Day ${day.date} should have temperature")
                .isNotNull()
            assertThat(day.windSpeedMph)
                .describedAs("Day ${day.date} should have wind speed")
                .isNotNull()
        }
    }

    @Test
    fun `days beyond weather range have UNKNOWN weather`() {
        // Then: days outside the fixture's date range have UNKNOWN weather and null weather fields
        val weatherDates = weatherByDate.keys
        val daysWithoutWeather = forecast.filter { it.date !in weatherDates }

        assertThat(daysWithoutWeather).isNotEmpty()
        for (day in daysWithoutWeather) {
            assertThat(day.weather)
                .describedAs("Day ${day.date} should have UNKNOWN weather")
                .isEqualTo(WeatherCondition.UNKNOWN)
            assertThat(day.temperatureF)
                .describedAs("Day ${day.date} should have null temperature")
                .isNull()
        }
    }

    @Test
    fun `astro fields are populated for all days`() {
        // Then: every day has moonrise, sunset, and azimuth populated
        for (day in forecast) {
            assertThat(day.moonrise)
                .describedAs("Day ${day.date} should have moonrise")
                .isNotNull()
            assertThat(day.sunset)
                .describedAs("Day ${day.date} should have sunset")
                .isNotNull()
            assertThat(day.azimuthDegrees)
                .describedAs("Day ${day.date} azimuth should be 0-360")
                .isBetween(0, 360)
            assertThat(day.azimuthCardinal)
                .describedAs("Day ${day.date} should have cardinal direction")
                .isIn("N", "NE", "E", "SE", "S", "SW", "W", "NW")
        }
    }

    @Test
    fun `verdict is applied correctly`() {
        // Then: every day has a verdict that matches re-evaluation with its phase window status
        for (day in forecast) {
            val inPhaseWindow = astroCalculator.isInPhaseWindow(
                day.date, settings.daysBeforeFullMoon, settings.daysAfterFullMoon
            )
            val reEvaluated = verdictEngine.evaluate(day, settings, inPhaseWindow)
            assertThat(day.verdict)
                .describedAs("Day ${day.date} verdict should match engine evaluation")
                .isEqualTo(reEvaluated.verdict)
            assertThat(day.verdictChecks)
                .describedAs("Day ${day.date} checks should match engine evaluation")
                .isEqualTo(reEvaluated.checks)
        }
    }

    @Test
    fun `forecast contains both GOOD and BAD days`() {
        // Then: across 3 months there should be a mix of verdicts
        val verdicts = forecast.map { it.verdict }.toSet()
        assertThat(verdicts)
            .describedAs("Forecast should contain a mix of GOOD and BAD days")
            .contains(Verdict.GOOD, Verdict.BAD)
    }

    // Stub implementations — buildForecast doesn't call API or cache
    private class StubVisualCrossingApi : VisualCrossingApi(
        client = io.ktor.client.HttpClient(),
        apiKey = "unused",
    )

    private class StubWeatherCacheDao : WeatherCacheDao {
        override suspend fun insertAll(entries: List<WeatherCacheEntity>) {}
        override suspend fun getForLocation(locationId: Long) = emptyList<WeatherCacheEntity>()
        override suspend fun deleteStaleEntries(cutoffTimestamp: Long) {}
        override suspend fun deleteForLocation(locationId: Long) {}
    }
}
