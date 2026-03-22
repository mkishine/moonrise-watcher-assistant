package name.kishinevsky.michael.moonriseassistant.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import name.kishinevsky.michael.moonriseassistant.location.Geocoding
import name.kishinevsky.michael.moonriseassistant.location.GeocodingResult
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

    private fun createVm(
        repo: FakeLocationRepository = FakeLocationRepository(),
        geocoding: Geocoding = FakeGeocoding(),
    ): Pair<AddLocationViewModel, FakeLocationRepository> {
        val vm = AddLocationViewModel(repo, geocoding)
        return vm to repo
    }

    // ── saveLocation tests ───────────────────────────────────

    @Test
    fun `initial state is Idle`() = runTest(testDispatcher) {
        // Given / When
        val (vm, _) = createVm()

        // Then
        assertThat(vm.uiState.value).isEqualTo(AddLocationUiState.Idle)
    }

    @Test
    fun `emits Success after saving valid location`() = runTest(testDispatcher) {
        // Given
        val repo = FakeLocationRepository()
        val (vm, _) = createVm(repo = repo)

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
        val (vm, _) = createVm()

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
        val (vm, _) = createVm()

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
        val (vm, _) = createVm()

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
        val (vm, _) = createVm()

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
        val (vm, _) = createVm()
        vm.saveLocation("Seattle", "WA", 47.6, -122.3)
        advanceUntilIdle()
        assertThat(vm.uiState.value).isEqualTo(AddLocationUiState.Success)

        // When
        vm.resetState()

        // Then
        assertThat(vm.uiState.value).isEqualTo(AddLocationUiState.Idle)
    }

    // ── resolveAndSaveLocation tests ─────────────────────────

    @Test
    fun `resolveAndSaveLocation geocodes and saves on success`() = runTest(testDispatcher) {
        // Given
        val repo = FakeLocationRepository()
        val geocoding = FakeGeocoding(
            result = GeocodingResult.Success(42.84, -71.74, "Wilton, NH"),
        )
        val (vm, _) = createVm(repo = repo, geocoding = geocoding)

        // When
        vm.resolveAndSaveLocation("Wilton, NH", "")
        advanceUntilIdle()

        // Then
        assertThat(vm.uiState.value).isEqualTo(AddLocationUiState.Success)
        assertThat(repo.savedLocations).hasSize(1)
        val saved = repo.savedLocations.first()
        assertThat(saved.name).isEqualTo("Wilton, NH")
        assertThat(saved.latitude).isEqualTo(42.84)
        assertThat(saved.longitude).isEqualTo(-71.74)
        assertThat(saved.cityState).isEqualTo("Wilton, NH")
    }

    @Test
    fun `resolveAndSaveLocation uses custom name when provided`() = runTest(testDispatcher) {
        // Given
        val repo = FakeLocationRepository()
        val geocoding = FakeGeocoding(
            result = GeocodingResult.Success(42.84, -71.74, "Wilton, NH"),
        )
        val (vm, _) = createVm(repo = repo, geocoding = geocoding)

        // When
        vm.resolveAndSaveLocation("Wilton, NH", "Home")
        advanceUntilIdle()

        // Then
        assertThat(vm.uiState.value).isEqualTo(AddLocationUiState.Success)
        assertThat(repo.savedLocations.first().name).isEqualTo("Home")
    }

    @Test
    fun `resolveAndSaveLocation emits Error when not found`() = runTest(testDispatcher) {
        // Given
        val geocoding = FakeGeocoding(result = GeocodingResult.NotFound)
        val (vm, _) = createVm(geocoding = geocoding)

        // When
        vm.resolveAndSaveLocation("Nonexistent Place", "")
        advanceUntilIdle()

        // Then
        val state = vm.uiState.value
        assertThat(state).isInstanceOf(AddLocationUiState.Error::class.java)
        assertThat((state as AddLocationUiState.Error).message)
            .contains("Could not find location")
            .contains("Nonexistent Place")
    }

    @Test
    fun `resolveAndSaveLocation emits Error on geocoding failure`() = runTest(testDispatcher) {
        // Given
        val geocoding = FakeGeocoding(
            result = GeocodingResult.Error("Network error"),
        )
        val (vm, _) = createVm(geocoding = geocoding)

        // When
        vm.resolveAndSaveLocation("Wilton, NH", "")
        advanceUntilIdle()

        // Then
        val state = vm.uiState.value
        assertThat(state).isInstanceOf(AddLocationUiState.Error::class.java)
        assertThat((state as AddLocationUiState.Error).message).isEqualTo("Network error")
    }

    @Test
    fun `resolveAndSaveLocation emits Error for blank city query`() = runTest(testDispatcher) {
        // Given
        val (vm, _) = createVm()

        // When
        vm.resolveAndSaveLocation("  ", "")

        // Then — synchronous validation, no need to advance
        val state = vm.uiState.value
        assertThat(state).isInstanceOf(AddLocationUiState.Error::class.java)
        assertThat((state as AddLocationUiState.Error).message).contains("City name")
    }

    // ── editLocation tests ───────────────────────────────────

    @Test
    fun `editLocation calls updateLocation not addLocation`() = runTest(testDispatcher) {
        // Given
        val repo = FakeLocationRepository()
        val original = SavedLocation("1", "Home", "Chicago, IL", 41.88, -87.63)
        val (vm, _) = createVm(repo = repo)

        // When
        vm.editLocation(original, "My Spot", "Milwaukee, WI", 43.04, -87.91)
        advanceUntilIdle()

        // Then
        assertThat(vm.uiState.value).isEqualTo(AddLocationUiState.Success)
        assertThat(repo.updatedLocations).hasSize(1)
        assertThat(repo.savedLocations).isEmpty()
    }

    @Test
    fun `editLocation emits Success after successful update`() = runTest(testDispatcher) {
        // Given
        val original = SavedLocation("1", "Home", "Chicago, IL", 41.88, -87.63)
        val (vm, _) = createVm()

        // When
        vm.editLocation(original, "My Spot", null, 43.04, -87.91)
        advanceUntilIdle()

        // Then
        assertThat(vm.uiState.value).isEqualTo(AddLocationUiState.Success)
    }

    @Test
    fun `editLocation emits Error with invalid coordinates`() = runTest(testDispatcher) {
        // Given
        val original = SavedLocation("1", "Home", "Chicago, IL", 41.88, -87.63)
        val (vm, _) = createVm()

        // When
        vm.editLocation(original, "Home", null, 999.0, -87.63)

        // Then — synchronous validation
        val state = vm.uiState.value
        assertThat(state).isInstanceOf(AddLocationUiState.Error::class.java)
        assertThat((state as AddLocationUiState.Error).message).contains("Latitude")
    }

    @Test
    fun `editLocationByCityQuery geocodes and updates the existing location`() = runTest(testDispatcher) {
        // Given
        val repo = FakeLocationRepository()
        val geocoding = FakeGeocoding(
            result = GeocodingResult.Success(43.04, -87.91, "Milwaukee, WI"),
        )
        val original = SavedLocation("1", "Home", "Chicago, IL", 41.88, -87.63)
        val (vm, _) = createVm(repo = repo, geocoding = geocoding)

        // When
        vm.editLocationByCityQuery(original, "Milwaukee, WI", "")
        advanceUntilIdle()

        // Then
        assertThat(vm.uiState.value).isEqualTo(AddLocationUiState.Success)
        assertThat(repo.updatedLocations).hasSize(1)
        assertThat(repo.updatedLocations.first().cityState).isEqualTo("Milwaukee, WI")
        assertThat(repo.savedLocations).isEmpty()
    }

    // ── Fakes ────────────────────────────────────────────────

    private class FakeLocationRepository : LocationRepository(StubLocationDao()) {
        val savedLocations = mutableListOf<SavedLocation>()
        val updatedLocations = mutableListOf<SavedLocation>()

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

        override suspend fun updateLocation(location: SavedLocation) {
            updatedLocations.add(location)
        }
    }

    private class FakeGeocoding(
        private val result: GeocodingResult = GeocodingResult.NotFound,
    ) : Geocoding {
        override suspend fun geocode(query: String): GeocodingResult = result
    }
}
