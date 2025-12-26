# Weather Forecast App

An Android weather forecast application built with Clean Architecture and Jetpack Compose, providing current weather, 7-day forecasts, and location-based weather information.

## Overview

This app allows users to:
- View current day weather forecast
- Check weekly (7-day) weather forecasts
- Browse and search cities from a predefined list
- Add custom cities via search
- Get weather for current location automatically
- Access cached weather data offline

The app uses the OpenWeatherMap API to fetch real-time weather data and implements an offline-first caching strategy to ensure data availability even without network connectivity.

## Documentation

For detailed implementation plan and architecture, see [docs/ai/plan.md](docs/ai/plan.md)

🤖 This project is developed with [Claude Code](https://claude.com/claude-code), Anthropic's official CLI for AI-assisted software development. Claude Code helps with implementing Clean Architecture structure, writing Android components following best practices, setting up dependencies and configurations, creating UI with Jetpack Compose, and implementing repository patterns and use cases.

## Tech Stack

### Core
- **Kotlin** - Programming language
- **Jetpack Compose BOM 2024.12.00** - Modern UI toolkit
- **Material3** - Material Design components
- **Clean Architecture** - Separation of concerns

### Networking
- **Retrofit 2.11.0** - HTTP client
- **Gson Converter** - JSON serialization
- **OkHttp** - HTTP client implementation

### Database & Persistence
- **Room 2.6.1** - Local database
- **DataStore Preferences 1.1.1** - Key-value storage

### Dependency Injection
- **Hilt 2.52** - Dependency injection framework

### Location Services
- **Google Play Services Location 21.3.0** - Location provider

### Image Loading
- **Coil Compose 2.7.0** - Image loading library

### Asynchronous Programming
- **Kotlin Coroutines** - Asynchronous operations
- **Flow** - Reactive streams

### Navigation
- **Compose Navigation** - In-app navigation

## Architecture

The app follows Clean Architecture principles with three main layers:

- **Data Layer** - API services, Room database, repositories
- **Domain Layer** - Business logic, use cases, models
- **Presentation Layer** - Jetpack Compose UI, ViewModels

## Modularization

The project uses a multi-module architecture to improve build performance, code organization, and scalability.

### Module Structure

```
weather_forecast_app/
├── :app                    # Application entry point
├── :core                   # Shared infrastructure
├── :domain                 # Business logic layer
├── :data                   # Data implementation layer
└── :feature                # Presentation layer
```

### Module Details

#### :app
- **Purpose**: Application entry point and navigation setup
- **Contents**: `MainActivity`, `WeatherApp` (Application class)
- **Dependencies**: All other modules

#### :core
- **Purpose**: Shared infrastructure and utilities
- **Contents**:
  - Network layer (Retrofit, API services, DTOs)
  - Database layer (Room, DAOs, Entities)
  - Location services
  - UI components and theme
  - Utility classes
- **Dependencies**: `:domain` (for repository interfaces)

#### :domain
- **Purpose**: Business logic and domain models
- **Contents**:
  - Domain models (Weather, City, DailyForecast, Location)
  - Repository interfaces
  - Use cases (8 use cases)
- **Dependencies**: None (pure Kotlin)

#### :data
- **Purpose**: Data layer implementation
- **Contents**:
  - Repository implementations
  - Data mappers (DTO ↔ Entity ↔ Domain)
  - DI modules for repositories
- **Dependencies**: `:core`, `:domain`

#### :feature
- **Purpose**: Presentation layer
- **Contents**:
  - UI screens (Home, CityList, CitySearch, WeeklyForecast)
  - ViewModels and UI states
  - Navigation graph
- **Dependencies**: `:core`, `:domain`

### Module Dependency Graph

```
:app
├── :core ──────> :domain
├── :domain
├── :data ──────> :core, :domain
└── :feature ───> :core, :domain
```

## API

- **Provider**: OpenWeatherMap
- **Plan**: Free Plan (60 calls/min, 1M calls/month)
- **Endpoints**: Current weather, 5-day forecast, Geocoding

## Features

- Offline-first architecture with 30-minute cache TTL
- Auto-location with fallback to city selection
- Dark mode support
- Material3 design system
- Reactive UI updates using Flow
