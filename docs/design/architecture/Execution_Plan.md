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

## [ ] Step 3: Wire Location Selector + Edit Flow

**Goal:** Make the location name tappable to open the selector sheet; wire all location management
actions including edit.

**User stories:** US-009, US-010, US-011, US-012

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

**`AddLocationContext.EDIT`** — add a third context value to the enum:

- Top bar: back arrow, title "Edit Location"
- No welcome art
- Primary button: "Save"
- Pre-populated fields from the existing location (City tab if `cityState != null`, else
  Coordinates)

**`AddLocationViewModel` — edit mode:**

- Add
  `editLocation(original: SavedLocation, name: String, cityState: String?, latitude: Double, longitude: Double)` —
  validates, calls `locationRepository.updateLocation()`, emits `Success`
- Add `editLocationByCityQuery(original: SavedLocation, cityQuery: String, customName: String)` —
  geocodes, then calls `editLocation()` with resolved coordinates

**`MoonriseNavHost`** — wire all callbacks and add edit destination:

- Create `LocationSelectorViewModel` in the `MAIN` composable destination
- Collect its `uiState`; pass `locations` and `activeLocationId` down to `MainScreen`
- `onLocationSelect` → call `locationSelectorVm.selectLocation()`, then `mainVm.refresh()`
- `onAddLocation` → navigate to `Routes.addLocation(isFirstTime = false)`
- `onEditLocation` → navigate to `Routes.editLocation(location.id)`
- `onDeleteLocation` → call `locationSelectorVm.deleteLocation()`, then `mainVm.refresh()`
- Add `Routes.EDIT_LOCATION = "editLocation/{locationId}"` and
  `fun editLocation(locationId: String): String` to `Routes.kt`
- Add `composable(Routes.EDIT_LOCATION)` destination:
    - Load location by ID via `locationRepository.getLocationById()`
    - Pre-populate tab and field values per the Q4 decision (City tab if `cityState != null`, else
      Coordinates)
    - Pass `context = AddLocationContext.EDIT` to `AddLocationScreen`
    - On `Success`: signal refresh and pop back stack

### Files to modify

- `app/src/main/kotlin/.../components/TopBar.kt`
- `app/src/main/kotlin/.../screens/MainScreen.kt`
- `app/src/main/kotlin/.../screens/AddLocationScreen.kt` — add `EDIT` to `AddLocationContext`
- `app/src/main/kotlin/.../viewmodel/AddLocationViewModel.kt` — add edit methods
- `app/src/main/kotlin/.../navigation/Routes.kt` — add `EDIT_LOCATION` and helper
- `app/src/main/kotlin/.../navigation/MoonriseNavHost.kt` — wire selector + add edit composable

### Tests to write

- `editLocation()` calls `updateLocation()` on the repository, not `addLocation()`
- `editLocation()` emits `Success` after successful update
- `editLocation()` emits `Error` with invalid coordinates
- `editLocationByCityQuery()` geocodes and then updates the existing location
- Update affected Compose tests to pass new `MainScreen` parameters (use empty defaults)

### Verification

- [ ] `scripts/run.sh viewmodel-tests ./gradlew testDebugUnitTest --tests "*.viewmodel.*Test"`
  passes
- [ ] App compiles: `scripts/run.sh build-debug ./gradlew assembleDebug`
- [ ] Tapping location name in top bar opens the location selector sheet
- [ ] Selecting a location closes the sheet and refreshes the forecast
- [ ] Tapping "Add Location" in the sheet navigates to AddLocationScreen (additional context)
- [ ] Delete confirmation dialog appears; confirming removes the location
- [ ] Single-location guard: Delete is disabled when only one location exists
- [ ] Tapping Edit on a location opens AddLocationScreen with pre-filled fields and correct tab
- [ ] Saving with changed name updates the location in the selector list
- [ ] Saving with changed coordinates triggers a forecast refresh

---

