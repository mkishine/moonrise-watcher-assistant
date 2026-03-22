package name.kishinevsky.michael.moonriseassistant.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import name.kishinevsky.michael.moonriseassistant.setThemedContent
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test

class AddLocationScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun firstTimeContext_showsSetUpLocationTitle() {
        // Given: AddLocationScreen in first-time context
        composeTestRule.setThemedContent {
            AddLocationScreen(context = AddLocationContext.FIRST_TIME)
        }

        // Then: title shows "Set Up Location"
        composeTestRule.onNodeWithText("Set Up Location").assertIsDisplayed()
    }

    @Test
    fun firstTimeContext_showsGetStartedButton() {
        // Given: AddLocationScreen in first-time context
        composeTestRule.setThemedContent {
            AddLocationScreen(context = AddLocationContext.FIRST_TIME)
        }

        // Then: button shows "Get Started"
        composeTestRule.onNodeWithText("Get Started").assertIsDisplayed()
    }

    @Test
    fun firstTimeContext_hasNoBackButton() {
        // Given: AddLocationScreen in first-time context
        composeTestRule.setThemedContent {
            AddLocationScreen(context = AddLocationContext.FIRST_TIME)
        }

        // Then: no back button
        composeTestRule.onNodeWithContentDescription("Back").assertDoesNotExist()
    }

    @Test
    fun additionalContext_showsAddLocationTitle() {
        // Given: AddLocationScreen in additional context
        composeTestRule.setThemedContent {
            AddLocationScreen(context = AddLocationContext.ADDITIONAL)
        }

        // Then: title shows "Add Location"
        composeTestRule.onNodeWithText("Add Location").assertIsDisplayed()
    }

    @Test
    fun additionalContext_showsSaveButton() {
        // Given: AddLocationScreen in additional context
        composeTestRule.setThemedContent {
            AddLocationScreen(context = AddLocationContext.ADDITIONAL)
        }

        // Then: button shows "Save"
        composeTestRule.onNodeWithText("Save").assertIsDisplayed()
    }

    @Test
    fun additionalContext_backButtonFiresCallback() {
        // Given: AddLocationScreen with back callback tracker
        var backClicked = false
        composeTestRule.setThemedContent {
            AddLocationScreen(
                context = AddLocationContext.ADDITIONAL,
                onBack = { backClicked = true },
            )
        }

        // When: clicking back
        composeTestRule.onNodeWithContentDescription("Back").performClick()

        // Then: callback was triggered
        assertThat(backClicked).isTrue()
    }

    @Test
    fun cityMode_showsCityField() {
        // Given: AddLocationScreen in city mode
        composeTestRule.setThemedContent {
            AddLocationScreen(inputMode = LocationInputMode.CITY)
        }

        // Then: City, State label is present
        composeTestRule.onNodeWithText("City, State").assertIsDisplayed()
    }

    @Test
    fun coordinatesMode_showsLatLonFields() {
        // Given: AddLocationScreen in coordinates mode
        composeTestRule.setThemedContent {
            AddLocationScreen(inputMode = LocationInputMode.COORDINATES)
        }

        // Then: Latitude and Longitude labels are present
        composeTestRule.onNodeWithText("Latitude").assertIsDisplayed()
        composeTestRule.onNodeWithText("Longitude").assertIsDisplayed()
    }

    @Test
    fun errorMessage_isDisplayed() {
        // Given: AddLocationScreen with an error
        composeTestRule.setThemedContent {
            AddLocationScreen(errorMessage = "City not found")
        }

        // Then: error message with warning is displayed
        composeTestRule.onNodeWithText("\u26A0 City not found").assertIsDisplayed()
    }

    @Test
    fun loading_showsVerifyingAndDisablesButton() {
        // Given: AddLocationScreen in loading state
        composeTestRule.setThemedContent {
            AddLocationScreen(
                context = AddLocationContext.ADDITIONAL,
                isLoading = true,
            )
        }

        // Then: "Verifying..." text is shown and button is disabled
        composeTestRule.onNodeWithText("Verifying...", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Verifying...", substring = true)
            .assertIsNotEnabled()
    }
}
