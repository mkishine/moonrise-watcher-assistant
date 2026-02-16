package name.kisinievsky.michael.moonriseassistant.api

import org.assertj.core.api.Assertions.assertThat
import org.json.JSONObject
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import java.net.HttpURLConnection
import java.net.URI
import java.util.Properties

/**
 * Live smoke test against the Visual Crossing Timeline Weather API.
 * Requires a valid API key in secrets.properties at the project root.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VisualCrossingApiTest {

    private lateinit var responseBody: String
    private lateinit var json: JSONObject
    private var statusCode: Int = 0

    @BeforeAll
    fun callApi() {
        // Given: a valid API key from secrets.properties
        val secretsFile = File(System.getProperty("user.dir")!!).resolve("../secrets.properties")
        require(secretsFile.exists()) {
            "secrets.properties not found at ${secretsFile.absolutePath}. " +
                "Create it with VISUAL_CROSSING_API_KEY=<your-key>"
        }
        val props = Properties().apply { secretsFile.inputStream().use { load(it) } }
        val apiKey = props.getProperty("VISUAL_CROSSING_API_KEY")
            ?: error("VISUAL_CROSSING_API_KEY not set in secrets.properties")

        // When: we call the Timeline API for New York, next 15 days
        val url = URI(
            "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/" +
                "New%20York%2CNY/next15days?unitGroup=us&key=$apiKey&include=days,hours"
        ).toURL()

        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 15_000
        connection.readTimeout = 15_000

        statusCode = connection.responseCode
        responseBody = connection.inputStream.bufferedReader().readText()
        connection.disconnect()

        json = JSONObject(responseBody)

        // Save fixture for future replay tests
        val fixtureDir = File(System.getProperty("user.dir")!!)
            .resolve("src/test/resources/fixtures")
        fixtureDir.mkdirs()
        fixtureDir.resolve("visual-crossing-timeline-response.json").writeText(responseBody)
    }

    @Test
    fun `response status is 200`() {
        // Then: the API returns success
        assertThat(statusCode).isEqualTo(200)
    }

    @Test
    fun `response contains 15 or 16 daily entries`() {
        // Then: the days array has 15–16 entries (today + next 15 days; count may vary by time zone)
        val days = json.getJSONArray("days")
        assertThat(days.length()).isBetween(15, 16)
    }

    @Test
    fun `each day has required weather fields`() {
        // Then: every day object includes the fields we need
        val days = json.getJSONArray("days")
        val requiredFields = listOf(
            "datetime", "cloudcover", "conditions", "moonphase", "sunrise", "sunset", "precipprob"
        )
        for (i in 0 until days.length()) {
            val day = days.getJSONObject(i)
            for (field in requiredFields) {
                assertThat(day.has(field))
                    .describedAs("Day $i missing field '$field'")
                    .isTrue()
            }
        }
    }

    @Test
    fun `each day has hourly array with cloudcover`() {
        // Then: every day with hourly data has an hours array and each hour has cloudcover
        // Note: the API may omit hours for the last day in the range
        val days = json.getJSONArray("days")
        var daysWithHours = 0
        for (i in 0 until days.length()) {
            val day = days.getJSONObject(i)
            if (!day.has("hours")) {
                continue
            }
            val hours = day.getJSONArray("hours")
            if (hours.length() == 0) {
                continue
            }
            daysWithHours++
            for (h in 0 until hours.length()) {
                assertThat(hours.getJSONObject(h).has("cloudcover"))
                    .describedAs("Day $i hour $h missing 'cloudcover'")
                    .isTrue()
            }
        }
        assertThat(daysWithHours)
            .describedAs("Expected at least 14 days with hourly data")
            .isGreaterThanOrEqualTo(14)
    }
}
