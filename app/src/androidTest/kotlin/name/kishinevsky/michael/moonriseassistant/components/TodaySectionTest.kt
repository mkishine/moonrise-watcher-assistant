package name.kishinevsky.michael.moonriseassistant.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import name.kishinevsky.michael.moonriseassistant.model.CheckResult
import name.kishinevsky.michael.moonriseassistant.model.Verdict
import name.kishinevsky.michael.moonriseassistant.model.VerdictChecks
import name.kishinevsky.michael.moonriseassistant.model.WeatherCondition
import name.kishinevsky.michael.moonriseassistant.preview.SampleData
import name.kishinevsky.michael.moonriseassistant.setThemedContent
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class TodaySectionTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysSunsetAndMoonriseTimes() {
        // Given: TodaySection with sample today data
        composeTestRule.setThemedContent {
            TodaySection(day = SampleData.today)
        }

        // Then: sunset and moonrise times are displayed
        composeTestRule.onNodeWithText("5:34 PM").assertIsDisplayed()
        composeTestRule.onNodeWithText("6:12 PM").assertIsDisplayed()
    }

    @Test
    fun displaysAzimuth() {
        // Given: TodaySection with sample today data
        composeTestRule.setThemedContent {
            TodaySection(day = SampleData.today)
        }

        // Then: azimuth value is displayed
        composeTestRule.onNodeWithText("98\u00B0 ESE").assertIsDisplayed()
    }

    @Test
    fun displaysWeatherLabel() {
        // Given: TodaySection with CLEAR weather
        composeTestRule.setThemedContent {
            TodaySection(day = SampleData.today)
        }

        // Then: weather label is displayed
        composeTestRule.onNodeWithText("\u2600 Clear", substring = true).assertIsDisplayed()
    }

    @Test
    fun displaysTemperatureWithWindchill() {
        // Given: TodaySection with temp 45 and windchill 38
        composeTestRule.setThemedContent {
            TodaySection(day = SampleData.today)
        }

        // Then: temperature with windchill is displayed
        composeTestRule.onNodeWithText("45\u00B0F  Feels 38\u00B0F").assertIsDisplayed()
    }

    @Test
    fun badVerdictShowsReasonInBadge() {
        // Given: TodaySection with a BAD day due to weather
        val badWeatherDay = SampleData.upcomingDays[1] // Cloudy day, verdict BAD
        composeTestRule.setThemedContent {
            TodaySection(day = badWeatherDay)
        }

        // Then: BAD badge with reason is displayed
        composeTestRule.onNodeWithText("\u25CF BAD (weather)", substring = true).assertIsDisplayed()
    }
}
