package name.kishinevsky.michael.moonriseassistant

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule

/**
 * Sets content wrapped in MaterialTheme for consistent test rendering.
 * Uses MaterialTheme directly instead of MoonriseAssistantTheme to avoid
 * the Activity window cast in the theme's SideEffect.
 */
fun ComposeContentTestRule.setThemedContent(content: @Composable () -> Unit) {
    setContent {
        MaterialTheme(content = content)
    }
}
