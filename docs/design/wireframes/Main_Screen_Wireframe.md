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

- `☰` — hamburger menu icon
- `●` / `○` — good (filled green) / bad (hollow gray or red) indicator
- `☼` — sunset, `☽` — moonrise
- `☀` clear, `⛅` partly cloudy, `☁` cloudy, `?` weather unknown
- Today section is visually separated from the forecast list by a divider
- In landscape, today occupies roughly one-third of the width; the forecast list fills the rest
- The forecast list scrolls vertically; the today section stays fixed
- Today section should not consume more than ~40% of screen height (per US-003)
