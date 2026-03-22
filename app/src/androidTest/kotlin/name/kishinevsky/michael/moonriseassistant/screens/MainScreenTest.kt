package name.kishinevsky.michael.moonriseassistant.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import name.kishinevsky.michael.moonriseassistant.model.ForecastDay
import name.kishinevsky.michael.moonriseassistant.preview.SampleData
import name.kishinevsky.michael.moonriseassistant.setThemedContent
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test

class MainScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysLocationNameInTopBar() {
        // Given: MainScreen with sample data
        composeTestRule.setThemedContent {
            MainScreen(
                locationName = SampleData.LOCATION_NAME,
                today = SampleData.today,
                upcomingDays = SampleData.upcomingDays,
            )
        }

        // Then: location name is displayed
        composeTestRule.onNodeWithText("Home \u2014 Seattle, WA").assertIsDisplayed()
    }

    @Test
    fun displaysTodayLabel() {
        // Given: MainScreen with sample data
        composeTestRule.setThemedContent {
            MainScreen(
                locationName = SampleData.LOCATION_NAME,
                today = SampleData.today,
                upcomingDays = SampleData.upcomingDays,
            )
        }

        // Then: TODAY label is displayed
        composeTestRule.onNodeWithText("TODAY", substring = true).assertIsDisplayed()
    }

    @Test
    fun displaysGoodVerdictBadge() {
        // Given: MainScreen with a GOOD today
        composeTestRule.setThemedContent {
            MainScreen(
                locationName = SampleData.LOCATION_NAME,
                today = SampleData.today,
                upcomingDays = SampleData.upcomingDays,
            )
        }

        // Then: GOOD badge text is present
        composeTestRule.onNodeWithText("\u25CF GOOD", substring = true).assertIsDisplayed()
    }

    @Test
    fun displaysUpcomingHeader() {
        // Given: MainScreen with upcoming days
        composeTestRule.setThemedContent {
            MainScreen(
                locationName = SampleData.LOCATION_NAME,
                today = SampleData.today,
                upcomingDays = SampleData.upcomingDays,
            )
        }

        // Then: UPCOMING header is displayed
        composeTestRule.onNodeWithText("UPCOMING").assertIsDisplayed()
    }

    @Test
    fun clickingForecastItemFiresCallback() {
        // Given: MainScreen with a callback tracker
        var clickedDay: ForecastDay? = null
        composeTestRule.setThemedContent {
            MainScreen(
                locationName = SampleData.LOCATION_NAME,
                today = SampleData.today,
                upcomingDays = SampleData.upcomingDays,
                onDayClick = { clickedDay = it },
            )
        }

        // When: clicking the first upcoming day (Fri, Feb 13)
        composeTestRule.onNodeWithText("Fri, Feb 13").performClick()

        // Then: callback received the correct day
        assertThat(clickedDay).isEqualTo(SampleData.upcomingDays[0])
    }

    @Test
    fun menuClickFiresCallback() {
        // Given: MainScreen with a menu callback tracker
        var menuClicked = false
        composeTestRule.setThemedContent {
            MainScreen(
                locationName = SampleData.LOCATION_NAME,
                today = SampleData.today,
                upcomingDays = SampleData.upcomingDays,
                onMenuClick = { menuClicked = true },
            )
        }

        // When: clicking the menu icon
        composeTestRule.onNodeWithContentDescription("Menu").performClick()

        // Then: callback was triggered
        assertThat(menuClicked).isTrue()
    }

    @Test
    fun refreshButtonIsDisplayed() {
        // Given: MainScreen with sample data
        composeTestRule.setThemedContent {
            MainScreen(
                locationName = SampleData.LOCATION_NAME,
                today = SampleData.today,
                upcomingDays = SampleData.upcomingDays,
            )
        }

        // Then: refresh button is present in the top bar
        composeTestRule.onNodeWithContentDescription("Refresh").assertIsDisplayed()
    }

    @Test
    fun refreshButtonClickFiresCallback() {
        // Given: MainScreen with a refresh callback tracker
        var refreshClicked = false
        composeTestRule.setThemedContent {
            MainScreen(
                locationName = SampleData.LOCATION_NAME,
                today = SampleData.today,
                upcomingDays = SampleData.upcomingDays,
                onRefresh = { refreshClicked = true },
            )
        }

        // When: clicking the refresh icon
        composeTestRule.onNodeWithContentDescription("Refresh").performClick()

        // Then: callback was triggered
        assertThat(refreshClicked).isTrue()
    }
}
