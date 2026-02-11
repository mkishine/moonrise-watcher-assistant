# User Flow Diagrams
# Moonrise Watching Assistant

**Date:** February 3, 2026
**Related Documents:** Moonrise_App_PRD.md, User_Stories.md

---

## Table of Contents

1. [Introduction](#introduction)
2. [Flow 1: First-Time User Setup](#flow-1-first-time-user-setup)
3. [Flow 2: Daily Check Workflow](#flow-2-daily-check-workflow)
4. [Flow 3: Location Management](#flow-3-location-management)
5. [Flow 4: Settings Configuration](#flow-4-settings-configuration)
6. [Flow 5: Detail View Navigation](#flow-5-detail-view-navigation)
7. [Complete App Navigation Map](#complete-app-navigation-map)

---

## Introduction

This document contains user flow diagrams showing how users interact with the Moonrise Watching Assistant. Each diagram is written in Mermaid format and will render as a flowchart on GitHub and in most markdown viewers.

---

## Flow 1: First-Time User Setup

This flow shows what happens when a user opens the app for the first time.

```mermaid
flowchart TD
    Start(["User Opens App<br>First Time"]) --> Location["Enter Location Screen"]

    Location --> EnterMethod{"Location Entry<br>Method"}
    EnterMethod -->|City Name| EnterCity["Enter City, State"]
    EnterMethod -->|Coordinates| EnterCoords["Enter Lat/Long"]

    EnterCity --> Validate{"Location Valid?"}
    EnterCoords --> Validate

    Validate -->|No| Error["Show Error Message"]
    Error --> Location

    Validate -->|Yes| SaveLocation["Save Location"]
    SaveLocation --> LoadData["Fetch Forecast Data"]
    LoadData --> MainScreen["Show Main Screen<br>Today + Forecast List"]
    MainScreen --> End(["User Can Start Using App"])
```

**Key Decision Points:**
- User chooses between city name or coordinates
- Location validation prevents invalid entries
- After successful setup, user immediately sees forecast

---

## Flow 2: Daily Check Workflow

This is the most common user flow - opening the app to check if tonight is good for moonrise watching.

```mermaid
flowchart TD
    Start(["User Opens App"]) --> CheckCache{"Cached Data<br>Available?"}
    CheckCache -->|Yes| ShowCached["Display Cached Data"]
    CheckCache -->|No| Loading["Show Loading Indicator"]
    Loading --> FetchData["Fetch Forecast from API"]
    FetchData --> ShowCached

    ShowCached --> MainView["Main Screen:<br>Today Prominent<br>Forecast List"]

    MainView --> UserAction{"User Action"}

    UserAction -->|Refresh| PullRefresh["Pull to Refresh"]
    PullRefresh --> FetchData

    UserAction -->|Check Tonight| ReadToday["Read Today's Info:<br>Sunset, Moonrise, Weather"]
    ReadToday --> TonightGood{"Tonight Marked<br>as Good?"}
    TonightGood -->|Yes| PlanWatch["User Plans to Watch"]
    TonightGood -->|No| CheckOther["Scroll to See<br>Other Good Nights"]

    UserAction -->|Scan List| ScanList["Scroll Through<br>Phase Window Days"]
    ScanList --> SpotGood{"See Green<br>Good Night?"}
    SpotGood -->|Yes| TapDay["Tap on Day"]
    SpotGood -->|No| End1(["No Good Nights<br>This Period"])

    TapDay --> DetailView["Detail View Screen"]
    DetailView --> CheckDetails["Review Weather Details:<br>Temp, Wind, Clouds, Azimuth"]
    CheckDetails --> BackAction{"User Action"}
    BackAction -->|Back Button| MainView
    BackAction -->|Swipe Next| NextDay["Next Day Detail"]
    BackAction -->|Swipe Previous| PrevDay["Previous Day Detail"]
    NextDay --> DetailView
    PrevDay --> DetailView

    PlanWatch --> End2(["User Exits App"])
    CheckOther --> End2
```

**Key Behaviors:**
- App shows cached data immediately if available
- Today's info is prioritized for quick decision-making
- Good nights are visually highlighted for easy scanning
- Users can navigate between details without returning to list

---

## Flow 3: Location Management

This flow shows how users manage multiple viewing locations (Phase 2 feature, but included for completeness).

```mermaid
flowchart TD
    Start(["User Wants to Manage<br>Locations"]) --> OpenMenu["Open Menu/<br>Location Selector"]

    OpenMenu --> Action{"User Action"}

    Action -->|Switch Location| ShowList["Show Saved<br>Locations List"]
    ShowList --> SelectLoc["Tap Location"]
    SelectLoc --> LoadForecast["Load Forecast for<br>Selected Location"]
    LoadForecast --> MainScreen["Return to Main Screen<br>with New Location Data"]

    Action -->|Add Location| AddScreen["Add Location Screen"]
    AddScreen --> EntryMethod{"Entry Method"}
    EntryMethod -->|City Name| EnterCity["Enter City"]
    EntryMethod -->|Coordinates| EnterCoords["Enter Lat/Long"]

    EnterCity --> NameLoc["Optional: Name<br>This Location"]
    EnterCoords --> NameLoc
    NameLoc --> ValidateLoc{"Valid Location?"}
    ValidateLoc -->|No| ErrorMsg["Show Error"]
    ErrorMsg --> AddScreen
    ValidateLoc -->|Yes| SaveNew["Save Location"]
    SaveNew --> ShowList

    Action -->|Edit Location| ShowList2["Show Saved Locations"]
    ShowList2 --> LongPress["Long Press on Location"]
    LongPress --> EditMenu["Show Edit Menu"]
    EditMenu --> EditAction{"Edit Action"}
    EditAction -->|Edit Name| EditName["Edit Location Name"]
    EditAction -->|Edit Coords| EditCoords["Edit Coordinates"]
    EditAction -->|Delete| ConfirmDel{"Confirm Delete?"}

    EditName --> SaveChanges["Save Changes"]
    EditCoords --> SaveChanges
    SaveChanges --> ShowList

    ConfirmDel -->|No| ShowList
    ConfirmDel -->|Yes| CheckOnly{"Only Location?"}
    CheckOnly -->|Yes| ErrorCantDel["Error: Must Have<br>At Least One Location"]
    ErrorCantDel --> ShowList
    CheckOnly -->|No| DeleteLoc["Delete Location"]
    DeleteLoc --> SwitchDefault["Switch to Another<br>Location"]
    SwitchDefault --> MainScreen

    MainScreen --> End(["User Continues Using App"])
```

**Key Features:**
- Quick location switching from main screen
- Multiple ways to add locations
- Safety check prevents deleting last location
- Editing preserves user's place in the app

---

## Flow 4: Settings Configuration

This flow shows how users configure app preferences.

```mermaid
flowchart TD
    Start(["User Opens Settings"]) --> SettingsScreen["Settings Screen"]

    SettingsScreen --> SettingChoice{"Setting to Change"}

    SettingChoice -->|Max Moonrise Time| TimeSetting["Maximum Moonrise Time"]
    TimeSetting --> TimePicker["Show Time Picker"]
    TimePicker --> SetTime["User Sets Time"]
    SetTime --> SaveSetting["Save Preference"]
    SaveSetting --> Recalc["Recalculate Good/Bad<br>Night Indicators"]
    Recalc --> SettingsScreen

    SettingChoice -->|Before-Sunset Tolerance| ToleranceSetting["Before-Sunset Tolerance"]
    ToleranceSetting --> SetTolerance["User Sets Minutes"]
    SetTolerance --> SaveSetting

    SettingChoice -->|Phase Window| PhaseSetting["Days Before/After<br>Full Moon, Forecast Period"]
    PhaseSetting --> SetPhase["User Adjusts Values"]
    SetPhase --> SaveSetting

    SettingChoice -->|Unit System| UnitSetting["Imperial / Metric Toggle"]
    UnitSetting --> SaveUnit["Save Preference"]
    SaveUnit --> SettingsScreen

    SettingsScreen --> Exit["Exit Settings"]
    Exit --> MainScreen["Return to Main Screen<br>with Updated Settings"]
    MainScreen --> End(["User Continues Using App"])
```

**Key Behaviors:**
- Changing moonrise time or tolerance immediately updates good/bad indicators
- Changing phase window parameters immediately updates the forecast list
- Unit system change applies to all temperature and wind speed values
- Settings are persistent across app sessions

---

## Flow 5: Detail View Navigation

This flow focuses specifically on how users interact with the detail view.

```mermaid
flowchart TD
    Start(["User in Main Screen<br>List View"]) --> TapDay["Tap on a Day"]
    TapDay --> DetailView["Detail View Opens:<br>Full Weather Info<br>Temperature, Wind,<br>Clouds, Precipitation,<br>Azimuth"]

    DetailView --> UserAction{"User Action"}

    UserAction -->|Read Info| ReviewDetails["Review All Details"]
    ReviewDetails --> Decision{"Decide to Watch?"}
    Decision -->|Yes| MentalNote["Mental Note<br>of Date/Time"]
    Decision -->|No| UserAction

    UserAction -->|Swipe Left| NextDay["Load Next Day<br>Detail View"]
    NextDay --> DetailView

    UserAction -->|Swipe Right| PrevDay["Load Previous Day<br>Detail View"]
    PrevDay --> DetailView

    UserAction -->|Back Button| CloseDetail["Close Detail View"]
    CloseDetail --> MainScreen["Return to<br>Main Screen List"]

    UserAction -->|Tap Background| CloseDetail

    MentalNote --> CloseDetail
    MainScreen --> End(["User Continues<br>or Exits App"])
```

**Navigation Options:**
- Tap to open detail
- Swipe to navigate between days without closing
- Multiple ways to close (back button, tap outside)
- Information is read-only (no editing in detail view)

---

## Complete App Navigation Map

This diagram shows all major screens and how they connect.

```mermaid
flowchart TD
    Start(["App Launch"]) --> FirstTime{"First Time<br>User?"}

    FirstTime -->|Yes| Setup["Location Setup"]
    Setup --> Main

    FirstTime -->|No| Main["Main Screen:<br>Today + Forecast List"]

    Main --> MainActions{"User Action"}

    MainActions -->|Tap Day| Detail["Detail View"]
    Detail -->|Back/Close| Main
    Detail -->|Swipe| Detail

    MainActions -->|Tap Location| LocSelector["Location Selector"]
    LocSelector -->|Select| Main
    LocSelector -->|Add New| AddLoc["Add Location"]
    AddLoc -->|Save| LocSelector
    LocSelector -->|Edit/Delete| EditLoc["Edit Location"]
    EditLoc -->|Save| LocSelector

    MainActions -->|Pull Refresh| Refresh["Refresh Data"]
    Refresh --> Main

    MainActions -->|Open Menu| Menu["Menu/Hamburger"]
    Menu --> MenuChoice{"Menu Option"}

    MenuChoice -->|Settings| Settings["Settings Screen"]
    Settings -->|Back| Main

    MenuChoice -->|Locations| LocSelector

    Main --> Exit(["User Exits App"])
```

**Screen Hierarchy:**
1. **Main Screen** - Central hub, where users spend most time
2. **Detail View** - Modal overlay, quick access and exit
3. **Location Selector** - Secondary screen, location management
4. **Settings** - Secondary screen, preferences

---

## Navigation Principles

### Design Decisions

1. **Main Screen is Home Base:** All flows return to main screen
2. **Minimal Depth:** No screen is more than 2 taps from main screen
3. **Fast Access:** Most common action (check tonight) requires zero navigation
4. **Non-Modal Settings:** Settings don't block main functionality
5. **Swipeable Details:** Users can browse days without constant back/forth

### Common User Paths

**Path 1: Quick Tonight Check**
```
Open App → Read Today Section → Exit
(0 taps beyond app launch)
```

**Path 2: Find Good Night This Week**
```
Open App → Scan List → Tap Good Day → Read Details → Back
(2 taps total)
```

**Path 3: Change Location**
```
Open App → Tap Location → Select Different Location → View New Forecast
(2 taps total)
```

**Path 4: Adjust Bedtime Preference**
```
Open App → Menu → Settings → Change Max Time → Back to Main
(3 taps total)
```

---

## Error Handling Flows

### Network Error During Data Fetch

```mermaid
flowchart TD
    Start["User Opens App"] --> Fetch["Attempt to Fetch Data"]
    Fetch --> Network{"Network<br>Available?"}

    Network -->|No| Cached{"Cached Data<br>Available?"}
    Cached -->|Yes| ShowCached["Show Cached Data<br>+ Offline Indicator"]
    Cached -->|No| ShowError["Show Error Screen:<br>No Connection<br>Retry Button"]

    ShowError --> Retry{"User Taps Retry?"}
    Retry -->|Yes| Fetch
    Retry -->|No| End(["User Exits"])

    Network -->|Yes| API{"API<br>Responds?"}
    API -->|No| ShowError
    API -->|Yes| ShowData["Show Fresh Data"]

    ShowCached --> UserNote["User Knows Data<br>May Be Outdated"]
    ShowData --> End2(["User Continues Normally"])
    UserNote --> End2
```

### Invalid Location Entry

```mermaid
flowchart TD
    Start["User Enters Location"] --> Submit["Tap Save/Submit"]
    Submit --> Validate{"Valid Format?"}

    Validate -->|No| FormatError["Show Error:<br>Invalid Format<br>Examples Shown"]
    FormatError --> Edit["User Edits Entry"]
    Edit --> Submit

    Validate -->|Yes| Lookup{"Location<br>Found?"}
    Lookup -->|No| NotFound["Show Error:<br>Location Not Found<br>Suggestions if Available"]
    NotFound --> Edit

    Lookup -->|Yes| Coords{"Get<br>Coordinates?"}
    Coords -->|No| ServiceError["Show Error:<br>Service Temporarily<br>Unavailable"]
    ServiceError --> Retry{"Retry?"}
    Retry -->|Yes| Lookup
    Retry -->|No| Edit

    Coords -->|Yes| Success["Save Location"]
    Success --> LoadForecast["Fetch Forecast Data"]
    LoadForecast --> End(["Location Saved<br>Forecast Displayed"])
```

---

## Notes for Developers

### Rendering These Diagrams

These Mermaid diagrams will render automatically on:
- GitHub (in markdown files)
- GitLab
- Many markdown editors (VS Code with Mermaid extension, Obsidian, etc.)

To render locally:
```bash
# Install mermaid-cli
npm install -g @mermaid-js/mermaid-cli

# Convert to image
mmdc -i User_Flows.md -o flows.png
```

### Using in Development

- Reference these flows when implementing navigation
- Use as basis for creating automated UI tests
- Share with designers to ensure UI supports these flows
- Update diagrams when requirements change

