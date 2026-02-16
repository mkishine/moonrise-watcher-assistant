package name.kishinevsky.michael.moonriseassistant.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import name.kishinevsky.michael.moonriseassistant.model.AppSettings
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: AppSettings,
    onBack: () -> Unit = {},
    onDaysBeforeChange: (Int) -> Unit = {},
    onDaysAfterChange: (Int) -> Unit = {},
    onForecastPeriodChange: (Int) -> Unit = {},
    onMaxTimeClick: () -> Unit = {},
    onToleranceChange: (Int) -> Unit = {},
    onUnitToggle: (Boolean) -> Unit = {},
    onAboutClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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

            // ── Viewing Window ──────────────────────────────────────
            SettingsSectionHeader("VIEWING WINDOW")
            StepperRow(
                label = "Days before full moon",
                value = settings.daysBeforeFullMoon,
                onDecrement = { onDaysBeforeChange(settings.daysBeforeFullMoon - 1) },
                onIncrement = { onDaysBeforeChange(settings.daysBeforeFullMoon + 1) },
                decrementEnabled = settings.daysBeforeFullMoon > 0,
                incrementEnabled = settings.daysBeforeFullMoon < 7,
            )
            StepperRow(
                label = "Days after full moon",
                value = settings.daysAfterFullMoon,
                onDecrement = { onDaysAfterChange(settings.daysAfterFullMoon - 1) },
                onIncrement = { onDaysAfterChange(settings.daysAfterFullMoon + 1) },
                decrementEnabled = settings.daysAfterFullMoon > 0,
                incrementEnabled = settings.daysAfterFullMoon < 10,
            )
            StepperRow(
                label = "Forecast period (months)",
                value = settings.forecastPeriodMonths,
                onDecrement = { onForecastPeriodChange(settings.forecastPeriodMonths - 1) },
                onIncrement = { onForecastPeriodChange(settings.forecastPeriodMonths + 1) },
                decrementEnabled = settings.forecastPeriodMonths > 1,
                incrementEnabled = settings.forecastPeriodMonths < 12,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Time Constraints ────────────────────────────────────
            SettingsSectionHeader("TIME CONSTRAINTS")
            TimePickerRow(
                label = "Latest moonrise time",
                value = settings.maxMoonriseTime.format(timeDisplayFormatter),
                onClick = onMaxTimeClick,
            )
            StepperRow(
                label = "Before-sunset tolerance (min)",
                value = settings.beforeSunsetToleranceMin,
                onDecrement = { onToleranceChange(settings.beforeSunsetToleranceMin - 5) },
                onIncrement = { onToleranceChange(settings.beforeSunsetToleranceMin + 5) },
                decrementEnabled = settings.beforeSunsetToleranceMin > 0,
                incrementEnabled = settings.beforeSunsetToleranceMin < 120,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Units ───────────────────────────────────────────────
            SettingsSectionHeader("UNITS")
            UnitToggleRow(
                useMetric = settings.useMetric,
                onToggle = onUnitToggle,
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()

            // ── About & Help ────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onAboutClick)
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "About & Help",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
    )
    HorizontalDivider(modifier = Modifier.padding(bottom = 12.dp))
}

@Composable
private fun StepperRow(
    label: String,
    value: Int,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    decrementEnabled: Boolean = true,
    incrementEnabled: Boolean = true,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onDecrement,
                enabled = decrementEnabled,
                modifier = Modifier.size(40.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Decrease",
                )
            }
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
            IconButton(
                onClick = onIncrement,
                enabled = incrementEnabled,
                modifier = Modifier.size(40.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increase",
                )
            }
        }
    }
}

@Composable
private fun TimePickerRow(
    label: String,
    value: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UnitToggleRow(
    useMetric: Boolean,
    onToggle: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Unit system",
            style = MaterialTheme.typography.bodyLarge,
        )
        SingleChoiceSegmentedButtonRow {
            SegmentedButton(
                selected = !useMetric,
                onClick = { onToggle(false) },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
            ) {
                Text("Imperial")
            }
            SegmentedButton(
                selected = useMetric,
                onClick = { onToggle(true) },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
            ) {
                Text("Metric")
            }
        }
    }
}

private val timeDisplayFormatter = DateTimeFormatter.ofPattern("h:mm a")
