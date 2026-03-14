package name.kishinevsky.michael.moonriseassistant.viewmodel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import name.kishinevsky.michael.moonriseassistant.storage.dao.LocationDao
import name.kishinevsky.michael.moonriseassistant.storage.dao.SettingsDao
import name.kishinevsky.michael.moonriseassistant.storage.dao.WeatherCacheDao
import name.kishinevsky.michael.moonriseassistant.storage.entity.LocationEntity
import name.kishinevsky.michael.moonriseassistant.storage.entity.SettingsEntity
import name.kishinevsky.michael.moonriseassistant.storage.entity.WeatherCacheEntity

/**
 * Stub DAOs that satisfy constructor requirements for open repository classes.
 * All methods throw — fakes override every repository method so these are never called.
 */

class StubLocationDao : LocationDao {
    override suspend fun insert(location: LocationEntity): Long = error("stub")
    override fun getAll(): Flow<List<LocationEntity>> = flowOf(emptyList())
    override fun getActive(): Flow<LocationEntity?> = flowOf(null)
    override suspend fun clearActive() = error("stub")
    override suspend fun setActive(locationId: Long) = error("stub")
    override suspend fun delete(locationId: Long) = error("stub")
    override suspend fun count(): Int = error("stub")
}

class StubSettingsDao : SettingsDao {
    override fun getSettings(): Flow<SettingsEntity?> = flowOf(null)
    override suspend fun insertOrUpdate(settings: SettingsEntity) = error("stub")
}

class StubWeatherCacheDao : WeatherCacheDao {
    override suspend fun insertAll(entries: List<WeatherCacheEntity>) = error("stub")
    override suspend fun getForLocation(locationId: Long): List<WeatherCacheEntity> = error("stub")
    override suspend fun deleteStaleEntries(cutoffTimestamp: Long) = error("stub")
    override suspend fun deleteForLocation(locationId: Long) = error("stub")
}
