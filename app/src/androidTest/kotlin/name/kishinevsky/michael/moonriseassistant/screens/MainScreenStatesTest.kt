package name.kishinevsky.michael.moonriseassistant.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import name.kishinevsky.michael.moonriseassistant.preview.SampleData
import name.kishinevsky.michael.moonriseassistant.setThemedContent
import org.junit.Rule
import org.junit.Test

class MainScreenStatesTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun firstTime_displaysWelcomeAndAddLocation() {
        // Given: first-time screen
        composeTestRule.setThemedContent {
            MainScreenFirstTime(onAddLocation = {})
        }

        // Then: welcome text and Add Location button are present
        composeTestRule.onNodeWithText("Welcome!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Add Location").assertIsDisplayed()
    }

    @Test
    fun firstTime_addLocationClickFiresCallback() {
        // Given: first-time screen with callback tracker
        var clicked = false
        composeTestRule.setThemedContent {
            MainScreenFirstTime(onAddLocation = { clicked = true })
        }

        // When: clicking Add Location
        composeTestRule.onNodeWithText("Add Location").performClick()

        // Then: callback was triggered
        assert(clicked) { "Expected onAddLocation to be triggered" }
    }

    @Test
    fun firstTime_displaysAppTitle() {
        // Given: first-time screen
        composeTestRule.setThemedContent {
            MainScreenFirstTime(onAddLocation = {})
        }

        // Then: app title is in top bar
        composeTestRule.onNodeWithText("Moonrise Assistant").assertIsDisplayed()
    }

    @Test
    fun loading_displaysLocationName() {
        // Given: loading screen
        composeTestRule.setThemedContent {
            MainScreenLoading(locationName = SampleData.LOCATION_NAME)
        }

        // Then: location name is displayed
        composeTestRule.onNodeWithText("Home \u2014 Seattle, WA").assertIsDisplayed()
    }

    @Test
    fun error_displaysMessageAndRetryButton() {
        // Given: error screen
        composeTestRule.setThemedContent {
            MainScreenError(
                locationName = SampleData.LOCATION_NAME,
                errorMessage = SampleData.ERROR_NETWORK,
                onRetry = {},
            )
        }

        // Then: error text and Retry button are present
        composeTestRule.onNodeWithText(SampleData.ERROR_NETWORK).assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
    }

    @Test
    fun error_retryClickFiresCallback() {
        // Given: error screen with callback tracker
        var retried = false
        composeTestRule.setThemedContent {
            MainScreenError(
                locationName = SampleData.LOCATION_NAME,
                errorMessage = SampleData.ERROR_NETWORK,
                onRetry = { retried = true },
            )
        }

        // When: clicking Retry
        composeTestRule.onNodeWithText("Retry").performClick()

        // Then: callback was triggered
        assert(retried) { "Expected onRetry to be triggered" }
    }

    @Test
    fun empty_displaysNextFullMoonDate() {
        // Given: empty forecast screen
        composeTestRule.setThemedContent {
            MainScreenEmpty(
                locationName = SampleData.LOCATION_NAME,
                today = SampleData.today,
                nextFullMoonDate = SampleData.NEXT_FULL_MOON_DATE,
            )
        }

        // Then: next full moon info is displayed
        composeTestRule.onNodeWithText("Next full moon: Mar 12").assertIsDisplayed()
    }
}