## [ ] Step 4: About Screen

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

## [ ] Step 5: Tutorial / Welcome Screen

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

## Open Questions

These questions should be resolved before implementation begins.

---

### Q1: Per-location settings — in scope for Phase 2?

The PRD Phase 2 goal mentions "Configurable maximum moonrise time per location." US-013's AC also
notes "Settings apply to all locations (or per-location in Phase 2)." The current data model has a
single-row global `SettingsEntity` (id = 1).

**Impact if in scope:** Requires a Room schema migration, a new table or column on `LocationEntity`,
and significant changes to how `ForecastRepository` fetches settings. Would need a dedicated step
before Step 1.

**Impact if out of scope:** Settings remain global. Plan stands as written.

**Suggestion:** Defer to Phase 3. The user stories don't include a specific story for per-location
settings, and the existing US-013 AC phrasing ("or per-location") reads as aspirational rather than
required. Keeping settings global avoids a schema migration and keeps Phase 2 focused on the
location management UX.

**Decision:** Settings remain global for Phase 2. Per-location settings documented as a future
enhancement in the PRD.

**Status:** ✅ Resolved

---

### Q2: Who activates the next location after a delete?

When the user deletes the currently active location, something must activate a remaining one before
the forecast can reload. Two options:

- **Repository handles it:** `deleteLocation()` detects if the deleted location was active and
  automatically calls `setActive()` on the first remaining location. Leaves the DB in a consistent
  state regardless of caller.
- **Caller handles it:** `deleteLocation()` only deletes. The ViewModel or NavHost checks whether
  the deleted location was active and calls `setActive()` if needed.

**Suggestion:** Repository handles it. Keeps DB consistency invariants inside the data layer;
callers
don't need to reason about activation state after a delete.

**Decision:** Repository handles it. `deleteLocation()` will auto-activate the first remaining
location if the deleted one was active.

**Status:** ✅ Resolved

---

### Q3: MainViewModel — explicit refresh or flow-reactive after location switch?

When the user switches locations, the plan calls `mainVm.refresh()` explicitly from NavHost. An
alternative is to restructure `MainViewModel` to observe `getActiveLocation()` as a continuous Flow
(using `flatMapLatest`), so it auto-reacts to any location change without external signals.

- **Explicit refresh (current plan):** No ViewModel restructuring. Consistent with the existing
  `refresh()` pattern.
- **Flow-reactive:** More elegant; would also handle edit-coordinate changes automatically. Requires
  restructuring `MainViewModel.loadForecast()`.

**Suggestion:** Explicit refresh for now. The existing pattern is well-tested and the extra wiring
in
NavHost is minimal. Flow-reactive is a worthwhile refactor but not necessary to ship Phase 2.

**Decision:** Explicit refresh. Flow-reactive MainViewModel documented as a future enhancement in
the PRD.

**Status:** ✅ Resolved

---

### Q4: Edit pre-fill — which tab and what value?

A location has both `cityState` (nullable) and `lat/lng`. When the edit screen opens, two options:

- **Option A:** If `cityState != null`, pre-select the City tab and fill it with `cityState`. If
  null, pre-select Coordinates.
- **Option B:** Always pre-select Coordinates with lat/lng.

**Suggestion:** Option A. It preserves the user's original input context (they entered a city name;
showing it back is less surprising than showing raw coordinates). Coordinates tab remains available
if they want to switch.

**Decision:** Option A. Pre-select City tab with `cityState` if non-null; otherwise pre-select
Coordinates tab with lat/lng.

**Status:** ✅ Resolved

---

### Q5: Steps 3 and 4 ordering (minor)

Step 3 references `Routes.editLocation()` which is added in Step 4. The dependency table doesn't
reflect this link. Two resolutions:

- **Merge Steps 3 and 4** into a single step ("Wire Location Selector + Edit Flow").
- **Add Step 4 as a dependency of Step 3** and note that the edit callback in Step 3 can be stubbed
  (navigate to a TODO route) until Step 4 is complete.

