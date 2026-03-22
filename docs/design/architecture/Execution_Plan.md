# Execution Plan

**Date:** March 21, 2026

This document sequences the build into incremental, testable steps. Each step produces working,
tested code before the next step begins.

---

## Phase 1 Summary (Complete)

All 11 MVP steps complete. 48 unit tests + 9 Compose test files. End-to-end flow functional.

| ID     | Title                         | Priority    | Status |
|--------|-------------------------------|-------------|--------|
| US-001 | Add First Location            | Must Have   | ✅ Done |
| US-003 | Check Tonight's Conditions    | Must Have   | ✅ Done |
| US-004 | See Upcoming Good Nights      | Must Have   | ✅ Done |
| US-005 | View Detailed Weather         | Must Have   | ✅ Done |
| US-006 | Know Where to Look            | Must Have   | ✅ Done |
| US-013 | Set Moonrise Time Constraints | Must Have   | ✅ Done |
| US-007 | Understand Why a Night is Bad | Should Have | ✅ Done |
| US-008 | Refresh Forecast Data         | Should Have | ✅ Done |

---

## Phase 2

**Goal:** Multi-location support, location management, and informational screens.

### User Story Progress

| ID     | Title                       | Priority    | Status    |
|--------|-----------------------------|-------------|-----------|
| US-009 | Add Additional Location     | Must Have   | ⬜ Pending |
| US-010 | Switch Between Locations    | Must Have   | ⬜ Pending |
| US-011 | Edit Location Details       | Should Have | ⬜ Pending |
| US-012 | Delete Location             | Should Have | ⬜ Pending |
| US-014 | View About/Help Information | Should Have | ⬜ Pending |
| US-002 | Understand App Purpose      | Should Have | ⬜ Pending |

### Scaffolding Already in Place

The following Phase 2 components were built during Phase 1 and are waiting to be wired up:

| Component                 | Status                                                                |
|---------------------------|-----------------------------------------------------------------------|
| `LocationSelectorContent` | Complete UI: list, context menu, delete dialog                        |
| `LocationDao`             | Has `getAll`, `insert`, `clearActive`, `setActive`, `delete`, `count` |
| `LocationRepository`      | Has `addLocation`, `getActiveLocation`, `getLocationCount`            |
| `AddLocationScreen`       | Supports `FIRST_TIME` and `ADDITIONAL` contexts                       |
| `AddLocationViewModel`    | Saves new locations; needs edit mode                                  |
| Navigation route          | `addLocation/{isFirstTime}` already wired                             |

---

## Implementation Steps

---

## [ ] Step 1: Extend LocationRepository for Multi-Location Support

**Goal:** Add the missing data-layer methods that Phase 2 features depend on.

**User stories:** US-009, US-010, US-011, US-012

### What to build

New `LocationDao` methods:

- `@Update update(location: LocationEntity)` — for editing name/coordinates
-
`@Query("SELECT * FROM locations WHERE id = :id LIMIT 1") suspend fun getById(id: Long): LocationEntity?`

New `LocationRepository` methods:

- `getAllLocations(): Flow<List<SavedLocation>>` — ordered by `id`; used by
  LocationSelectorViewModel
- `setActive(locationId: String)` — clears existing active flag, sets new one; used by US-010
- `deleteLocation(locationId: String)` — removes a location by ID; used by US-012
- `updateLocation(location: SavedLocation)` — updates name, cityState, lat, lng; used by US-011
- `getLocationById(locationId: String): SavedLocation?` — loads a single location for edit pre-fill

### Files to modify

- `app/src/main/kotlin/.../storage/dao/LocationDao.kt` — add `update()` and `getById()`
- `app/src/main/kotlin/.../repository/LocationRepository.kt` — add five new methods above
- `app/src/test/kotlin/.../viewmodel/StubDaos.kt` — add stubs for new DAO methods

### Files to create

- `app/src/test/kotlin/.../repository/LocationRepositoryTest.kt`

### Tests to write

