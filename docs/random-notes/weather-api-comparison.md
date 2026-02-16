# Weather API Comparison

**Date:** February 16, 2026

Evaluation of three candidates for PRD requirement 3.1 (14-day weather forecast with cloud coverage
data). Decision recorded in PRD Decision Log.

## Evaluation Criteria (from PRD)

- 14-day forecast support (req 3.1)
- Sky clarity categories: clear / partly cloudy / cloudy (req 3.2, for days 3-14)
- Detailed cloud coverage percentage (req 3.3, for days 0-2)
- Precipitation forecast (req 6.4.7)
- Horizon-specific cloud data (ideal but PRD acknowledges may not exist)
- Free tier sufficient for a personal/low-traffic app
- Location-based query (lat/lon)

## Comparison Table

| Criteria                   | OpenWeatherMap                  | WeatherAPI.com                              | Visual Crossing                            |
|----------------------------|---------------------------------|---------------------------------------------|--------------------------------------------|
| **14-day forecast (free)** | No -- 8 days max (One Call 3.0) | No -- 3 days max                            | Yes -- 15 days                             |
| **14-day forecast (paid)** | ~$40/mo (16-day endpoint)       | ~$8/mo (Professional)                       | N/A (free covers it)                       |
| **Cloud cover field**      | `clouds` (0-100%) + text        | `cloud` (0-100%) per hour                   | `cloudcover` (0-100%) daily + hourly       |
| **Hourly cloud data**      | First 48 hours only             | All forecast days                           | All forecast days                          |
| **Precipitation**          | `pop`, `rain`/`snow` amounts    | `chance_of_rain/snow`, amounts, types       | `precipprob`, `precip`, `preciptype` array |
| **Moon data in response**  | moonrise, moonset, moon_phase   | moonrise, moonset, moon_phase, illumination | moon_phase only (no moonrise)              |
| **Free tier call limit**   | 1,000/day                       | 1,000,000/month                             | 1,000 records/day (~66 calls for 15-day)   |
| **Credit card required**   | Yes (even for free)             | No                                          | No                                         |
| **Attribution required**   | Yes (CC BY-SA 4.0)              | Yes ("Powered by")                          | Verify -- free is personal/non-commercial  |

## Candidate Details

### OpenWeatherMap

- **One Call API 3.0** is the main product. Free tier gives 1,000 calls/day but only 8-day daily
  forecast (today + 7 days). A separate **16-Day Daily Forecast** endpoint exists but requires a
  paid subscription (~$40/mo Developer tier).
- Credit card is required even for free usage (you set a $0 spending cap).
- New API keys can take up to 2 hours to activate.
- Response includes `moonrise`, `moonset`, `moon_phase` in daily entries, plus hourly data for the
  first 48 hours with per-hour `clouds` values.
- License: CC BY-SA 4.0 on free tier (attribution required).

### WeatherAPI.com

- Free tier provides only 3 days of forecast. 14-day forecast requires the Professional plan
  (~$8/mo).
- No credit card required for free signup.
- Response has an `astro` object per forecast day with `moonrise`, `moonset`, `moon_phase`, and
  `moon_illumination` -- the richest moon data of the three.
- Hourly data includes `cloud` (0-100%) for all forecast days.
- Attribution required on free tier ("Powered by WeatherAPI.com").

### Visual Crossing

- **Timeline Weather API** returns 15-day forecast on the free tier. This is the only candidate
  that meets the 14-day PRD requirement without a paid plan.
- Free tier: 1,000 records/day (one record = one day of data for one location; a 15-day forecast =
  15 records, so ~66 calls/day possible).
- No credit card required for signup.
- Response includes `cloudcover` (0-100%) and `conditions` text at both daily and hourly
  granularity for all forecast days. Hourly data lets you check cloud cover specifically at moonrise
  time.
- Includes `moonphase` (0-1 scale, 0.5 = full moon), `sunrise`, `sunset` -- but **no moonrise or
  moonset times**. An astronomy library is needed for those.
- Precipitation fields: `precipprob`, `precip` (amount), `preciptype` (array of types), `snow`,
  `snowdepth`.
- Free tier is personal/non-commercial. Commercial use (e.g., Play Store distribution) requires a
  paid plan (~$35/mo Professional).

## Decision

**Visual Crossing** selected. Primary reason: only candidate with 14-day forecast on the free tier.

Trade-offs accepted:

- No moonrise/moonset times in response -- astronomy library needed regardless (for azimuth).
- Free tier is non-commercial -- acceptable for Phase 1 MVP; license review needed before Play
  Store publication.

## Caveat

This analysis is based on AI training data (cutoff May 2025). Pricing and terms should be verified
against current documentation before implementation:

- https://www.visualcrossing.com/pricing
- https://openweathermap.org/price
- https://www.weatherapi.com/pricing.aspx
