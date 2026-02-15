# Main Screen Wireframe

**Related Requirements:** PRD 6.1–6.4, 7.1–7.3 | User Stories: US-003, US-004, US-006

---

## Screen Elements

| Region        | Element            | Source    | Notes                                    |
|---------------|--------------------|-----------|------------------------------------------|
| Top bar       | Location name      | US-010    | Tappable to switch locations             |
| Top bar       | Menu icon          | PRD 6.x   | Opens settings, locations, about         |
| Today section | Date               | US-003    | e.g. "Wed, Feb 12"                       |
| Today section | Good/bad badge     | PRD 7.1   | Green GOOD or red/gray BAD with reason   |
| Today section | Sunset time        | PRD 6.4.1 | e.g. "Sunset 5:34 PM"                    |
| Today section | Moonrise time      | PRD 6.4.2 | e.g. "Moonrise 6:12 PM"                  |
| Today section | Azimuth            | PRD 7.3   | Degrees + cardinal, e.g. "98° ESE"      |
| Today section | Weather indicator  | PRD 6.4.3 | Icon or text, e.g. "Clear"              |
| Forecast list | Date               | US-004    | One row per phase-window day             |
| Forecast list | Good/bad indicator | PRD 7.1   | Green dot / red dot / gray dot           |
| Forecast list | Sunset time        | PRD 6.4.1 | Abbreviated                              |
| Forecast list | Moonrise time      | PRD 6.4.2 | Abbreviated                              |
| Forecast list | Azimuth (short)    | US-006    | e.g. "98° ESE"                           |
| Forecast list | Sky clearness      | PRD 6.4.3 | Icon or "Weather unknown" beyond 14 days |

---

## Sample Data

Location: **Home — Seattle, WA**

| Date         | Sunset  | Moonrise | Azimuth  | Weather         | Verdict        |
|--------------|---------|----------|----------|-----------------|----------------|
| Wed, Feb 12  | 5:34 PM | 6:12 PM  | 98° ESE  | Clear           | GOOD           |
| Thu, Feb 13  | 5:35 PM | 7:08 PM  | 103° ESE | Partly cloudy   | GOOD           |
| Fri, Feb 14  | 5:37 PM | 8:15 PM  | 107° ESE | Cloudy          | BAD (weather)  |
| Sun, Feb 16  | 5:39 PM | 11:42 PM | 112° ESE | Clear           | BAD (too late) |
| Thu, Mar 12  | 6:12 PM | 6:45 PM  | 96° E    | Weather unknown | GOOD           |

> **Note:** Feb 15 is omitted (outside phase window). The gap between Feb 16 and Mar 12 represents
> hidden days between two phase windows — the app shows only phase-window days per PRD 1.1.

---

## Portrait Layout

```
┌──────────────────────────────────┐
│ ☰  Home — Seattle, WA           │  ← top bar
├──────────────────────────────────┤
│                                  │
│  TODAY  Wed, Feb 12     ● GOOD   │  ← today section
│                                  │
│  Sunset    5:34 PM               │
│  Moonrise  6:12 PM               │
│  Azimuth   98° ESE               │
│  Weather   ☀ Clear               │
│                                  │
├──────────────────────────────────┤
│  UPCOMING                        │  ← forecast list header
│┌────────────────────────────────┐│
││ ● Thu, Feb 13                  ││
││   ☼ 5:35  ☽ 7:08  103° ESE ⛅ ││
│├────────────────────────────────┤│
││ ○ Fri, Feb 14                  ││
││   ☼ 5:37  ☽ 8:15  107° ESE ☁ ││
│├────────────────────────────────┤│
││ ○ Sun, Feb 16                  ││
││   ☼ 5:39  ☽ 11:42 112° ESE ☀ ││
│├────────────────────────────────┤│
││ ● Thu, Mar 12                  ││
││   ☼ 6:12  ☽ 6:45   96° E   ? ││
│└────────────────────────────────┘│
│         ↕ scrollable             │
└──────────────────────────────────┘
```

## Landscape Layout

