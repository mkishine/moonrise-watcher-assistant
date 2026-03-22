package name.kishinevsky.michael.moonriseassistant.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import name.kishinevsky.michael.moonriseassistant.model.ForecastDay
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastList(
    days: List<ForecastDay>,
    onDayClick: (ForecastDay) -> Unit,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    lastUpdated: Instant? = null,
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier,
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Text(
                    text = "UPCOMING",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                )
            }
            items(days, key = { it.date.toEpochDay() }) { day ->
                ForecastListItem(
                    day = day,
                    onClick = { onDayClick(day) },
                )
            }
            if (lastUpdated != null) {
                item {
                    Text(
                        text = "Updated ${formatUpdatedTime(lastUpdated)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    )
                }
            }
        }
    }
}

private val updatedTimeFormatter = DateTimeFormatter.ofPattern("h:mm a")

private fun formatUpdatedTime(instant: Instant): String =
    instant.atZone(ZoneId.systemDefault()).toLocalTime().format(updatedTimeFormatter)
