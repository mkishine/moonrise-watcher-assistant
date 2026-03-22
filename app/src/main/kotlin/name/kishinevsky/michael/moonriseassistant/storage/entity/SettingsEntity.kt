package name.kishinevsky.michael.moonriseassistant.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey val id: Int = 1,
    val daysBeforeFullMoon: Int = 2,
    val daysAfterFullMoon: Int = 5,
    val forecastPeriodMonths: Int = 3,
    val maxMoonriseHour: Int = 23,
    val maxMoonriseMinute: Int = 0,
    val beforeSunsetToleranceMin: Int = 30,
    val useMetric: Boolean = false,
)
