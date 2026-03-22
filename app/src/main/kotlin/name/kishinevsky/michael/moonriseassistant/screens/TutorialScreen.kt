package name.kishinevsky.michael.moonriseassistant.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

private data class TutorialPage(val heading: String, val body: String)

private val TUTORIAL_PAGES = listOf(
    TutorialPage(
        heading = "What is Moonrise Watcher?",
        body = "Moonrise Watcher helps you find the best nights to watch the full moon rise. " +
            "It combines moon phase data, timing constraints, and weather forecasts to tell you " +
            "at a glance whether tonight — or any upcoming night — is worth heading outside.",
    ),
    TutorialPage(
        heading = "What Makes a Good Night?",
        body = "Three things need to line up: the moon must be near its full phase (within your " +
            "configurable viewing window), it must rise after sunset and before your bedtime, " +
            "and the sky must be clear or mostly clear. All three \u2713 means a GOOD night.",
    ),
    TutorialPage(
        heading = "Reading the Forecast",
        body = "The forecast list shows only the nights inside your viewing window. " +
            "A green GOOD badge means all criteria pass. A red BAD badge shows which check " +
            "failed. Tap any row to open the detail sheet with exact times, azimuth, " +
            "temperature, wind, and cloud cover.",
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialScreen(
    onDismiss: () -> Unit = {},
) {
    val pagerState = rememberPagerState(pageCount = { TUTORIAL_PAGES.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    TextButton(onClick = onDismiss) {
                        Text("Skip")
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) { page ->
                TutorialPageContent(
                    page = TUTORIAL_PAGES[page],
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp),
                )
            }

            // Page indicator dots
            Row(
                modifier = Modifier.padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                repeat(TUTORIAL_PAGES.size) { index ->
                    val isSelected = pagerState.currentPage == index
                    Surface(
                        modifier = Modifier.size(if (isSelected) 10.dp else 8.dp),
                        shape = CircleShape,
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.outlineVariant
                        },
                    ) {}
                }
            }

            // Next / Done button
            val isLastPage = pagerState.currentPage == TUTORIAL_PAGES.lastIndex
            Button(
                onClick = {
                    if (isLastPage) {
                        onDismiss()
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
            ) {
                Text(if (isLastPage) "Done" else "Next")
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun TutorialPageContent(page: TutorialPage, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = page.heading,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = page.body,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}