```
┌─────────────────────────────────────────────────────────────────┐
│ ☰  Home — Seattle, WA                                          │
├───────────────────────┬─────────────────────────────────────────┤
│                       │  UPCOMING                               │
│  TODAY                │ ┌─────────────────────────────────────┐ │
│  Wed, Feb 12  ● GOOD  │ │ ● Thu, Feb 13                      │ │
│                       │ │   ☼ 5:35  ☽ 7:08  103° ESE  ⛅     │ │
│  Sunset    5:34 PM    │ ├─────────────────────────────────────┤ │
│  Moonrise  6:12 PM    │ │ ○ Fri, Feb 14                      │ │
│  Azimuth   98° ESE    │ │   ☼ 5:37  ☽ 8:15  107° ESE  ☁     │ │
│  Weather   ☀ Clear    │ ├─────────────────────────────────────┤ │
│                       │ │ ○ Sun, Feb 16                       │ │
│                       │ │   ☼ 5:39  ☽ 11:42 112° ESE  ☀     │ │
│                       │ ├─────────────────────────────────────┤ │
│                       │ │ ● Thu, Mar 12                       │ │
│                       │ │   ☼ 6:12  ☽ 6:45   96° E    ?     │ │
│                       │ └─────────────────────────────────────┘ │
│                       │          ↕ scrollable                   │
└───────────────────────┴─────────────────────────────────────────┘
```

## Annotations

### Symbol Legend

Symbols used in the ASCII wireframes above:

- **Menu** (`☰`) — Hamburger menu icon (Material `Icons.Menu`)
- **Good** (`●`) — Good verdict, filled circle, green
- **Bad** (`○`) — Bad verdict, hollow circle, gray
- **Sunset** (`☼`) — Sunset time prefix
- **Moonrise** (`☽`) — Moonrise time prefix
- **Clear** (`☀`) — Clear sky
- **Partly cloudy** (`⛅`) — Partly cloudy sky
- **Cloudy** (`☁`) — Cloudy / overcast sky
- **Unknown** (`?`) — Weather unknown (beyond forecast range)

### Adaptive Layout

| Property             | Portrait (< 600 dp)                    | Landscape (>= 600 dp)                 |
|----------------------|----------------------------------------|---------------------------------------|
| Overall structure    | Vertical `Column` (today then list)    | Horizontal `Row` (today beside list)  |
| Today section width  | Full width                             | 1/3 of available width (`weight(1)`)  |
| Forecast list width  | Full width                             | 2/3 of available width (`weight(2)`)  |
| Today section height | Capped at 40% of screen height         | Full height of content area           |
| Scroll behavior      | Forecast list scrolls; today is fixed  | Forecast list scrolls; today is fixed |
| Breakpoint           | `BoxWithConstraints.maxWidth` = 600 dp | —                                     |

### Top Bar

- Uses Material 3 `TopAppBar` with location name as title
- Navigation icon: hamburger menu (`Icons.Default.Menu`)
- Tapping the location name will open location switcher (not yet implemented)
- Tapping the menu icon opens settings/locations/about (not yet implemented)

### Today Section

- Wrapped in a Material 3 `Card` with 2 dp elevation
- Outer padding: 16 dp horizontal, 8 dp vertical
- Inner padding: 16 dp all sides
- **Header row** (full width, space-between):
  - Left side: "TODAY" label (`labelLarge`, bold) + formatted date (`bodyLarge`)
  - Right side: verdict badge — "● GOOD" on green background or "● BAD (reason)" on red background,
    with 4 dp rounded corners and 8×4 dp padding
- **Detail rows** (below header, 12 dp top padding, 4 dp vertical spacing):
  - Each row: label (`bodyMedium`, `onSurfaceVariant`) + value (`bodyMedium`, medium weight)
  - Labels: "Sunset", "Moonrise", "Azimuth", "Weather"
  - Date format: `"EEE, MMM d"` → e.g. "Wed, Feb 12"
  - Time format: `"h:mm a"` → e.g. "5:34 PM"
  - Azimuth format: `"{degrees}° {cardinal}"` → e.g. "98° ESE"
  - Weather format: icon + label → e.g. "☀ Clear", "? Weather unknown"

### Forecast List

- Section header: "UPCOMING" label (rendered inside `ForecastList` composable)
- Scrollable `LazyColumn`
- Each item is a `ForecastListItem` separated by a `HorizontalDivider`
- Entire item row is tappable (navigates to detail view, not yet implemented)

### Forecast List Item

- Outer padding: 16 dp horizontal, 10 dp vertical
- **Line 1:** verdict dot + formatted date
  - Dot: `●` (green) for GOOD, `○` (gray) for BAD, 8 dp end padding
  - Date: `bodyLarge`, medium weight, same format as today header
- **Line 2:** compact detail string, 24 dp start padding, 2 dp top padding
  - Format: `"☼ {sunset}  ☽ {moonrise}  {azimuth}° {cardinal}  {weatherIcon}"`
  - Time format (abbreviated): `"h:mm"` (no AM/PM) → e.g. "5:35"
  - Style: `bodyMedium`, `onSurfaceVariant`

### Not Yet Shown

These states are not yet wireframed and will need separate designs:

- Empty state (no upcoming phase-window days)
- Loading state (fetching data)
- Error state (network failure, location unavailable)
- First-time setup (no location configured)