- `getAllLocations()` returns a flow containing all inserted locations in insert order
- `setActive()` deactivates the previously active location and activates the target
- `deleteLocation()` removes the location; subsequent `getAllLocations()` does not include it
- `updateLocation()` changes name, cityState, lat, and lng without creating a duplicate entry
- `getLocationById()` returns the correct location when it exists, null otherwise

### Verification

- [ ] `scripts/run.sh repo-tests ./gradlew testDebugUnitTest --tests "*.repository.*Test"` passes

---

## [ ] Step 2: LocationSelectorViewModel

**Goal:** State management for the location selector bottom sheet.

**User stories:** US-010 (switch), US-012 (delete)

### What to build

```kotlin
sealed interface LocationSelectorUiState {
    data object Loading : LocationSelectorUiState
    data class Content(
        val locations: List<SavedLocation>,
        val activeLocationId: String,
    ) : LocationSelectorUiState
}
```

`LocationSelectorViewModel(private val locationRepository: LocationRepository)`:

- On init: collect `getAllLocations()` and `getActiveLocation()` flows, combine into `Content`
- `selectLocation(location: SavedLocation)` — calls `locationRepository.setActive()`
- `deleteLocation(location: SavedLocation)` — calls `locationRepository.deleteLocation()`

### Files to create

- `app/src/main/kotlin/.../viewmodel/LocationSelectorViewModel.kt`
- `app/src/test/kotlin/.../viewmodel/LocationSelectorViewModelTest.kt`

### Tests to write

- Emits `Content` with all locations and the active ID on init
- `selectLocation()` triggers `setActive()` on the repository
- `deleteLocation()` triggers `deleteLocation()` on the repository
- After deleting the active location, the repository selects a remaining location (handled by
  repository — verify the ViewModel delegates correctly)

### Verification

- [ ] `scripts/run.sh viewmodel-tests ./gradlew testDebugUnitTest --tests "*.viewmodel.*Test"`
  passes

---

## [ ] Step 3: Wire Location Selector into the Main Screen

**Goal:** Make the location name in the top bar tappable; open the `LocationSelectorContent` sheet.

**User stories:** US-010, US-012

### What to build

**`TopBar`** — make the location title clickable:

- Add optional `onLocationNameClick: (() -> Unit)? = null` parameter
- When provided, render the location name as a `TextButton` (style `titleLarge`, no extra padding);
  otherwise render it as a plain `Text` (backward-compatible default)

**`MainScreen`** — add location selector sheet:

- Add parameters: `locations: List<SavedLocation>`, `activeLocationId: String`,
  `onLocationSelect: (SavedLocation) -> Unit`, `onAddLocation: () -> Unit`,
  `onEditLocation: (SavedLocation) -> Unit`, `onDeleteLocation: (SavedLocation) -> Unit`
- Add `showLocationSelector: Boolean` state; set to `true` when location name is tapped
- Render a `ModalBottomSheet` (alongside the existing detail sheet) wrapping
  `LocationSelectorContent` when `showLocationSelector` is `true`
- Pass `onClose = { showLocationSelector = false }` and all action callbacks through to the sheet

**`MoonriseNavHost`** — wire the ViewModel and callbacks:

- Create `LocationSelectorViewModel` in the `MAIN` composable destination
- Collect its `uiState`; pass `locations` and `activeLocationId` down to `MainScreen`
- `onLocationSelect` → call `vm.selectLocation()`, then trigger a forecast refresh via
  `mainVm.refresh()`
- `onAddLocation` → navigate to `Routes.addLocation(isFirstTime = false)`
- `onEditLocation` → navigate to `Routes.editLocation(location.id)` (added in Step 4)
- `onDeleteLocation` → call `locationSelectorVm.deleteLocation()`; if deleted location was active,
  also trigger `mainVm.refresh()`

### Files to modify

- `app/src/main/kotlin/.../components/TopBar.kt`
- `app/src/main/kotlin/.../screens/MainScreen.kt`
- `app/src/main/kotlin/.../navigation/MoonriseNavHost.kt`

### Tests to write

Update affected Compose tests to pass new required parameters where needed (use empty defaults).

### Verification

