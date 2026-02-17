package name.kishinevsky.michael.moonriseassistant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import name.kishinevsky.michael.moonriseassistant.model.ForecastDay
import name.kishinevsky.michael.moonriseassistant.model.Verdict
import name.kishinevsky.michael.moonriseassistant.model.VerdictChecks
import name.kishinevsky.michael.moonriseassistant.model.WeatherCondition
import name.kishinevsky.michael.moonriseassistant.ui.theme.BadRed
import name.kishinevsky.michael.moonriseassistant.ui.theme.BadRedLight
import name.kishinevsky.michael.moonriseassistant.ui.theme.GoodGreen
import name.kishinevsky.michael.moonriseassistant.ui.theme.GoodGreenLight
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun TodaySection(
    day: ForecastDay,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header row: "TODAY  Wed, Feb 12    ● GOOD"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "TODAY",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "  " + formatDate(day),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                VerdictBadge(day.verdict, day.verdictChecks)
            }

            // Detail rows
            Column(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                DetailRow("Sunset", formatTime(day.sunset))
                DetailRow("Moonrise", formatTime(day.moonrise))
                DetailRow("Azimuth", "${day.azimuthDegrees}° ${day.azimuthCardinal}")
                DetailRow("Weather", weatherLabel(day.weather))
                DetailRow("Temp", formatTemperature(day.temperatureF, day.windchillF))
                DetailRow("Wind", formatWind(day.windSpeedMph))
            }
        }
    }
}

@Composable
private fun VerdictBadge(verdict: Verdict, checks: VerdictChecks) {
    val reason = checks.badgeReason()
    val (bg, fg, label) = when (verdict) {
        Verdict.GOOD -> Triple(GoodGreenLight, GoodGreen, "GOOD")
        Verdict.BAD -> Triple(
            BadRedLight,
            BadRed,
            if (reason != null) "BAD ($reason)" else "BAD",
        )
    }
    Text(
        text = "● $label",
        color = fg,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .background(bg, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
    )
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(end = 12.dp),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
        )
    }
}

private val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")

private fun formatTime(time: java.time.LocalTime): String = time.format(timeFormatter)

private fun formatDate(day: ForecastDay): String {
    val dow = day.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US)
    val month = day.date.month.getDisplayName(TextStyle.SHORT, Locale.US)
    return "$dow, $month ${day.date.dayOfMonth}"
}

private fun weatherLabel(condition: WeatherCondition): String = when (condition) {
    WeatherCondition.CLEAR -> "☀ Clear"
    WeatherCondition.PARTLY_CLOUDY -> "⛅ Partly cloudy"
    WeatherCondition.CLOUDY -> "☁ Cloudy"
    WeatherCondition.UNKNOWN -> "? Weather unknown"
}

private fun formatTemperature(temperatureF: Int?, windchillF: Int?): String {
    if (temperatureF == null) return "—"
    return if (windchillF != null && windchillF != temperatureF) {
        "${temperatureF}°F  Feels ${windchillF}°F"
    } else {
        "${temperatureF}°F"
    }
}

private fun formatWind(windSpeedMph: Int?): String {
    return if (windSpeedMph != null) "$windSpeedMph mph" else "—"
}
