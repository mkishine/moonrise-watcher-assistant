package name.kishinevsky.michael.moonriseassistant.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import name.kishinevsky.michael.moonriseassistant.preview.SampleData
import name.kishinevsky.michael.moonriseassistant.setThemedContent
import org.junit.Rule
import org.junit.Test

class ForecastListItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysFormattedDate() {
        // Given: ForecastListItem with first upcoming day (Feb 13)
        composeTestRule.setThemedContent {
            ForecastListItem(
                day = SampleData.upcomingDays[0],
                onClick = {},
            )
        }

        // Then: formatted date is displayed
        composeTestRule.onNodeWithText("Fri, Feb 13").assertIsDisplayed()
    }

    @Test
    fun goodDayShowsFilledDot() {
        // Given: ForecastListItem with a GOOD day
        composeTestRule.setThemedContent {
            ForecastListItem(
                day = SampleData.upcomingDays[0],
                onClick = {},
            )
        }

        // Then: filled dot is present
        composeTestRule.onNodeWithText("\u25CF", substring = true).assertIsDisplayed()
    }

    @Test
    fun clickFiresCallback() {
        // Given: ForecastListItem with callback tracker
        var clicked = false
        composeTestRule.setThemedContent {
            ForecastListItem(
                day = SampleData.upcomingDays[0],
                onClick = { clicked = true },
            )
        }

        // When: clicking the item
        composeTestRule.onNodeWithText("Fri, Feb 13").performClick()

        // Then: callback was triggered
        assert(clicked) { "Expected onClick to be triggered" }
    }

    @Test
    fun badDayWithCloudyWeatherShowsWeatherReason() {
        // Given: ForecastListItem with a BAD day due to cloudy weather (upcomingDays[1])
        composeTestRule.setThemedContent {
            ForecastListItem(
                day = SampleData.upcomingDays[1],
                onClick = {},
            )
        }

        // Then: "weather" reason hint is displayed
        composeTestRule.onNodeWithText("\u00B7 weather", substring = true).assertIsDisplayed()
    }

    @Test
    fun badDayWithTooLateMoonriseShowsTooLateReason() {
        // Given: ForecastListItem with a BAD day because moonrise is too late (upcomingDays[2])
        composeTestRule.setThemedContent {
            ForecastListItem(
                day = SampleData.upcomingDays[2],
                onClick = {},
            )
        }

        // Then: "too late" reason hint is displayed
        composeTestRule.onNodeWithText("\u00B7 too late", substring = true).assertIsDisplayed()
    }

    @Test
    fun goodDayDoesNotShowReasonHint() {
        // Given: ForecastListItem with a GOOD day
        composeTestRule.setThemedContent {
            ForecastListItem(
                day = SampleData.upcomingDays[0],
                onClick = {},
            )
        }

        // Then: no reason hint is shown
        composeTestRule.onNodeWithText("\u00B7", substring = true).assertDoesNotExist()
    }
}
