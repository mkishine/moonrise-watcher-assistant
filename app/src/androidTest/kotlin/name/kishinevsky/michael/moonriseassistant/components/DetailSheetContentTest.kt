package name.kishinevsky.michael.moonriseassistant.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import name.kishinevsky.michael.moonriseassistant.preview.SampleData
import name.kishinevsky.michael.moonriseassistant.setThemedContent
import org.junit.Rule
import org.junit.Test

class DetailSheetContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysAstronomicalData() {
        // Given: DetailSheetContent with a good day
        composeTestRule.setThemedContent {
            DetailSheetContent(days = listOf(SampleData.detailGoodDay))
        }

        // Then: astronomical section header and times are displayed
        composeTestRule.onNodeWithText("ASTRONOMICAL").assertIsDisplayed()
        composeTestRule.onNodeWithText("5:35 PM").assertIsDisplayed()  // sunset
        composeTestRule.onNodeWithText("7:08 PM").assertIsDisplayed()  // moonrise
    }

    @Test
    fun displaysWeatherData() {
        // Given: DetailSheetContent with a good day (has weather data)
        composeTestRule.setThemedContent {
            DetailSheetContent(days = listOf(SampleData.detailGoodDay))
        }

        // Then: weather section header and data are displayed
        composeTestRule.onNodeWithText("WEATHER").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cloud cover 40%", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("42\u00B0F", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("12 mph NW").assertIsDisplayed()
    }

    @Test
    fun weatherUnknown_showsUnavailableMessage() {
        // Given: DetailSheetContent with a weather-unknown day
        composeTestRule.setThemedContent {
            DetailSheetContent(days = listOf(SampleData.detailWeatherUnknown))
        }

        // Then: unavailable message is displayed
        composeTestRule.onNodeWithText("Weather forecast unavailable", substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun displaysVerdictConstraints() {
        // Given: DetailSheetContent with a good day
        composeTestRule.setThemedContent {
            DetailSheetContent(days = listOf(SampleData.detailGoodDay))
        }

        // Then: checkmark characters with constraint labels are displayed
        composeTestRule.onNodeWithText("Moon in phase window").assertIsDisplayed()
        composeTestRule.onNodeWithText("Moonrise after sunset").assertIsDisplayed()
    }

    @Test
    fun displaysSwipeHint() {
        // Given: DetailSheetContent with any day
        composeTestRule.setThemedContent {
            DetailSheetContent(days = listOf(SampleData.detailGoodDay))
        }

        // Then: swipe hint is displayed
        composeTestRule.onNodeWithText("swipe between days", substring = true).assertIsDisplayed()
    }
}