- [ ] App compiles: `scripts/run.sh build-debug ./gradlew assembleDebug`
- [ ] Tapping location name in top bar opens the location selector sheet
- [ ] Selecting a location closes the sheet and refreshes the forecast
- [ ] Tapping "Add Location" in the sheet navigates to AddLocationScreen (additional context)
- [ ] Delete confirmation dialog appears; confirming removes the location
- [ ] Single-location guard: Delete is disabled when only one location exists

---

## [ ] Step 4: Edit Location Flow

**Goal:** Allow users to edit name or coordinates of a saved location.

**User stories:** US-011

### What to build

**`AddLocationContext.EDIT`** — add a third context value:

- Top bar: back arrow, title "Edit Location"
- No welcome art
- Primary button: "Save"
- Pre-populated fields from the existing location

**`AddLocationViewModel` — edit mode:**

- Add `setEditTarget(location: SavedLocation)` — stores the original location; pre-populates
  display state for the NavHost
- Add
  `editLocation(original: SavedLocation, name: String, cityState: String?, latitude: Double, longitude: Double)` —
  validates, calls `locationRepository.updateLocation()`, emits `Success`
- Add `editLocationByCityQuery(original: SavedLocation, cityQuery: String, customName: String)` —
  geocodes, then calls `editLocation()` with resolved coordinates

**Navigation:**

- `Routes.kt` — add `EDIT_LOCATION = "editLocation/{locationId}"` and
  `fun editLocation(locationId: String): String`
- `MoonriseNavHost.kt` — add a `composable(Routes.EDIT_LOCATION)` destination:
    - Load location by ID via `locationRepository.getLocationById()`
    - Pre-populate `cityValue`, `latitudeValue`, `longitudeValue`, `nameValue` from loaded location
    - Pass `context = AddLocationContext.EDIT` to `AddLocationScreen`
    - On `Success`: signal refresh and pop back stack

### Files to modify

- `app/src/main/kotlin/.../screens/AddLocationScreen.kt` — add `EDIT` to `AddLocationContext`; adapt
  title and button text for the EDIT case (already parameterized — minimal change)
- `app/src/main/kotlin/.../viewmodel/AddLocationViewModel.kt` — add edit methods
- `app/src/main/kotlin/.../navigation/Routes.kt` — add `EDIT_LOCATION` and helper
- `app/src/main/kotlin/.../navigation/MoonriseNavHost.kt` — add edit composable

### Files to modify (tests)

- `app/src/test/kotlin/.../viewmodel/AddLocationViewModelTest.kt` — add edit scenarios

### Tests to write

- `editLocation()` calls `updateLocation()` on the repository, not `addLocation()`
- `editLocation()` emits `Success` after successful update
- `editLocation()` emits `Error` with invalid coordinates
- `editLocationByCityQuery()` geocodes and then updates the existing location

### Verification

- [ ] `scripts/run.sh viewmodel-tests ./gradlew testDebugUnitTest --tests "*.viewmodel.*Test"`
  passes
- [ ] Tapping Edit on a location opens AddLocationScreen with pre-filled fields
- [ ] Saving with changed name updates the location in the selector list
- [ ] Saving with changed coordinates triggers a forecast refresh

---

## [ ] Step 5: About Screen

**Goal:** Provide app version, explanation of how good nights are determined, and a glossary.

**User stories:** US-014

### What to build

`AboutScreen` composable:

- Top bar with back arrow, title "About"
- App name and version (read from `BuildConfig.VERSION_NAME`)
- Section: "How Good Nights Are Determined" — explains phase window, timing constraints, weather
- Section: "Glossary" — Azimuth, Full Moon, Moon Phase, Windchill definitions (from PRD Appendix A)
- No interactive elements beyond the back button

Link from Settings:

- Add an "About" item at the bottom of `SettingsScreen` that navigates to `Routes.ABOUT`

### Files to create

- `app/src/main/kotlin/.../screens/AboutScreen.kt`
- `app/src/androidTest/kotlin/.../screens/AboutScreenTest.kt`

### Files to modify

