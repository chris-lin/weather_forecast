# Weather Forecast App - Implementation Plan

## Project Overview
Build a weather forecast Android app using Clean Architecture and Jetpack Compose with OpenWeatherMap API.

## Requirements
- Display current day weather forecast
- Display weekly (7-day) weather forecast
- City list with predefined cities + search functionality
- Auto-location feature
- Cache weather data for offline access
- Use OpenWeatherMap API (Free Plan)
- Jetpack Compose UI
- Clean Architecture

## Architecture Structure

```
app/src/main/java/com/example/weatherforecast/
├── data/
│   ├── local/          # Room database, DAOs, entities
│   ├── remote/         # API service, DTOs
│   ├── repository/     # Repository implementations
│   └── mapper/         # Data mappers
├── domain/
│   ├── model/          # Domain models
│   ├── repository/     # Repository interfaces
│   └── usecase/        # Use cases
├── presentation/
│   ├── ui/             # Compose screens & ViewModels
│   ├── navigation/     # Navigation graph
│   └── theme/          # Compose theme
├── di/                 # Hilt modules
└── util/               # Utilities
```

## Implementation Phases

### Phase 1: Project Setup & Dependencies
**Files to modify:**
- `gradle/libs.versions.toml` - Add all dependency versions
- `build.gradle.kts` (root) - Add Hilt and KSP plugins
- `app/build.gradle.kts` - Add dependencies and enable buildConfig
- `local.properties` - Add OPENWEATHER_API_KEY
- `app/src/main/AndroidManifest.xml` - Add permissions and Application class

**Dependencies to add:**
- Jetpack Compose BOM (2024.12.00)
- Compose Material3, Navigation, Lifecycle
- Retrofit 2.11.0 + Gson converter
- Room 2.6.1 + KTX
- Hilt 2.52
- Google Play Services Location 21.3.0
- Coil Compose 2.7.0
- DataStore Preferences 1.1.1
- Coroutines

**Application class:**
- `app/src/main/java/com/example/weatherforecast/WeatherApp.kt` with @HiltAndroidApp

### Phase 2: Data Layer - Remote

**API Service:**
- `data/remote/api/WeatherApiService.kt` - Retrofit interface
  - getCurrentWeather(cityId)
  - getCurrentWeatherByCoordinates(lat, lon)
  - getForecast(cityId)
  - searchCities(query) - Geocoding API
  - getCityByCoordinates(lat, lon)

**DTOs:**
- `data/remote/dto/CurrentWeatherDto.kt`
- `data/remote/dto/ForecastDto.kt`
- `data/remote/dto/GeocodingDto.kt`
- `data/remote/dto/CommonDto.kt` - Shared components (CoordDto, WeatherDto, MainDto, etc.)

**DI Module:**
- `di/NetworkModule.kt` - Provide Retrofit, OkHttp, ApiService

### Phase 3: Data Layer - Local

**Entities:**
- `data/local/entity/WeatherEntity.kt` - Current weather cache
- `data/local/entity/DailyForecastEntity.kt` - Daily forecast cache
- `data/local/entity/CityEntity.kt` - Cities (predefined + user-added)

**DAOs:**
- `data/local/dao/WeatherDao.kt`
- `data/local/dao/DailyForecastDao.kt`
- `data/local/dao/CityDao.kt`

**Database:**
- `data/local/database/WeatherDatabase.kt` - Room database with 3 entities

**Predefined Data:**
- `data/local/PredefinedCities.kt` - Initial city list (London, Tokyo, etc.)

**DI Module:**
- `di/DatabaseModule.kt` - Provide Database and DAOs

### Phase 4: Domain Layer

**Models:**
- `domain/model/Weather.kt` - Domain weather model
- `domain/model/DailyForecast.kt` - Domain forecast model
- `domain/model/City.kt` - Domain city model
- `domain/model/Location.kt` - Lat/lon wrapper

**Repository Interfaces:**
- `domain/repository/WeatherRepository.kt`
- `domain/repository/CityRepository.kt`
- `domain/repository/LocationRepository.kt`

**Use Cases:**
- `domain/usecase/GetCurrentWeatherUseCase.kt`
- `domain/usecase/GetWeeklyForecastUseCase.kt`
- `domain/usecase/GetCurrentLocationWeatherUseCase.kt`
- `domain/usecase/GetCitiesUseCase.kt`
- `domain/usecase/SearchCitiesUseCase.kt`
- `domain/usecase/AddCityUseCase.kt`
- `domain/usecase/RefreshWeatherUseCase.kt`

### Phase 5: Data Layer - Repository Implementation

**Mappers:**
- `data/mapper/WeatherMapper.kt` - DTO ↔ Entity ↔ Domain
- `data/mapper/ForecastMapper.kt` - Aggregate 3-hour forecast to daily
- `data/mapper/CityMapper.kt`

**Repository Implementations:**
- `data/repository/WeatherRepositoryImpl.kt` - Offline-first strategy
- `data/repository/CityRepositoryImpl.kt`
- `data/repository/LocationRepositoryImpl.kt` - FusedLocationProviderClient

**DI Modules:**
- `di/LocationModule.kt` - Provide FusedLocationProviderClient
- `di/RepositoryModule.kt` - Bind repository interfaces to implementations

