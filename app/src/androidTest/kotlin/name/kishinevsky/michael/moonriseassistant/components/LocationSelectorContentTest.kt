package name.kishinevsky.michael.moonriseassistant.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import name.kishinevsky.michael.moonriseassistant.model.SavedLocation
import name.kishinevsky.michael.moonriseassistant.preview.SampleData
import name.kishinevsky.michael.moonriseassistant.setThemedContent
import org.junit.Rule
import org.junit.Test

class LocationSelectorContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysAllLocationNames() {
        // Given: LocationSelectorContent with 3 locations
        composeTestRule.setThemedContent {
            LocationSelectorContent(
                locations = SampleData.savedLocations,
                activeLocationId = "1",
            )
        }

        // Then: all location names are displayed
        composeTestRule.onNodeWithText("Home \u2014 Seattle, WA").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cabin \u2014 Leavenworth").assertIsDisplayed()
        composeTestRule.onNodeWithText("Observatory \u2014 Goldendale").assertIsDisplayed()
    }

    @Test
    fun activeLocationHasCheckmark() {
        // Given: LocationSelectorContent with first location active
        composeTestRule.setThemedContent {
            LocationSelectorContent(
                locations = SampleData.savedLocations,
                activeLocationId = "1",
            )
        }

        // Then: checkmark is displayed (for the active location)
        composeTestRule.onNodeWithText("\u2713").assertIsDisplayed()
    }

    @Test
    fun clickingLocationFiresCallback() {
        // Given: LocationSelectorContent with callback tracker
        var selectedLocation: SavedLocation? = null
        composeTestRule.setThemedContent {
            LocationSelectorContent(
                locations = SampleData.savedLocations,
                activeLocationId = "1",
                onLocationSelect = { selectedLocation = it },
            )
        }

        // When: clicking the second location
        composeTestRule.onNodeWithText("Cabin \u2014 Leavenworth").performClick()

        // Then: callback received the correct location
        assert(selectedLocation == SampleData.savedLocations[1]) {
            "Expected second location but got $selectedLocation"
        }
    }

    @Test
    fun displaysAddLocationRow() {
        // Given: LocationSelectorContent
        composeTestRule.setThemedContent {
            LocationSelectorContent(
                locations = SampleData.savedLocations,
                activeLocationId = "1",
            )
        }

        // Then: Add Location row is displayed
        composeTestRule.onNodeWithText("Add Location").assertIsDisplayed()
    }
}
