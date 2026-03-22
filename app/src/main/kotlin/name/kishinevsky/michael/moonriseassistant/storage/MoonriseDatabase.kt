package name.kishinevsky.michael.moonriseassistant.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import name.kishinevsky.michael.moonriseassistant.storage.dao.LocationDao
import name.kishinevsky.michael.moonriseassistant.storage.dao.SettingsDao
import name.kishinevsky.michael.moonriseassistant.storage.dao.WeatherCacheDao
import name.kishinevsky.michael.moonriseassistant.storage.entity.LocationEntity
import name.kishinevsky.michael.moonriseassistant.storage.entity.SettingsEntity
import name.kishinevsky.michael.moonriseassistant.storage.entity.WeatherCacheEntity

@Database(
    entities = [LocationEntity::class, SettingsEntity::class, WeatherCacheEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class MoonriseDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun settingsDao(): SettingsDao
    abstract fun weatherCacheDao(): WeatherCacheDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // weather_cache is a pure cache — safe to drop and recreate with the new schema
                db.execSQL("DROP TABLE IF EXISTS `weather_cache`")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `weather_cache` (
                        `locationId` INTEGER NOT NULL,
                        `date` TEXT NOT NULL,
                        `jsonBlob` TEXT NOT NULL,
                        `fetchedAt` INTEGER NOT NULL,
                        `timezone` TEXT NOT NULL,
                        PRIMARY KEY(`locationId`, `date`)
                    )
                    """.trimIndent()
                )
            }
        }
    }
}
