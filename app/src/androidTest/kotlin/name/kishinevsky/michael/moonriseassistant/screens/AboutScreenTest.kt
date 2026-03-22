package name.kishinevsky.michael.moonriseassistant.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import name.kishinevsky.michael.moonriseassistant.setThemedContent
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test

class AboutScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysAppName() {
        // Given: About screen
        composeTestRule.setThemedContent {
            AboutScreen()
        }

        // Then: app name is displayed
        composeTestRule.onNodeWithText("Moonrise Watcher").assertIsDisplayed()
    }

    @Test
    fun displaysHowGoodNightsAreDeterminedSection() {
        // Given: About screen
        composeTestRule.setThemedContent {
            AboutScreen()
        }

        // Then: section heading is displayed
        composeTestRule.onNodeWithText("How Good Nights Are Determined").assertIsDisplayed()
    }

    @Test
    fun displaysGlossarySectionWithAzimuthEntry() {
        // Given: About screen
        composeTestRule.setThemedContent {
            AboutScreen()
        }

        // Then: Glossary heading and Azimuth entry are displayed
        composeTestRule.onNodeWithText("Glossary").assertIsDisplayed()
        composeTestRule.onNodeWithText("Azimuth").assertIsDisplayed()
    }

    @Test
    fun backButtonFiresCallback() {
        // Given: About screen with callback tracker
        var backClicked = false
        composeTestRule.setThemedContent {
            AboutScreen(onBack = { backClicked = true })
        }

        // When: clicking back
        composeTestRule.onNodeWithContentDescription("Back").performClick()

        // Then: callback was triggered
        assertThat(backClicked).isTrue()
    }
}