**Cache Strategy:**
- 30-minute TTL for weather data
- Return cached data immediately + fetch fresh in background
- Update cache when API succeeds

### Phase 6: Presentation Layer - Theme & Components

**Theme:**
- `presentation/theme/Color.kt`
- `presentation/theme/Type.kt`
- `presentation/theme/Theme.kt` - Material3 theme with dark mode

**Reusable Components:**
- `presentation/ui/components/WeatherCard.kt`
- `presentation/ui/components/ForecastItem.kt`
- `presentation/ui/components/CityItem.kt`
- `presentation/ui/components/LoadingIndicator.kt`
- `presentation/ui/components/ErrorView.kt`
- `presentation/ui/components/WeatherIcon.kt` - Load from OpenWeatherMap
- `presentation/ui/components/SearchBar.kt`

**Navigation:**
- `presentation/navigation/Screen.kt` - Sealed class for routes
- `presentation/navigation/NavGraph.kt` - NavHost setup

### Phase 7: Presentation Layer - Screens

**Home Screen:**
- `presentation/ui/home/HomeScreen.kt` - Current weather + quick forecast
- `presentation/ui/home/HomeViewModel.kt` - @HiltViewModel
- `presentation/ui/home/HomeUiState.kt` - Sealed interface (Loading/Success/Error)

**City List Screen:**
- `presentation/ui/citylist/CityListScreen.kt`
- `presentation/ui/citylist/CityListViewModel.kt`
- `presentation/ui/citylist/CityListUiState.kt`

**City Search Screen:**
- `presentation/ui/citysearch/CitySearchScreen.kt`
- `presentation/ui/citysearch/CitySearchViewModel.kt`
- `presentation/ui/citysearch/CitySearchUiState.kt`

**Weekly Forecast Screen:**
- `presentation/ui/weeklyforecast/WeeklyForecastScreen.kt`
- `presentation/ui/weeklyforecast/WeeklyForecastViewModel.kt`
- `presentation/ui/weeklyforecast/WeeklyForecastUiState.kt`

### Phase 8: Main Activity & Navigation

**MainActivity:**
- `MainActivity.kt` - Set up Compose with NavHost
- Request location permission
- Initialize navigation

### Phase 9: Utilities

**Utility Classes:**
- `util/DateTimeUtil.kt` - Format timestamps, dates
- `util/TemperatureUtil.kt` - Temperature formatting
- `util/WeatherIconUtil.kt` - Icon URL builder
- `util/PermissionUtil.kt` - Permission helpers
- `util/NetworkUtil.kt` - Connectivity check
- `util/ApiException.kt` - Custom exceptions
- `util/Result.kt` - Result wrapper (if needed)

### Phase 10: Data Initialization

**First Launch:**
- Populate predefined cities in Room database
- Use DataStore to track first launch
- Clean up old cached data (>7 days)

## Key Implementation Details

### OpenWeatherMap API
- Base URL: `https://api.openweathermap.org`
- Current weather: `GET /data/2.5/weather`
- 5-day forecast: `GET /data/2.5/forecast` (3-hour intervals)
- Geocoding: `GET /geo/1.0/direct` and `/geo/1.0/reverse`
- Units: metric (Celsius)
- Free plan: 60 calls/min, 1M calls/month

### API Key Management
```kotlin
// local.properties
OPENWEATHER_API_KEY=your_api_key_here

// build.gradle.kts
buildConfigField("String", "OPENWEATHER_API_KEY", "\"${properties.getProperty("OPENWEATHER_API_KEY")}\"")
```

### Permissions (AndroidManifest.xml)
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

### Weekly Forecast Aggregation
- Free API returns 5-day/3-hour forecast (40 points)
- Group by date (extract date from dt_txt)
- Calculate min/max temp per day
- Select most common weather condition
- Take first 7 days for weekly forecast

### Error Handling
- Network errors: Show cached data + error message
- Location denied: Fall back to city selection
- API rate limit: Use cached data, show retry option
- Empty cache: Show error with retry

### Offline-First Strategy
1. Observe cached data from Room (Flow)
2. Check if cache is stale (>30 min)
3. If stale, fetch from API in background
4. Update cache on success
5. UI automatically updates via Flow

## Critical Files Summary

**Configuration (5 files):**
- gradle/libs.versions.toml
- build.gradle.kts (root)
- app/build.gradle.kts
- local.properties
- app/src/main/AndroidManifest.xml

**Core Architecture (5 files):**
- WeatherApp.kt
- data/remote/api/WeatherApiService.kt
- data/local/database/WeatherDatabase.kt
- data/repository/WeatherRepositoryImpl.kt
- presentation/ui/home/HomeViewModel.kt

**Total Files: ~80**

## Testing Strategy
- Manual testing of all user flows
- Test offline mode (airplane mode)
- Test location permission grant/deny
- Test API error scenarios (disconnect, invalid key)
- Test cache expiration and refresh
- Test city search and selection

## Notes
- No comments in code (per user config)
- No unit tests (per user config)
- Use aggressive caching to respect API limits
- Weather icons from: `https://openweathermap.org/img/wn/{icon}@2x.png`
- Predefined cities: London, New York, Tokyo, Paris, Beijing, Sydney, Moscow, Rio