**Suggestion:** Merge Steps 3 and 4. They share the same NavHost composable and are naturally done
together in one sitting.

**Decision:** Merged into a single step. Steps renumbered: old Steps 5 and 6 become Steps 4 and 5.

**Status:** ✅ Resolved

---

## PR #16 Review Comments

Comments from the Copilot reviewer on PR #16. Work through these before declaring Phase 2 complete.

| # | File                                                    | Line | Issue                                                                                                                                                                                                    | Status |
|---|---------------------------------------------------------|------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------|
| 1 | `app/src/androidTest/.../screens/AboutScreenTest.kt`    | 64   | Uses Kotlin `assert(...)` — JVM assertions can be silently disabled. Replace with AssertJ/JUnit assertion.                                                                                               | [x]    |
| 2 | `app/src/androidTest/.../screens/TutorialScreenTest.kt` | 40   | Same as #1 — uses Kotlin `assert(...)`.                                                                                                                                                                  | [x]    |
| 3 | `app/src/androidTest/.../screens/TutorialScreenTest.kt` | 71   | Same as #1 — uses Kotlin `assert(...)`.                                                                                                                                                                  | [x]    |
| 4 | `app/src/main/.../screens/TutorialScreen.kt`            | 74   | Top app bar has empty title (`title = {}`). Wireframe specifies "How It Works".                                                                                                                          | [x]    |
| 5 | `app/src/main/.../viewmodel/MainViewModel.kt`           | 53   | Inside `collectLatest { location -> ... }`, `loadForecast()` re-fetches active location via `getActiveLocation().first()` — redundant and inconsistent. Pass `location` directly as a parameter instead. | [x]    |
| 6 | `app/src/main/.../repository/LocationRepository.kt`     | 61   | Delete-then-activate ordering leaves a transient window with no active location, causing UI flicker. Activate replacement *before* deleting.                                                             | [x]    |
| 7 | `app/src/main/.../navigation/MoonriseNavHost.kt`        | 282  | `locationId` falls back to `""` if nav arg is missing; `toLong()` on blank string crashes. Use `toLongOrNull()` and pop back on invalid input.                                                           | [x]    |
| 8 | `app/src/test/.../viewmodel/StubDaos.kt`                | 28   | `getById()` stub returns non-null `LocationEntity` but the DAO interface declares `LocationEntity?`. Won't compile — change stub return type to `LocationEntity?`.                                       | [x]    |

---

## End-of-Phase Checklist

Before declaring Phase 2 complete:

1. `scripts/run.sh detekt ./gradlew detekt`
2. `scripts/run.sh unit-tests ./gradlew testDebugUnitTest`
3. `scripts/run.sh compose-tests scripts/compose-test.sh`

---

## Summary

| Step | What                               | Key Files Changed                                                   | Key Tests                                       | Depends On | Status |
|------|------------------------------------|---------------------------------------------------------------------|-------------------------------------------------|------------|--------|
| 1    | LocationRepository extensions      | `LocationDao`, `LocationRepository`                                 | getAllLocations, setActive, delete, update      | —          | [ ]    |
| 2    | LocationSelectorViewModel          | `LocationSelectorViewModel`                                         | State on init, selectLocation, deleteLocation   | 1          | [ ]    |
| 3    | Wire Location Selector + Edit Flow | `TopBar`, `MainScreen`, `AddLocationViewModel`, `Routes`, `NavHost` | ViewModel edit scenarios, Compose tests updated | 1, 2       | [ ]    |
| 4    | About Screen                       | `AboutScreen`, `SettingsScreen`, nav                                | Compose tests for About                         | —          | [ ]    |
| 5    | Tutorial Screen                    | `TutorialScreen`, `StateViews`, nav                                 | Compose tests for Tutorial                      | 4          | [ ]    |
