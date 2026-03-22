package name.kishinevsky.michael.moonriseassistant.network

import kotlinx.serialization.json.Json
import name.kishinevsky.michael.moonriseassistant.network.model.TimelineResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * Replay tests that deserialize a recorded Visual Crossing API response
 * using kotlinx.serialization. No live API calls.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VisualCrossingApiTest {

    private val json = Json { ignoreUnknownKeys = true }
    private lateinit var response: TimelineResponse

    @BeforeAll
    fun loadFixture() {
        // Given: a recorded API response fixture
        val fixtureText = javaClass.classLoader!!
            .getResource("fixtures/visual-crossing-timeline-response.json")!!
            .readText()

        // When: we deserialize it
        response = json.decodeFromString<TimelineResponse>(fixtureText)
    }

    @Test
    fun `fixture deserializes with 15 or 16 days`() {
        // Then: the response has the expected number of days
        assertThat(response.days.size).isBetween(15, 16)
    }

    @Test
    fun `top-level fields are populated`() {
        // Then: key top-level fields are present
        assertThat(response.latitude).isBetween(-90.0, 90.0)
        assertThat(response.longitude).isBetween(-180.0, 180.0)
        assertThat(response.resolvedAddress).isNotBlank()
        assertThat(response.timezone).isNotBlank()
    }

    @Test
    fun `each day has required weather fields`() {
        // Then: every day has the fields the app needs
        for (day in response.days) {
            assertThat(day.datetime).matches("\\d{4}-\\d{2}-\\d{2}")
            assertThat(day.cloudcover).isBetween(0.0, 100.0)
            assertThat(day.windspeed).isGreaterThanOrEqualTo(0.0)
            assertThat(day.conditions).isNotBlank()
            assertThat(day.moonphase).isBetween(0.0, 1.0)
            assertThat(day.sunrise).isNotBlank()
            assertThat(day.sunset).isNotBlank()
            assertThat(day.precipprob).isBetween(0.0, 100.0)
        }
    }

    @Test
    fun `temperature fields parse correctly`() {
        // Then: temperature fields are reasonable values
        val firstDay = response.days.first()
        assertThat(firstDay.tempmax).isBetween(-50.0, 150.0)
        assertThat(firstDay.tempmin).isBetween(-50.0, 150.0)
        assertThat(firstDay.temp).isBetween(-50.0, 150.0)
        assertThat(firstDay.tempmin).isLessThanOrEqualTo(firstDay.tempmax)
    }

    @Test
    fun `hourly data is present for near-term days`() {
        // Then: at least 14 days have hourly data with 24 hours each
        val daysWithHours = response.days.count { it.hours.isNotEmpty() }
        assertThat(daysWithHours)
            .describedAs("At least 14 days should have hourly data")
            .isGreaterThanOrEqualTo(14)

        val firstDayHours = response.days.first().hours
        assertThat(firstDayHours).hasSize(24)
    }

    @Test
    fun `hourly cloudcover is present and in range`() {
        // Then: every hour of the first day has a valid cloudcover value
        val hours = response.days.first().hours
        for (hour in hours) {
            assertThat(hour.cloudcover).isBetween(0.0, 100.0)
        }
    }

    @Test
    fun `preciptype can be null or a list of strings`() {
        // Then: preciptype is either null or contains known values
        val validTypes = setOf("rain", "snow", "freezingrain", "ice")
        for (day in response.days) {
            if (day.preciptype != null) {
                assertThat(day.preciptype).allSatisfy { type ->
                    assertThat(type).isIn(validTypes)
                }
            }
        }
    }
}
