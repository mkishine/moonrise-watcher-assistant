package name.kishinevsky.michael.moonriseassistant.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import name.kishinevsky.michael.moonriseassistant.model.SavedLocation
import name.kishinevsky.michael.moonriseassistant.storage.dao.LocationDao
import name.kishinevsky.michael.moonriseassistant.storage.entity.LocationEntity

open class LocationRepository(private val dao: LocationDao) {

    open fun getActiveLocation(): Flow<SavedLocation?> {
        return dao.getActive().map { it?.toSavedLocation() }
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

    open suspend fun getLocationCount(): Int {
        return dao.count()
    }

    open fun getAllLocations(): Flow<List<SavedLocation>> {
        return dao.getAll().map { list -> list.map { it.toSavedLocation() } }
    }

    open suspend fun setActive(locationId: String) {
        dao.clearActive()
        dao.setActive(locationId.toLong())
    }

    open suspend fun deleteLocation(locationId: String) {
        val id = locationId.toLong()
        val toDelete = dao.getById(id) ?: return
        dao.delete(id)
        if (toDelete.isActive) {
            dao.getAll().first().firstOrNull()?.let { first ->
                dao.setActive(first.id)
            }
        }
    }

    open suspend fun updateLocation(location: SavedLocation) {
        val existing = dao.getById(location.id.toLong()) ?: return
        dao.update(
            existing.copy(
                name = location.name,
                cityState = location.cityState,
                latitude = location.latitude,
                longitude = location.longitude,
            )
        )
    }

    open suspend fun getLocationById(locationId: String): SavedLocation? {
        return dao.getById(locationId.toLong())?.toSavedLocation()
    }

    private fun LocationEntity.toSavedLocation() = SavedLocation(
        id = id.toString(),
        name = name,
        cityState = cityState,
        latitude = latitude,
        longitude = longitude,
    )
}
