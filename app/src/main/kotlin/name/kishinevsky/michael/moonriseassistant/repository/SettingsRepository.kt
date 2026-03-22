package name.kishinevsky.michael.moonriseassistant.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import name.kishinevsky.michael.moonriseassistant.model.AppSettings
import name.kishinevsky.michael.moonriseassistant.storage.dao.SettingsDao
import name.kishinevsky.michael.moonriseassistant.storage.entity.SettingsEntity
import java.time.LocalTime

open class SettingsRepository(private val dao: SettingsDao) {

    open fun getSettings(): Flow<AppSettings> {
        return dao.getSettings().map { entity ->
            entity?.toAppSettings() ?: AppSettings()
        }
    }

    open suspend fun updateSettings(settings: AppSettings) {
        dao.insertOrUpdate(settings.toEntity())
    }

    private fun SettingsEntity.toAppSettings() = AppSettings(
        daysBeforeFullMoon = daysBeforeFullMoon,
        daysAfterFullMoon = daysAfterFullMoon,
        forecastPeriodMonths = forecastPeriodMonths,
        maxMoonriseTime = LocalTime.of(maxMoonriseHour, maxMoonriseMinute),
        beforeSunsetToleranceMin = beforeSunsetToleranceMin,
        useMetric = useMetric,
    )

    private fun AppSettings.toEntity() = SettingsEntity(
        daysBeforeFullMoon = daysBeforeFullMoon,
        daysAfterFullMoon = daysAfterFullMoon,
        forecastPeriodMonths = forecastPeriodMonths,
        maxMoonriseHour = maxMoonriseTime.hour,
        maxMoonriseMinute = maxMoonriseTime.minute,
        beforeSunsetToleranceMin = beforeSunsetToleranceMin,
        useMetric = useMetric,
    )
}
