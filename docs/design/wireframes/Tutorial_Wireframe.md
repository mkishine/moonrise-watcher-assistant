# Tutorial Wireframe

**Related Requirements:** General UX | User Stories: US-002

---

## Screen Elements

| Region      | Element              | Source | Notes                                                   |
|-------------|----------------------|--------|---------------------------------------------------------|
| Top bar     | Title "How It Works" | —      | Centered; no back arrow                                 |
| Top bar     | Skip button          | US-002 | `TextButton`, top-right; exits tutorial immediately     |
| Content     | Card title           | US-002 | `headlineSmall`, centered                               |
| Content     | Card body            | US-002 | `bodyLarge`, `onSurfaceVariant`, centered               |
| Footer      | Page indicator dots  | US-002 | Three dots; filled = current page                       |
| Footer      | Next / Done button   | US-002 | `FilledButton`; "Next" on pages 1–2, "Done" on page 3   |
| Entry point | "How it works" link  | US-002 | `TextButton` on the first-time screen, below Add button |

---

## Card Content

### Card 1 — What is Moonrise Watcher?

> Moonrise Watcher helps you find the best nights to watch the moon rise.
>
> A great moonrise happens when the moon is full and bright, rises at a reasonable hour, and the
> sky is clear. Those conditions don't line up every night — this app finds the nights when they do.

### Card 2 — What Makes a Good Night?

> Three things need to line up:
>
> **Phase** — The moon should be near full, so it's large and bright. The app shows nights within a
> few days of each full moon.
>
> **Timing** — The moon should rise after sunset (so the sky is dark enough to see it) and before
> your bedtime. Both limits are adjustable in Settings.
>
> **Weather** — Clear or partly cloudy skies give you the best view. Cloudy nights are marked as
> unfavorable.

### Card 3 — Reading the Forecast

> The main screen shows tonight at the top, followed by upcoming nights in the next few months.
>
> A **green badge** means all three conditions are met — it's a good night to go out.
>
> A **red badge** shows which condition isn't met, such as "too late" or "weather."
>
> Tap any night for detailed weather: temperature, wind, precipitation, and the exact compass
> direction the moon will rise from.

---

## Portrait Layout — Card 1

```
┌──────────────────────────────────┐
│     How It Works          [Skip] │  ← top bar
├──────────────────────────────────┤
│                                  │
│                                  │
│                                  │
│                                  │
│         ☽                        │
│                                  │
│   What is Moonrise Watcher?      │
│                                  │
│   Moonrise Watcher helps you     │
│   find the best nights to watch  │
│   the moon rise.                 │
│                                  │
│   A great moonrise happens when  │
│   the moon is full and bright,   │
│   rises at a reasonable hour,    │
│   and the sky is clear. Those    │
│   conditions don't line up every │
│   night — this app finds the     │
│   nights when they do.           │
│                                  │
│                                  │
│            ●  ○  ○               │  ← page dots
│                                  │
│            [    Next    ]        │
│                                  │
└──────────────────────────────────┘
```

## Portrait Layout — Card 2

```
┌──────────────────────────────────┐
│     How It Works          [Skip] │
├──────────────────────────────────┤
│                                  │
│                                  │
│   What Makes a Good Night?       │
│                                  │
│   Three things need to line up:  │
│                                  │
│   Phase — The moon should be     │
│   near full, so it's large and   │
│   bright. The app shows nights   │
│   within a few days of each      │
│   full moon.                     │
│                                  │
│   Timing — The moon should rise  │
│   after sunset and before your   │
│   bedtime. Both limits are       │
│   adjustable in Settings.        │
│                                  │
│   Weather — Clear or partly      │
│   cloudy skies give you the      │
│   best view.                     │
│                                  │
│            ○  ●  ○               │
│                                  │
│            [    Next    ]        │
│                                  │
└──────────────────────────────────┘
```

## Portrait Layout — Card 3

```
┌──────────────────────────────────┐
│     How It Works          [Skip] │
├──────────────────────────────────┤
│                                  │
│                                  │
│   Reading the Forecast           │
│                                  │
│   The main screen shows tonight  │
│   at the top, followed by        │
│   upcoming nights in the next     │
│   few months.                    │
│                                  │
│   ● GOOD  A green badge means    │
│   all three conditions are met   │
│   — it's a good night to go out. │
│                                  │
│   ● BAD (too late)  A red badge  │
│   shows which condition isn't    │
│   met.                           │
│                                  │
│   Tap any night for detailed     │
│   weather and the exact compass  │
│   direction of moonrise.         │
│                                  │
│            ○  ○  ●               │
│                                  │
│            [    Done    ]        │
│                                  │
└──────────────────────────────────┘
```

---

## Entry Point — First-Time Screen

```
┌──────────────────────────────────┐
│       Set Up Location            │
├──────────────────────────────────┤
│                                  │
│              ☽                   │
│                                  │
│   Welcome to Moonrise Watcher    │
│                                  │
│   [ + Add Your First Location ]  │  ← FilledButton
│                                  │
│       [ How it works ]           │  ← TextButton
│                                  │
└──────────────────────────────────┘
```

---

## Annotations

### Navigation and Entry Points

- Triggered from the first-time screen via the "How it works" `TextButton`
- Also accessible from the About screen via a "How It Works" navigation row
- On completion (Done or Skip): pops back to the calling screen
- System back behaves the same as Skip

### Top Bar

- `CenterAlignedTopAppBar`, title "How It Works"
- No back arrow (Skip button replaces it)
- Skip: `TextButton`, `primary` color, right-aligned in the top bar actions slot
- Skip navigates back immediately regardless of current page

### Paging

- Material 3 `HorizontalPager` (from `accompanist-pager` or Compose Foundation `Pager`)
- Three pages; swipe left/right to navigate
- Page indicator: three `Icon` dots, filled dot for current page, outlined for others

### Card Layout

- Cards are not Material `Card` components — the page content fills the screen area below the top
  bar without a card elevation or border
- Screen horizontal padding: 24 dp
- Card title: `headlineSmall`, centered, `onSurface`
- Card body: `bodyLarge`, `onSurfaceVariant`, left-aligned (centered in Card 1 only)
- Card 1 moon icon (`☽`): `displaySmall`, centered, 24 dp above title
- Bold inline text in Cards 2 and 3 (Phase, Timing, Weather; GOOD badge, BAD badge): rendered with
  `SpanStyle(fontWeight = FontWeight.Bold)` inside an `AnnotatedString`
- Minimum 32 dp between last body line and page dots

### Footer

- Page indicator dots centered horizontally, 16 dp above the button
- Next / Done button: `FilledButton`, full width minus 48 dp horizontal padding, 48 dp height
- Button label: "Next" on pages 1 and 2; "Done" on page 3
- Tapping Next advances to the next page (same as swiping)
- Tapping Done on page 3 navigates back (same as Skip)

### Padding and Spacing

- Screen horizontal padding: 24 dp
- Top of content area to card title (or moon icon): 32 dp
- Between card title and body: 20 dp
- Footer bottom padding: 24 dp + window inset
