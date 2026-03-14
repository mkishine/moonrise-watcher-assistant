package name.kishinevsky.michael.moonriseassistant.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import name.kishinevsky.michael.moonriseassistant.model.SavedLocation
import name.kishinevsky.michael.moonriseassistant.storage.dao.LocationDao
import name.kishinevsky.michael.moonriseassistant.storage.entity.LocationEntity

open class LocationRepository(private val dao: LocationDao) {

    open fun getActiveLocation(): Flow<SavedLocation?> {
        return dao.getActive().map { it?.toSavedLocation() }
    }

    fun getAllLocations(): Flow<List<SavedLocation>> {
        return dao.getAll().map { entities -> entities.map { it.toSavedLocation() } }
    }

    open suspend fun addLocation(
        name: String,
        cityState: String?,
        lat: Double,
        lng: Double,
    ): SavedLocation {
        val entity = LocationEntity(
            name = name,
            cityState = cityState,
            latitude = lat,
            longitude = lng,
            isActive = true,
        )
        dao.clearActive()
        val id = dao.insert(entity)
        return SavedLocation(
            id = id.toString(),
            name = name,
            cityState = cityState,
            latitude = lat,
            longitude = lng,
        )
    }

    suspend fun setActive(locationId: Long) {
        dao.clearActive()
        dao.setActive(locationId)
    }

    open suspend fun getLocationCount(): Int {
        return dao.count()
    }

    private fun LocationEntity.toSavedLocation() = SavedLocation(
        id = id.toString(),
        name = name,
        cityState = cityState,
        latitude = latitude,
        longitude = longitude,
    )
}
