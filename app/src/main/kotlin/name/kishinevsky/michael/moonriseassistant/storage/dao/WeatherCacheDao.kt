@file:Suppress("EmptyMethod", "EmptyMethod", "EmptyMethod")

package name.kishinevsky.michael.moonriseassistant.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import name.kishinevsky.michael.moonriseassistant.storage.entity.WeatherCacheEntity

@Suppress("EmptyMethod")
@Dao
interface WeatherCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<WeatherCacheEntity>)

    @Query("SELECT * FROM weather_cache WHERE locationId = :locationId ORDER BY date")
    suspend fun getForLocation(locationId: Long): List<WeatherCacheEntity>

    @Query("DELETE FROM weather_cache WHERE fetchedAt < :cutoffTimestamp")
    suspend fun deleteStaleEntries(cutoffTimestamp: Long)

    @Query("DELETE FROM weather_cache WHERE locationId = :locationId")
    suspend fun deleteForLocation(locationId: Long)
}
