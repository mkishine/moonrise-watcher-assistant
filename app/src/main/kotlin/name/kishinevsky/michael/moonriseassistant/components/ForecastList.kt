package name.kishinevsky.michael.moonriseassistant.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import name.kishinevsky.michael.moonriseassistant.model.ForecastDay

@Composable
fun ForecastList(
    days: List<ForecastDay>,
    onDayClick: (ForecastDay) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
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
    }
}
