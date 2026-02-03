# Moonrise Watching App

An Android application designed to help users identify optimal nights for watching moonrises by consolidating moon phase data, moonrise timing, and weather forecasts into a single, easy-to-use interface.

## Project Status

🚧 **Currently in Design Phase**

- ✅ Requirements gathering complete
- ✅ Product Requirements Document complete
- ✅ User stories defined
- ✅ User flows documented
- 🔄 Next: Wireframes and UI design
- ⏳ Development not yet started

## Documentation

### Requirements
- [Product Requirements Document (PRD)](docs/requirements/Moonrise_App_PRD.md) - Complete feature requirements and specifications

### Design
- [User Stories](docs/design/user-stories/User_Stories.md) - User-focused feature descriptions with acceptance criteria
- [User Flows](docs/design/user-flows/User_Flows.md) - Visual flowcharts showing user navigation and interactions

### Development
- Coming soon: API decisions, architecture documentation, setup guides

### Testing
- Coming soon: Test plans and testing documentation

## Quick Overview

### What Problem Does This Solve?

Watching moonrise requires three conditions to align:
1. Moon must be near full phase (visually impressive)
2. Moon must rise at a reasonable hour (before bedtime)
3. Sky must be clear, especially at the horizon

Currently, users must check multiple sources and manually correlate this information. This app consolidates everything into one place.

### Key Features (Planned)

- **14-day moonrise forecast** with clear good/bad night indicators
- **Weather integration** showing sky clarity, temperature, wind, and precipitation
- **Configurable bedtime constraint** (default: 11 PM)
- **Multiple saved locations** for travelers and multi-site observers
- **Azimuth display** showing compass direction of moonrise
- **At-a-glance interface** requiring minimal interaction

### Target Platform

- Android (native development)
- Minimum API level: TBD during development phase

## Project Structure

```
moonrise-watcher-assistant/
├── README.md                     # This file
├── docs/                         # All project documentation
│   ├── requirements/             # Requirements documentation
│   │   └── Moonrise_App_PRD.md  # Product Requirements Document
│   ├── design/                   # Design documentation
│   │   ├── user-stories/         # User stories
│   │   │   └── User_Stories.md
│   │   ├── user-flows/           # User flow diagrams
│   │   │   └── User_Flows.md
│   │   ├── wireframes/           # Wireframe mockups (coming soon)
│   │   ├── ui-mockups/           # High-fidelity UI designs (coming soon)
│   │   └── architecture/         # Technical architecture docs (coming soon)
│   ├── development/              # Development documentation
│   │   ├── api-decisions.md      # Decision log for API choices (coming soon)
│   │   └── setup-guide.md        # Developer setup instructions (coming soon)
│   └── testing/                  # Testing documentation
│       └── test-plans.md         # Test plans and strategies (coming soon)
├── src/                          # Source code (coming soon)
└── assets/                       # Images, icons, resources (coming soon)
```

## Development Phases

### Phase 1: MVP (Minimum Viable Product)
**Goal:** Core functionality for single-location moonrise forecasting

**Features:**
- Manual location entry
- 14-day forecast with moonrise times, sunset times, and weather
- Good/bad day indicators based on phase and time constraints
- List view with at-a-glance information
- Detail view with expanded weather information

### Phase 2: Enhancement
**Goal:** Multi-location support and refinements

**Features:**
- Multiple saved locations with management interface
- Configurable maximum moonrise time per location
- Improved weather visualization
- Performance optimizations and bug fixes

### Future Considerations
- User feedback mechanism to improve forecast accuracy
- Historical viewing log
- Additional weather data sources for horizon clarity
- Home screen widget
- Metric/Imperial unit toggle

## Technology Stack (Planned)

### Development
- **Platform:** Android (native)
- **Language:** Kotlin (TBD)
- **Astronomical Calculations:** Library TBD (candidates: SunCalc, custom algorithms)
- **Weather API:** TBD (candidates: OpenWeatherMap, WeatherAPI, Visual Crossing)

### Data Storage
- Local storage for saved locations and preferences
- Weather forecast caching with appropriate TTL

## Getting Started

### For Developers
*Development setup instructions will be added when development begins.*

### For Designers
Review the following documents in order:
1. [PRD](docs/requirements/Moonrise_App_PRD.md) - Understand the requirements
2. [User Stories](docs/design/user-stories/User_Stories.md) - Understand user needs
3. [User Flows](docs/design/user-flows/User_Flows.md) - Understand navigation and interactions

Next step: Create wireframes based on user flows.

### For Stakeholders
Start with the [Product Requirements Document](docs/requirements/Moonrise_App_PRD.md) for a complete overview of the project goals, features, and success metrics.

## Design Principles

Based on our requirements and user research:

1. **Today First** - Most important information (tonight's conditions) displayed prominently
2. **Minimal Interaction** - Common tasks require minimal taps
3. **Visual Clarity** - Good nights stand out immediately with clear visual indicators
4. **No Clutter** - At-a-glance view shows only essential information
5. **Progressive Disclosure** - Detailed information available on demand

## Contributing

*Contribution guidelines will be added if/when the project opens to external contributors.*

## Feedback Loop

This is a living project. Documentation will evolve through:
- Design phase (wireframes may reveal gaps in requirements)
- Development phase (technical constraints may require adjustments)
- Testing phase (usability findings may update requirements)
- Real-world usage (user feedback drives Phase 2 priorities)

See the [Feedback Loop Strategy](docs/requirements/Moonrise_App_PRD.md#feedback-loop-strategy) section in the PRD for details.

## Decision Log

Major design and technical decisions will be documented in `docs/development/` with:
- Date of decision
- Options considered
- Decision made
- Rationale

## App Versions

Development has not yet begun.

## Contact

*Contact information to be added*

## License

*License to be determined*

---

**Note:** This README will be updated as the project progresses through design, development, and deployment phases.
