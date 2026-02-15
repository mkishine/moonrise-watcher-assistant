# Detail View Wireframe

**Related Requirements:** PRD 6.4.4–6.4.8, 7.1, 7.3 | User Stories: US-005, US-006, US-007

---

## Screen Elements

| Region       | Element              | Source    | Notes                                            |
|--------------|----------------------|-----------|--------------------------------------------------|
| Header       | Date (full format)   | US-005    | e.g. "Thursday, February 13, 2026"               |
| Header       | Verdict badge        | PRD 7.1   | Green GOOD or red BAD with reason                |
| Astronomical | Sunset time          | PRD 6.4.1 | e.g. "5:35 PM"                                   |
| Astronomical | Moonrise time        | PRD 6.4.2 | e.g. "7:08 PM"                                   |
| Astronomical | Azimuth              | PRD 7.3   | Degrees + cardinal + expanded, e.g. "103° ESE"   |
| Weather      | Sky condition        | PRD 6.4.8 | e.g. "Partly Cloudy", "Cloud cover 40%"          |
| Weather      | Temperature          | PRD 6.4.4 | e.g. "42°F"                                      |
| Weather      | Windchill            | PRD 6.4.5 | e.g. "Feels 35°F"                                |
| Weather      | Wind speed           | PRD 6.4.6 | e.g. "12 mph NW"                                 |
| Weather      | Precipitation        | PRD 6.4.7 | e.g. "10% chance rain"                           |
| Verdict      | Constraint checklist | US-007    | Pass/fail for each constraint (phase, time, sky) |
| Navigation   | Swipe indicators     | US-005    | Left/right arrows or dots for adjacent days      |
| Navigation   | Close handle         | US-005    | Drag handle at top of bottom sheet               |

---

## Sample Data

### Good Day

| Field         | Value                           |
|---------------|---------------------------------|
| Date          | Thursday, February 13, 2026     |
| Verdict       | GOOD                            |
| Sunset        | 5:35 PM                         |
| Moonrise      | 7:08 PM                         |
| Azimuth       | 103° ESE (East-Southeast)       |
| Sky           | Partly Cloudy · Cloud cover 40% |
| Temperature   | 42°F                            |
| Windchill     | Feels 35°F                      |
| Wind          | 12 mph NW                       |
| Precipitation | 10% chance rain                 |

### Bad Day (Too Late)

| Field         | Value                     |
|---------------|---------------------------|
| Date          | Sunday, February 16, 2026 |
| Verdict       | BAD — Moon rises too late |
| Sunset        | 5:39 PM                   |
| Moonrise      | 11:42 PM                  |
| Azimuth       | 112° ESE (East-Southeast) |
| Sky           | Clear · Cloud cover 5%    |
| Temperature   | 38°F                      |
| Windchill     | Feels 30°F                |
| Wind          | 8 mph N                   |
| Precipitation | 0% chance                 |

### Weather Unknown (Beyond 14 Days)

| Field         | Value                    |
|---------------|--------------------------|
| Date          | Thursday, March 12, 2026 |
| Verdict       | GOOD (weather pending)   |
| Sunset        | 6:12 PM                  |
| Moonrise      | 6:45 PM                  |
| Azimuth       | 96° E (East)             |
| Sky           | Weather unknown          |
| Temperature   | —                        |
| Windchill     | —                        |
| Wind          | —                        |
| Precipitation | —                        |

---

## Portrait Layout — Good Day

```
┌──────────────────────────────────┐
│ ☰  Home — Seattle, WA           │  ← main screen (dimmed)
│┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄│
│░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│
│░░░░░░░░░░░ (dimmed) ░░░░░░░░░░░░░│
├──────────────────────────────────┤
│          ── drag handle ──       │  ← bottom sheet
│                                  │
│  Thu, Feb 13, 2026      ● GOOD   │
│                                  │
│  ASTRONOMICAL                    │
│  ─────────────────────────────── │
│  Sunset     5:35 PM              │
│  Moonrise   7:08 PM              │
│  Azimuth    103° ESE             │
│             (East-Southeast)     │
│                                  │
│  WEATHER                         │
│  ─────────────────────────────── │
│  Sky        ⛅ Partly Cloudy     │
│             Cloud cover 40%      │
│  Temp       42°F  Feels 35°F    │
│  Wind       12 mph NW           │
│  Precip     10% chance rain     │
│                                  │
│  VERDICT                         │
│  ─────────────────────────────── │
│  ✓ Moon in phase window          │
│  ✓ Moonrise after sunset        │
│  ✓ Moonrise before 11:00 PM     │
│  ✓ Sky mostly clear              │
│                                  │
│       ◂ swipe between days ▸     │
└──────────────────────────────────┘
```

## Portrait Layout — Bad Day

```
┌──────────────────────────────────┐
│ ☰  Home — Seattle, WA           │
│┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄│
│░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│
│░░░░░░░░░░░ (dimmed) ░░░░░░░░░░░░░│
├──────────────────────────────────┤
│          ── drag handle ──       │
│                                  │
│  Sun, Feb 16, 2026  ○ BAD       │
│                     (too late)   │
│                                  │
│  ASTRONOMICAL                    │
│  ─────────────────────────────── │
│  Sunset     5:39 PM              │
│  Moonrise   11:42 PM             │
│  Azimuth    112° ESE             │
│             (East-Southeast)     │
│                                  │
│  WEATHER                         │
│  ─────────────────────────────── │
│  Sky        ☀ Clear              │
│             Cloud cover 5%       │
│  Temp       38°F  Feels 30°F    │
│  Wind       8 mph N             │
│  Precip     0% chance           │
│                                  │
│  VERDICT                         │
│  ─────────────────────────────── │
│  ✓ Moon in phase window          │
│  ✗ Moonrise after 11:00 PM      │
│  ✓ Moonrise after sunset        │
│  ✓ Sky clear                     │
│                                  │
│       ◂ swipe between days ▸     │
└──────────────────────────────────┘
```

