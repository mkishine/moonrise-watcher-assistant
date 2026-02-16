package name.kishinevsky.michael.moonriseassistant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import name.kishinevsky.michael.moonriseassistant.model.ForecastDay
import name.kishinevsky.michael.moonriseassistant.model.Verdict
import name.kishinevsky.michael.moonriseassistant.model.WeatherCondition
import name.kishinevsky.michael.moonriseassistant.ui.theme.BadRed
import name.kishinevsky.michael.moonriseassistant.ui.theme.BadRedLight
import name.kishinevsky.michael.moonriseassistant.ui.theme.GoodGreen
import name.kishinevsky.michael.moonriseassistant.ui.theme.GoodGreenLight
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

/**
 * Content for the detail view bottom sheet. Does not include the ModalBottomSheet
 * wrapper itself, so it can be previewed standalone.
 */
@Composable
fun DetailSheetContent(
    day: ForecastDay,
    maxMoonriseTime: LocalTime = LocalTime.of(23, 0),
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(horizontal = 24.dp)) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header: date + verdict badge
        DetailHeader(day)

        Spacer(modifier = Modifier.height(20.dp))

        // Astronomical section
        SectionHeader("ASTRONOMICAL")
        DetailDataRow("Sunset", formatFullTime(day.sunset))
        DetailDataRow("Moonrise", formatFullTime(day.moonrise))
        DetailDataRow(
            "Azimuth",
            "${day.azimuthDegrees}° ${day.azimuthCardinal}",
        )
        if (day.azimuthCardinalExpanded != null) {
            Text(
                text = "(${day.azimuthCardinalExpanded})",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 88.dp),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Weather section
        SectionHeader("WEATHER")
        if (day.weather == WeatherCondition.UNKNOWN) {
            WeatherUnknownMessage()
        } else {
            DetailDataRow("Sky", weatherIconLabel(day.weather))
            if (day.cloudCoverPercent != null) {
                Text(
                    text = "Cloud cover ${day.cloudCoverPercent}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 88.dp),
                )
            }
            DetailDataRow("Temp", formatDetailTemp(day.temperatureF, day.windchillF))
            DetailDataRow("Wind", formatDetailWind(day.windSpeedMph, day.windDirection))
            DetailDataRow("Precip", formatPrecipitation(day.precipitationPercent, day.precipitationType))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Verdict section
        SectionHeader("VERDICT")
        ConstraintRow(pass = true, "Moon in phase window")
        ConstraintRow(
            pass = day.moonrise.isAfter(day.sunset.minusMinutes(30)),
            label = if (day.moonrise.isAfter(day.sunset.minusMinutes(30))) {
                "Moonrise after sunset"
            } else {
                "Moonrise before sunset"
            },
        )
        ConstraintRow(
            pass = day.moonrise.isBefore(maxMoonriseTime),
            label = if (day.moonrise.isBefore(maxMoonriseTime)) {
                "Moonrise before ${formatFullTime(maxMoonriseTime)}"
            } else {
                "Moonrise after ${formatFullTime(maxMoonriseTime)}"
            },
        )
        when (day.weather) {
            WeatherCondition.UNKNOWN -> ConstraintRowUnknown("Sky clarity unknown")
            WeatherCondition.CLEAR -> ConstraintRow(pass = true, "Sky clear")
            WeatherCondition.PARTLY_CLOUDY -> ConstraintRow(pass = true, "Sky mostly clear")
            WeatherCondition.CLOUDY -> ConstraintRow(pass = false, "Sky cloudy")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Swipe hint
        Text(
            text = "\u25C2 swipe between days \u25B8",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
        )
    }
}

@Composable
private fun DetailHeader(day: ForecastDay) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = formatFullDate(day),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
            )
            if (day.weather == WeatherCondition.UNKNOWN && day.verdict == Verdict.GOOD) {
                Text(
                    text = "(weather TBD)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        DetailVerdictBadge(day.verdict, day.verdictReason)
    }
}

@Composable
private fun DetailVerdictBadge(verdict: Verdict, reason: String?) {
    val (bg, fg, label) = when (verdict) {
        Verdict.GOOD -> Triple(GoodGreenLight, GoodGreen, "\u25CF GOOD")
        Verdict.BAD -> Triple(BadRedLight, BadRed, "\u25CB BAD")
    }
    Column(horizontalAlignment = Alignment.End) {
        Text(
            text = label,
            color = fg,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(bg, RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
        )
        if (verdict == Verdict.BAD && reason != null) {
            Text(
                text = "($reason)",
                style = MaterialTheme.typography.bodySmall,
                color = fg,
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))
}

@Composable
private fun DetailDataRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .padding(end = 12.dp)
                .defaultMinSize(minWidth = 72.dp),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun ConstraintRow(pass: Boolean, label: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = if (pass) "\u2713" else "\u2717",
            color = if (pass) GoodGreen else MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 8.dp),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = if (pass) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.error
            },
        )
    }
}

@Composable
private fun ConstraintRowUnknown(label: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "?",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 8.dp),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun WeatherUnknownMessage() {
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "Weather forecast unavailable\nfor this date.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    )
    Spacer(modifier = Modifier.height(8.dp))
}

// ── Formatters ──────────────────────────────────────────────────────

private val fullTimeFormatter = DateTimeFormatter.ofPattern("h:mm a")

private fun formatFullTime(time: LocalTime): String = time.format(fullTimeFormatter)

private fun formatFullDate(day: ForecastDay): String {
    val dow = day.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US)
    val month = day.date.month.getDisplayName(TextStyle.SHORT, Locale.US)
    return "$dow, $month ${day.date.dayOfMonth}, ${day.date.year}"
}

private fun weatherIconLabel(condition: WeatherCondition): String = when (condition) {
    WeatherCondition.CLEAR -> "\u2600 Clear"
    WeatherCondition.PARTLY_CLOUDY -> "\u26C5 Partly Cloudy"
    WeatherCondition.CLOUDY -> "\u2601 Cloudy"
    WeatherCondition.UNKNOWN -> "? Weather unknown"
}

private fun formatDetailTemp(temperatureF: Int?, windchillF: Int?): String {
    if (temperatureF == null) return "\u2014"
    return if (windchillF != null && windchillF != temperatureF) {
        "${temperatureF}\u00B0F  Feels ${windchillF}\u00B0F"
    } else {
        "${temperatureF}\u00B0F"
    }
}

private fun formatDetailWind(speedMph: Int?, direction: String?): String {
    if (speedMph == null) return "\u2014"
    return if (direction != null) "$speedMph mph $direction" else "$speedMph mph"
}

private fun formatPrecipitation(percent: Int?, type: String?): String {
    if (percent == null) return "\u2014"
    return if (type != null) "$percent% chance $type" else "$percent% chance"
}
