package name.kishinevsky.michael.moonriseassistant.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import name.kishinevsky.michael.moonriseassistant.storage.dao.LocationDao
import name.kishinevsky.michael.moonriseassistant.storage.dao.SettingsDao
import name.kishinevsky.michael.moonriseassistant.storage.dao.WeatherCacheDao
import name.kishinevsky.michael.moonriseassistant.storage.entity.LocationEntity
import name.kishinevsky.michael.moonriseassistant.storage.entity.SettingsEntity
import name.kishinevsky.michael.moonriseassistant.storage.entity.WeatherCacheEntity

@Database(
    entities = [LocationEntity::class, SettingsEntity::class, WeatherCacheEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class MoonriseDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun settingsDao(): SettingsDao
    abstract fun weatherCacheDao(): WeatherCacheDao
}
