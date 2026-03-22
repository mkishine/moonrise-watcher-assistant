package name.kishinevsky.michael.moonriseassistant.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import name.kishinevsky.michael.moonriseassistant.setThemedContent
import org.junit.Rule
import org.junit.Test

class TutorialScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysFirstPageHeading() {
        // Given: Tutorial screen
        composeTestRule.setThemedContent {
            TutorialScreen()
        }

        // Then: first page heading is displayed
        composeTestRule.onNodeWithText("What is Moonrise Watcher?").assertIsDisplayed()
    }

    @Test
    fun skipButtonFiresDismissCallback() {
        // Given: Tutorial screen with callback tracker
        var dismissed = false
        composeTestRule.setThemedContent {
            TutorialScreen(onDismiss = { dismissed = true })
        }

        // When: clicking Skip
        composeTestRule.onNodeWithText("Skip").performClick()

        // Then: dismiss callback was triggered
        assert(dismissed) { "Expected onDismiss to be triggered by Skip" }
    }

    @Test
    fun nextButtonAdvancesToPage2() {
        // Given: Tutorial screen on page 1
        composeTestRule.setThemedContent {
            TutorialScreen()
        }

        // When: clicking Next
        composeTestRule.onNodeWithText("Next").performClick()

        // Then: page 2 heading is displayed
        composeTestRule.onNodeWithText("What Makes a Good Night?").assertIsDisplayed()
    }

    @Test
    fun doneButtonOnLastPageFiresDismissCallback() {
        // Given: Tutorial screen, navigate to last page
        var dismissed = false
        composeTestRule.setThemedContent {
            TutorialScreen(onDismiss = { dismissed = true })
        }

        // When: advancing to last page and clicking Done
        composeTestRule.onNodeWithText("Next").performClick()
        composeTestRule.onNodeWithText("Next").performClick()
        composeTestRule.onNodeWithText("Done").performClick()

        // Then: dismiss callback was triggered
        assert(dismissed) { "Expected onDismiss to be triggered by Done" }
    }
}
