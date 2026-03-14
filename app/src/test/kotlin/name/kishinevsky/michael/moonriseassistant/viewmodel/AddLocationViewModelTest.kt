package name.kishinevsky.michael.moonriseassistant.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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
class AddLocationViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Idle`() = runTest(testDispatcher) {
        // Given / When
        val vm = AddLocationViewModel(FakeLocationRepository())

        // Then
        assertThat(vm.uiState.value).isEqualTo(AddLocationUiState.Idle)
    }

    @Test
    fun `emits Success after saving valid location`() = runTest(testDispatcher) {
        // Given
        val repo = FakeLocationRepository()
        val vm = AddLocationViewModel(repo)

        // When
        vm.saveLocation("Seattle", "WA", 47.6, -122.3)
        advanceUntilIdle()

        // Then
        assertThat(vm.uiState.value).isEqualTo(AddLocationUiState.Success)
        assertThat(repo.savedLocations).hasSize(1)
        assertThat(repo.savedLocations.first().name).isEqualTo("Seattle")
    }

    @Test
    fun `emits Error for blank name`() = runTest(testDispatcher) {
        // Given
        val vm = AddLocationViewModel(FakeLocationRepository())

        // When
        vm.saveLocation("  ", null, 47.6, -122.3)

        // Then — synchronous validation, no need to advance
        val state = vm.uiState.value
        assertThat(state).isInstanceOf(AddLocationUiState.Error::class.java)
        assertThat((state as AddLocationUiState.Error).message).contains("name")
    }

    @Test
    fun `emits Error for invalid latitude`() = runTest(testDispatcher) {
        // Given
        val vm = AddLocationViewModel(FakeLocationRepository())

        // When
        vm.saveLocation("Test", null, 91.0, -122.3)

        // Then
        val state = vm.uiState.value
        assertThat(state).isInstanceOf(AddLocationUiState.Error::class.java)
        assertThat((state as AddLocationUiState.Error).message).contains("Latitude")
    }

    @Test
    fun `emits Error for invalid longitude`() = runTest(testDispatcher) {
        // Given
        val vm = AddLocationViewModel(FakeLocationRepository())

        // When
        vm.saveLocation("Test", null, 47.6, 181.0)

        // Then
        val state = vm.uiState.value
        assertThat(state).isInstanceOf(AddLocationUiState.Error::class.java)
        assertThat((state as AddLocationUiState.Error).message).contains("Longitude")
    }

    @Test
    fun `emits Error for NaN latitude`() = runTest(testDispatcher) {
        // Given
        val vm = AddLocationViewModel(FakeLocationRepository())

        // When
        vm.saveLocation("Test", null, Double.NaN, -122.3)

        // Then
        val state = vm.uiState.value
        assertThat(state).isInstanceOf(AddLocationUiState.Error::class.java)
        assertThat((state as AddLocationUiState.Error).message).contains("Latitude")
    }

    @Test
    fun `resetState returns to Idle`() = runTest(testDispatcher) {
        // Given
        val vm = AddLocationViewModel(FakeLocationRepository())
        vm.saveLocation("Seattle", "WA", 47.6, -122.3)
        advanceUntilIdle()
        assertThat(vm.uiState.value).isEqualTo(AddLocationUiState.Success)

        // When
        vm.resetState()

        // Then
        assertThat(vm.uiState.value).isEqualTo(AddLocationUiState.Idle)
    }

    // ── Fakes ────────────────────────────────────────────────

    private class FakeLocationRepository : LocationRepository(StubLocationDao()) {
        val savedLocations = mutableListOf<SavedLocation>()

        override suspend fun addLocation(
            name: String,
            cityState: String?,
            lat: Double,
            lng: Double,
        ): SavedLocation {
            val location = SavedLocation(
                id = (savedLocations.size + 1).toString(),
                name = name,
                cityState = cityState,
                latitude = lat,
                longitude = lng,
            )
            savedLocations.add(location)
            return location
        }
    }
}