## Portrait Layout — Weather Unknown

```
┌──────────────────────────────────┐
│ ☰  Home — Seattle, WA           │
│┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄│
│░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│
│░░░░░░░░░░░ (dimmed) ░░░░░░░░░░░░░│
├──────────────────────────────────┤
│          ── drag handle ──       │
│                                  │
│  Thu, Mar 12, 2026      ● GOOD   │
│                   (weather TBD)  │
│                                  │
│  ASTRONOMICAL                    │
│  ─────────────────────────────── │
│  Sunset     6:12 PM              │
│  Moonrise   6:45 PM              │
│  Azimuth    96° E                │
│             (East)               │
│                                  │
│  WEATHER                         │
│  ─────────────────────────────── │
│                                  │
│    Weather forecast unavailable  │
│    for this date. Check back     │
│    closer to Feb 26.             │
│                                  │
│  VERDICT                         │
│  ─────────────────────────────── │
│  ✓ Moon in phase window          │
│  ✓ Moonrise after sunset        │
│  ✓ Moonrise before 11:00 PM     │
│  ? Sky clarity unknown           │
│                                  │
│       ◂ swipe between days ▸     │
└──────────────────────────────────┘
```

---

## Annotations

### Bottom Sheet Behavior

- Presented as a Material 3 `ModalBottomSheet`
- Drag handle at top, standard Material 3 styling
- Sheet covers approximately 75% of screen height (expandable to full)
- Main screen content behind the sheet is dimmed with a scrim
- Closing: swipe down, tap scrim, or system back gesture
- Sheet appears with a slide-up animation

### Header

- Full date format: `"EEE, MMM d, yyyy"` → e.g. "Thu, Feb 13, 2026"
- Left-aligned date, right-aligned verdict badge
- Verdict badge styling matches today section (green/red background, 4 dp rounded corners, 8×4 dp
  padding)
- Bad verdict includes parenthetical reason: "(too late)", "(before sunset)", "(weather)"
- Weather-unknown days with passing time constraints show "● GOOD" with "(weather TBD)" subtitle

### Section Headers

- Labels: "ASTRONOMICAL", "WEATHER", "VERDICT"
- Style: `labelLarge`, bold, `onSurfaceVariant`
- Separator line below each label: `HorizontalDivider`, 8 dp bottom padding
- Sections separated by 16 dp vertical spacing

### Astronomical Section

- Same field layout as today section: label + value pairs
- Labels: "Sunset", "Moonrise", "Azimuth"
- Azimuth shows expanded cardinal name on a second line: e.g. "(East-Southeast)"
- Expanded cardinal names: N=North, NNE=North-Northeast, NE=Northeast, ENE=East-Northeast,
  E=East, ESE=East-Southeast, SE=Southeast, SSE=South-Southeast, etc.
- Style: labels `bodyMedium` `onSurfaceVariant`, values `bodyMedium` medium weight
- 4 dp vertical spacing between rows

### Weather Section

- **Sky condition:** weather icon + label (same icons as main screen) plus cloud cover percentage
  on second line
- **Temperature row:** actual temp + windchill on same line, e.g. "42°F Feels 35°F"
- **Wind:** speed + cardinal direction, e.g. "12 mph NW"
- **Precipitation:** percentage chance + type, e.g. "10% chance rain", "0% chance"
- Units follow the selected unit system (Imperial/Metric per PRD 7.4)

### Weather Unknown State

- Shown for dates beyond the ~14-day weather forecast window
- Weather section displays centered informational message instead of data fields
- Message: "Weather forecast unavailable for this date. Check back closer to {date}."
- The check-back date is approximately 14 days before the forecast day
- Style: `bodyMedium`, `onSurfaceVariant`, centered within the weather section
- Verdict section shows `?` instead of `✓` or `✗` for sky clarity

### Verdict Section (Constraint Checklist)

- Shows each constraint with pass (`✓`) or fail (`✗`) indicator
- Constraints listed:
    1. "Moon in phase window" — always `✓` (only phase-window days are shown)
    2. "Moonrise after sunset" / "Moonrise before sunset" — checks PRD 2.2
    3. "Moonrise before {max time}" / "Moonrise after {max time}" — checks PRD 2.1
    4. "Sky clear" / "Sky mostly clear" / "Sky cloudy" / "Sky clarity unknown" — checks PRD 3.1–3.4
- Pass icon (`✓`): green, `bodyMedium`
- Fail icon (`✗`): red, `bodyMedium`, text also in `error` color
- Unknown icon (`?`): `onSurfaceVariant`, `bodyMedium`
- 4 dp vertical spacing between constraint rows
- The failing constraint is the reason shown in the header badge

### Swipe Navigation

- Swipe left/right to navigate between adjacent phase-window days
- Swipe indicators at bottom of sheet: `◂` and `▸` arrows with "swipe between days" hint text
- Hint text: `bodySmall`, `onSurfaceVariant`, centered, shown only on first open (then dismissed)
- Navigation wraps: first day has no left arrow, last day has no right arrow
- Content transitions with a horizontal slide animation
- Day order matches the forecast list order on the main screen

### Padding and Spacing

- Sheet horizontal padding: 24 dp
- Sheet top padding (below drag handle): 16 dp
- Sheet bottom padding: 24 dp (plus safe area inset)
- Between header and first section: 20 dp
- Between sections: 16 dp
- Between section header and first row: 8 dp
- Between data rows: 4 dp
