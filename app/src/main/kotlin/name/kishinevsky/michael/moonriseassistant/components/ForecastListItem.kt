package name.kishinevsky.michael.moonriseassistant.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import name.kishinevsky.michael.moonriseassistant.model.ForecastDay
import name.kishinevsky.michael.moonriseassistant.model.Verdict
import name.kishinevsky.michael.moonriseassistant.model.WeatherCondition
import name.kishinevsky.michael.moonriseassistant.ui.theme.GoodGreen
import name.kishinevsky.michael.moonriseassistant.ui.theme.NeutralGray
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun ForecastListItem(
    day: ForecastDay,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.clickable(onClick = onClick)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
        ) {
            // First line: dot + date
            Row(verticalAlignment = Alignment.CenterVertically) {
                val dotColor = if (day.verdict == Verdict.GOOD) GoodGreen else NeutralGray
                Text(
                    text = if (day.verdict == Verdict.GOOD) "●" else "○",
                    color = dotColor,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(end = 8.dp),
                )
                Text(
                    text = formatItemDate(day),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                )
            }
            // Second line: details
            Text(
                text = buildDetailLine(day),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 24.dp, top = 2.dp),
            )
        }
        HorizontalDivider()
    }
}

private val shortTimeFormatter = DateTimeFormatter.ofPattern("h:mm")

private fun formatItemDate(day: ForecastDay): String {
    val dow = day.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US)
    val month = day.date.month.getDisplayName(TextStyle.SHORT, Locale.US)
    return "$dow, $month ${day.date.dayOfMonth}"
}

private fun buildDetailLine(day: ForecastDay): String {
    val sunset = "☼ ${day.sunset.format(shortTimeFormatter)}"
    val moonrise = "☽ ${day.moonrise.format(shortTimeFormatter)}"
    val azimuth = "${day.azimuthDegrees}° ${day.azimuthCardinal}"
    val weather = when (day.weather) {
        WeatherCondition.CLEAR -> "☀"
        WeatherCondition.PARTLY_CLOUDY -> "⛅"
        WeatherCondition.CLOUDY -> "☁"
        WeatherCondition.UNKNOWN -> "?"
    }
    return "$sunset  $moonrise  $azimuth  $weather"
}
