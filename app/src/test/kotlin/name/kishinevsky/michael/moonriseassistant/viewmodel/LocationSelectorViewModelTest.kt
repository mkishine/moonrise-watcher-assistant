package name.kishinevsky.michael.moonriseassistant.viewmodel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import name.kishinevsky.michael.moonriseassistant.model.SavedLocation
import name.kishinevsky.michael.moonriseassistant.repository.LocationRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LocationSelectorViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var locationRepository: FakeLocationSelectorRepository
    private lateinit var viewModel: LocationSelectorViewModel

    @BeforeEach
    fun setup() {
        kotlinx.coroutines.Dispatchers.setMain(testDispatcher)
        locationRepository = FakeLocationSelectorRepository()
        viewModel = LocationSelectorViewModel(locationRepository)
    }

    @AfterEach
    fun tearDown() {
        kotlinx.coroutines.Dispatchers.resetMain()
    }

    @Test
    fun `emits Content with all locations and active ID on init`() = runTest {
        // Given
        val home = savedLocation("1", "Home")
        val cabin = savedLocation("2", "Cabin")
        locationRepository.setLocations(listOf(home, cabin))
        locationRepository.setActive(home)

        // When
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value as LocationSelectorUiState.Content
        assertThat(state.locations).containsExactly(home, cabin)
        assertThat(state.activeLocationId).isEqualTo("1")
    }

    @Test
    fun `selectLocation delegates to repository setActive`() = runTest {
        // Given
        val home = savedLocation("1", "Home")
        locationRepository.setLocations(listOf(home))
        locationRepository.setActive(home)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.selectLocation(home)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertThat(locationRepository.setActiveCalls).containsExactly("1")
    }

    @Test
    fun `deleteLocation delegates to repository deleteLocation`() = runTest {
        // Given
        val cabin = savedLocation("2", "Cabin")
        locationRepository.setLocations(listOf(cabin))
        locationRepository.setActive(cabin)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.deleteLocation(cabin)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertThat(locationRepository.deleteLocationCalls).containsExactly("2")
    }

    @Test
    fun `emits Loading when no active location is present`() = runTest {
        // Given - no active location set
        locationRepository.setLocations(listOf(savedLocation("1", "Home")))
        locationRepository.setActive(null)

        // When
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value).isInstanceOf(LocationSelectorUiState.Loading::class.java)
    }

    private fun savedLocation(id: String, name: String) = SavedLocation(
        id = id,
        name = name,
        cityState = null,
        latitude = 41.88,
        longitude = -87.63,
    )
}

private class FakeLocationSelectorRepository : LocationRepository(StubLocationDao()) {

    private val locationsFlow = MutableStateFlow<List<SavedLocation>>(emptyList())
    private val activeFlow = MutableStateFlow<SavedLocation?>(null)

    val setActiveCalls = mutableListOf<String>()
    val deleteLocationCalls = mutableListOf<String>()

    fun setLocations(locations: List<SavedLocation>) {
        locationsFlow.value = locations
    }

    fun setActive(location: SavedLocation?) {
        activeFlow.value = location
    }

    override fun getAllLocations(): Flow<List<SavedLocation>> = locationsFlow

    override fun getActiveLocation(): Flow<SavedLocation?> = activeFlow

    override suspend fun setActive(locationId: String) {
        setActiveCalls.add(locationId)
    }

    override suspend fun deleteLocation(locationId: String) {
        deleteLocationCalls.add(locationId)
    }
}
