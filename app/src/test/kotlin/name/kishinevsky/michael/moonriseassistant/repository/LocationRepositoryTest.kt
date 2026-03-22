package name.kishinevsky.michael.moonriseassistant.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import name.kishinevsky.michael.moonriseassistant.model.SavedLocation
import name.kishinevsky.michael.moonriseassistant.storage.dao.LocationDao
import name.kishinevsky.michael.moonriseassistant.storage.entity.LocationEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LocationRepositoryTest {

    private lateinit var dao: FakeLocationDao
    private lateinit var repository: LocationRepository

    @BeforeEach
    fun setup() {
        dao = FakeLocationDao()
        repository = LocationRepository(dao)
    }

    @Test
    fun `getAllLocations returns all inserted locations in insert order`() = runTest {
        // Given
        repository.addLocation("Home", "Chicago, IL", 41.88, -87.63)
        repository.addLocation("Cabin", null, 45.00, -90.00)

        // When
        val locations = repository.getAllLocations().first()

        // Then
        assertThat(locations).hasSize(2)
        assertThat(locations[0].name).isEqualTo("Home")
        assertThat(locations[1].name).isEqualTo("Cabin")
    }

    @Test
    fun `setActive deactivates previous location and activates the target`() = runTest {
        // Given - addLocation activates each new location, so cabin ends up active
        val home = repository.addLocation("Home", "Chicago, IL", 41.88, -87.63)
        repository.addLocation("Cabin", null, 45.00, -90.00)

        // When
        repository.setActive(home.id)

        // Then
        val active = repository.getActiveLocation().first()
        assertThat(active?.id).isEqualTo(home.id)
        assertThat(active?.name).isEqualTo("Home")
    }

    @Test
    fun `deleteLocation removes location from getAllLocations`() = runTest {
        // Given
        val home = repository.addLocation("Home", "Chicago, IL", 41.88, -87.63)
        repository.addLocation("Cabin", null, 45.00, -90.00)
        repository.setActive(home.id)
        val cabin = repository.getAllLocations().first().first { it.name == "Cabin" }

        // When
        repository.deleteLocation(cabin.id)

        // Then
        val remaining = repository.getAllLocations().first()
        assertThat(remaining).hasSize(1)
        assertThat(remaining[0].name).isEqualTo("Home")
    }

    @Test
    fun `deleteLocation auto-activates first remaining location when active is deleted`() = runTest {
        // Given - home is added first, cabin is added second and becomes active
        repository.addLocation("Home", "Chicago, IL", 41.88, -87.63)
        val cabin = repository.addLocation("Cabin", null, 45.00, -90.00)

        // When
        repository.deleteLocation(cabin.id)

        // Then - home (first remaining) is now active
        val active = repository.getActiveLocation().first()
        assertThat(active?.name).isEqualTo("Home")
    }

    @Test
    fun `updateLocation changes name cityState lat and lng without creating a duplicate`() = runTest {
        // Given
        val original = repository.addLocation("Home", "Chicago, IL", 41.88, -87.63)
        val updated = SavedLocation(
            id = original.id,
            name = "My Spot",
            cityState = "Milwaukee, WI",
            latitude = 43.04,
            longitude = -87.91,
        )

        // When
        repository.updateLocation(updated)

        // Then
        val all = repository.getAllLocations().first()
        assertThat(all).hasSize(1)
        val result = all[0]
        assertThat(result.name).isEqualTo("My Spot")
        assertThat(result.cityState).isEqualTo("Milwaukee, WI")
        assertThat(result.latitude).isEqualTo(43.04)
        assertThat(result.longitude).isEqualTo(-87.91)
    }

    @Test
    fun `getLocationById returns the correct location when it exists`() = runTest {
        // Given
        val location = repository.addLocation("Home", "Chicago, IL", 41.88, -87.63)

        // When
        val result = repository.getLocationById(location.id)

        // Then
        assertThat(result).isNotNull
        assertThat(result?.id).isEqualTo(location.id)
        assertThat(result?.name).isEqualTo("Home")
    }

    @Test
    fun `getLocationById returns null when location does not exist`() = runTest {
        // When
        val result = repository.getLocationById("999")

        // Then
        assertThat(result).isNull()
    }
}

private class FakeLocationDao : LocationDao {

    private var nextId = 1L
    private val locations = mutableListOf<LocationEntity>()
    private val allFlow = MutableStateFlow<List<LocationEntity>>(emptyList())
    private val activeFlow = MutableStateFlow<LocationEntity?>(null)

    override suspend fun insert(location: LocationEntity): Long {
        val id = nextId++
        locations.add(location.copy(id = id))
        updateFlows()
        return id
    }

    override fun getAll(): Flow<List<LocationEntity>> = allFlow

    override fun getActive(): Flow<LocationEntity?> = activeFlow

    override suspend fun clearActive() {
        val updated = locations.map { it.copy(isActive = false) }
        locations.clear()
        locations.addAll(updated)
        updateFlows()
    }

    override suspend fun setActive(locationId: Long) {
        val updated = locations.map { it.copy(isActive = it.id == locationId) }
        locations.clear()
        locations.addAll(updated)
        updateFlows()
    }

    override suspend fun delete(locationId: Long) {
        locations.removeIf { it.id == locationId }
        updateFlows()
    }

    override suspend fun count(): Int = locations.size

    override suspend fun update(location: LocationEntity) {
        val idx = locations.indexOfFirst { it.id == location.id }
        if (idx >= 0) {
            locations[idx] = location
            updateFlows()
        }
    }

    override suspend fun getById(id: Long): LocationEntity? = locations.find { it.id == id }

    private fun updateFlows() {
        allFlow.value = locations.toList()
        activeFlow.value = locations.find { it.isActive }
    }
}
