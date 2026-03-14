package name.kishinevsky.michael.moonriseassistant.storage.entity

import androidx.room.Entity

@Entity(
    tableName = "weather_cache",
    primaryKeys = ["locationId", "date"],
)
data class WeatherCacheEntity(
    val locationId: Long,
    val date: String,
    val jsonBlob: String,
    val fetchedAt: Long,
)