- `app/src/main/kotlin/.../navigation/Routes.kt` — add `ABOUT = "about"`
- `app/src/main/kotlin/.../navigation/MoonriseNavHost.kt` — add about composable
- `app/src/main/kotlin/.../screens/SettingsScreen.kt` — add About navigation row

### Tests to write (Compose)

- About screen shows app name
- About screen shows "How Good Nights Are Determined" section heading
- About screen shows "Glossary" section heading with "Azimuth" entry
- Tapping back closes the screen

### Verification

- [ ] `scripts/run.sh detekt ./gradlew detekt` passes
- [ ] About screen accessible from Settings
- [ ] `scripts/run.sh compose-tests scripts/compose-test.sh` passes

---

## [ ] Step 6: Tutorial / Welcome Screen

**Goal:** Show an optional tutorial on first launch; make it accessible from the About screen.

**User stories:** US-002

### What to build

`TutorialScreen` composable — a vertically paged (or scrollable) screen with three cards:

1. **"What is Moonrise Watcher?"** — brief app description
2. **"What Makes a Good Night?"** — phase window, timing, clear skies
3. **"Reading the Forecast"** — explains the list view, good/bad badges, detail sheet

Navigation controls:

- "Skip" text button (top-right) — exits tutorial immediately
- "Next" / "Done" button (bottom) — advances or exits
- Page indicator dots

First-launch integration:

- `MainScreenFirstTime` gets an "How It Works" link below the "Add Location" button; tapping
  navigates to the tutorial
- Tutorial is not shown automatically on launch (user-initiated from the first-time screen)

Access from About:

- "How It Works" row in `AboutScreen` navigates to `Routes.TUTORIAL`

### Files to create

- `app/src/main/kotlin/.../screens/TutorialScreen.kt`
- `app/src/androidTest/kotlin/.../screens/TutorialScreenTest.kt`

### Files to modify

- `app/src/main/kotlin/.../navigation/Routes.kt` — add `TUTORIAL = "tutorial"`
- `app/src/main/kotlin/.../navigation/MoonriseNavHost.kt` — add tutorial composable
- `app/src/main/kotlin/.../components/StateViews.kt` — add "How It Works" link to
  `FirstTimeSetup`
- `app/src/main/kotlin/.../screens/AboutScreen.kt` — add "How It Works" navigation row

### Tests to write (Compose)

- Tutorial screen shows the first page heading
- Skip button dismisses the tutorial
- Next button advances to page 2
- Done button on last page dismisses the tutorial

### Verification

- [ ] `scripts/run.sh compose-tests scripts/compose-test.sh` passes
- [ ] Tutorial accessible from first-time screen and from About screen
- [ ] Skip exits immediately; Done on last page exits

---

## End-of-Phase Checklist

Before declaring Phase 2 complete:

1. `scripts/run.sh detekt ./gradlew detekt`
2. `scripts/run.sh unit-tests ./gradlew testDebugUnitTest`
3. `scripts/run.sh compose-tests scripts/compose-test.sh`

---

## Summary

| Step | What                          | Key Files Changed                                   | Key Tests                                     | Depends On | Status |
|------|-------------------------------|-----------------------------------------------------|-----------------------------------------------|------------|--------|
| 1    | LocationRepository extensions | `LocationDao`, `LocationRepository`                 | getAllLocations, setActive, delete, update    | —          | [ ]    |
| 2    | LocationSelectorViewModel     | `LocationSelectorViewModel`                         | State on init, selectLocation, deleteLocation | 1          | [ ]    |
| 3    | Wire LocationSelector to UI   | `TopBar`, `MainScreen`, `MoonriseNavHost`           | Compose tests updated                         | 2          | [ ]    |
| 4    | Edit Location Flow            | `AddLocationViewModel`, `Routes`, `MoonriseNavHost` | ViewModel edit scenarios                      | 1          | [ ]    |
| 5    | About Screen                  | `AboutScreen`, `SettingsScreen`, nav                | Compose tests for About                       | —          | [ ]    |
| 6    | Tutorial Screen               | `TutorialScreen`, `StateViews`, nav                 | Compose tests for Tutorial                    | 5          | [ ]    |
