package name.kishinevsky.michael.moonriseassistant.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import name.kishinevsky.michael.moonriseassistant.BuildConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBack: () -> Unit = {},
    onHowItWorksClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // ── App info ────────────────────────────────────────────
            Text(
                text = "Moonrise Watcher",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Version ${BuildConfig.VERSION_NAME}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()

            // ── How good nights are determined ──────────────────────
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "How Good Nights Are Determined",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "A night is marked GOOD when three criteria are met:",
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "\u2022 Phase window — the moon is within 2 days before to 5 days after full moon " +
                    "(configurable in Settings).",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "\u2022 Timing — moonrise occurs after sunset (with a configurable tolerance) " +
                    "and before your set bedtime.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "\u2022 Weather — skies are clear or mostly clear at the time of moonrise.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()

            // ── Glossary ────────────────────────────────────────────
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Glossary",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))

            GlossaryEntry(
                term = "Azimuth",
                definition = "The compass direction of moonrise, measured in degrees clockwise from north. " +
                    "0\u00B0 = North, 90\u00B0 = East, 180\u00B0 = South, 270\u00B0 = West.",
            )
            Spacer(modifier = Modifier.height(12.dp))
            GlossaryEntry(
                term = "Full Moon",
                definition = "The lunar phase when the moon is fully illuminated as seen from Earth. " +
                    "This is the center of the viewing window.",
            )
            Spacer(modifier = Modifier.height(12.dp))
            GlossaryEntry(
                term = "Moon Phase",
                definition = "The fraction of the moon\u2019s illuminated surface visible from Earth, " +
                    "cycling from new moon through first quarter, full moon, third quarter, and back.",
            )
            Spacer(modifier = Modifier.height(12.dp))
            GlossaryEntry(
                term = "Windchill",
                definition = "The temperature it feels like outside, accounting for how wind accelerates " +
                    "heat loss from exposed skin. Shown when wind speed is significant.",
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()

            // ── How It Works ────────────────────────────────────────
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.material3.TextButton(
                onClick = onHowItWorksClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("How It Works")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun GlossaryEntry(term: String, definition: String) {
    Text(
        text = term,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.SemiBold,
    )
    Text(
        text = definition,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
