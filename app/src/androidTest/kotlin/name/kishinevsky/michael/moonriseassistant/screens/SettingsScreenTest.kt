package name.kishinevsky.michael.moonriseassistant.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import name.kishinevsky.michael.moonriseassistant.preview.SampleData
import name.kishinevsky.michael.moonriseassistant.setThemedContent
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysSettingsTitle() {
        // Given: Settings screen with default settings
        composeTestRule.setThemedContent {
            SettingsScreen(settings = SampleData.defaultSettings)
        }

        // Then: "Settings" title is displayed
        composeTestRule.onNodeWithText("Settings").assertIsDisplayed()
    }

    @Test
    fun backButtonFiresCallback() {
        // Given: Settings screen with callback tracker
        var backClicked = false
        composeTestRule.setThemedContent {
            SettingsScreen(
                settings = SampleData.defaultSettings,
                onBack = { backClicked = true },
            )
        }

        // When: clicking back
        composeTestRule.onNodeWithContentDescription("Back").performClick()

        // Then: callback was triggered
        assert(backClicked) { "Expected onBack to be triggered" }
    }

    @Test
    fun displaysViewingWindowSteppers() {
        // Given: Settings screen with default settings
        composeTestRule.setThemedContent {
            SettingsScreen(settings = SampleData.defaultSettings)
        }

        // Then: stepper labels and default values are displayed
        composeTestRule.onNodeWithText("Days before full moon").assertIsDisplayed()
        composeTestRule.onNodeWithText("Days after full moon").assertIsDisplayed()
        composeTestRule.onNodeWithText("Forecast period (months)").assertIsDisplayed()
        composeTestRule.onNodeWithText("2").assertIsDisplayed()
        composeTestRule.onNodeWithText("5").assertIsDisplayed()
        composeTestRule.onNodeWithText("3").assertIsDisplayed()
    }

    @Test
    fun displaysTimeConstraints() {
        // Given: Settings screen with default settings
        composeTestRule.setThemedContent {
            SettingsScreen(settings = SampleData.defaultSettings)
        }

        // Then: time constraint label and value are displayed
        composeTestRule.onNodeWithText("Latest moonrise time").assertIsDisplayed()
        composeTestRule.onNodeWithText("11:00 PM").assertIsDisplayed()
    }

    @Test
    fun displaysUnitToggle() {
        // Given: Settings screen with default settings
        composeTestRule.setThemedContent {
            SettingsScreen(settings = SampleData.defaultSettings)
        }

        // Then: unit toggle segments are displayed
        composeTestRule.onNodeWithText("Imperial").assertIsDisplayed()
        composeTestRule.onNodeWithText("Metric").assertIsDisplayed()
    }

    @Test
    fun displaysAboutAndHelp() {
        // Given: Settings screen with default settings
        composeTestRule.setThemedContent {
            SettingsScreen(settings = SampleData.defaultSettings)
        }

        // Then: About & Help row is displayed
        composeTestRule.onNodeWithText("About & Help").assertIsDisplayed()
    }

    @Test
    fun incrementDaysBeforeFiresCallback() {
        // Given: Settings screen with callback tracker
        var newValue: Int? = null
        composeTestRule.setThemedContent {
            SettingsScreen(
                settings = SampleData.defaultSettings,
                onDaysBeforeChange = { newValue = it },
            )
        }

        // When: clicking the first "Increase" button (Days before full moon)
        composeTestRule.onAllNodesWithContentDescription("Increase")[0].performClick()

        // Then: callback received incremented value (2 + 1 = 3)
        assert(newValue == 3) { "Expected 3 but got $newValue" }
    }
}
