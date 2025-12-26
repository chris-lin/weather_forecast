# Weather App Multi-Module Implementation Plan

## Target Architecture

Refactor the existing single `:app` module into 5 modules:

```
:app (Application entry point)
:feature (Presentation layer - All UI)
:data (Data implementation layer)
:domain (Business logic layer)
:core (Core infrastructure)
```

## Module Responsibilities and Contents

### 1. `:core` - Core Infrastructure Module

**Responsibility:** Provide shared infrastructure, utilities, networking, database, location services, and UI components

**Contents:**
- **Network Layer**
  - `data/remote/api/WeatherApiService.kt`
  - `data/remote/dto/*.kt` (All DTOs)
  - `di/NetworkModule.kt`

- **Database Layer**
  - `data/local/database/WeatherDatabase.kt`
  - `data/local/dao/*.kt` (All DAOs)
  - `data/local/entity/*.kt` (All Entities)
  - `data/local/DatabaseInitializer.kt`
  - `data/local/PredefinedCities.kt`
  - `di/DatabaseModule.kt`

- **Location Services**
  - `data/repository/LocationRepositoryImpl.kt`
  - `di/LocationModule.kt`

- **UI Components and Theme**
  - `presentation/theme/*.kt` (Color, Theme, Type)
  - `presentation/ui/components/*.kt` (All 9 shared components)

- **Utilities**
  - `util/*.kt` (All 6 utility classes)

- **DI**
  - `di/AppModule.kt` (Context and Dispatchers)

**Dependencies:** None (Leaf module)

**Gradle Configuration:**
```kotlin
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.weatherforecast.core"
    compileSdk = 36

    defaultConfig {
        minSdk = 28

        val properties = Properties()
        val localPropertiesFile = project.rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            properties.load(localPropertiesFile.inputStream())
        }
        buildConfigField(
            "String",
            "OPENWEATHER_API_KEY",
            "\"${properties.getProperty("OPENWEATHER_API_KEY", "")}\""
        )
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // AndroidX Core
    implementation(libs.androidx.core.ktx)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.coil.compose)

    // Networking
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Location
    implementation(libs.play.services.location)

    // DI
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)
}
```

---

### 2. `:domain` - Business Logic Layer

**Responsibility:** Define business models, Repository interfaces, and Use Cases

**Contents:**
- **Domain Models**
  - `domain/model/Weather.kt`
  - `domain/model/DailyForecast.kt`
  - `domain/model/City.kt`
  - `domain/model/Location.kt`

- **Repository Interfaces**
  - `domain/repository/WeatherRepository.kt`
  - `domain/repository/CityRepository.kt`
  - `domain/repository/LocationRepository.kt`

- **Use Cases** (All 8)
  - `domain/usecase/GetCurrentWeatherUseCase.kt`
  - `domain/usecase/GetWeeklyForecastUseCase.kt`
  - `domain/usecase/GetCurrentLocationWeatherUseCase.kt`
  - `domain/usecase/GetCitiesUseCase.kt`
  - `domain/usecase/SearchCitiesUseCase.kt`
  - `domain/usecase/AddCityUseCase.kt`
  - `domain/usecase/RemoveCityUseCase.kt`
  - `domain/usecase/RefreshWeatherUseCase.kt`

**Dependencies:** None (Pure Kotlin, no Android dependencies)

**Gradle Configuration:**
```kotlin
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.weatherforecast.domain"
    compileSdk = 36

    defaultConfig {
        minSdk = 28
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.javax.inject)
}
```

---

### 3. `:data` - Data Implementation Layer

**Responsibility:** Implement Repositories and data mapping

**Contents:**
- **Repository Implementations**
  - `data/repository/WeatherRepositoryImpl.kt`
  - `data/repository/CityRepositoryImpl.kt`

- **Data Mappers**
  - `data/mapper/WeatherMapper.kt`
  - `data/mapper/ForecastMapper.kt`
  - `data/mapper/CityMapper.kt`

- **DI**
  - `di/RepositoryModule.kt`

**Dependencies:** `:core`, `:domain`

**Gradle Configuration:**
```kotlin
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.example.weatherforecast.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 28
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.kotlinx.coroutines.android)
}
```

---

### 4. `:feature` - Presentation Layer

**Responsibility:** All UI screens, ViewModels, and navigation

**Contents:**
- **Home Screen**
  - `presentation/ui/home/HomeScreen.kt`
  - `presentation/ui/home/HomeViewModel.kt`
  - `presentation/ui/home/HomeUiState.kt`

- **CityList Screen**
  - `presentation/ui/citylist/CityListScreen.kt`
  - `presentation/ui/citylist/CityListViewModel.kt`
  - `presentation/ui/citylist/CityListUiState.kt`

- **CitySearch Screen**
  - `presentation/ui/citysearch/CitySearchScreen.kt`
  - `presentation/ui/citysearch/CitySearchViewModel.kt`
  - `presentation/ui/citysearch/CitySearchUiState.kt`

- **WeeklyForecast Screen**
  - `presentation/ui/weeklyforecast/WeeklyForecastScreen.kt`
  - `presentation/ui/weeklyforecast/WeeklyForecastViewModel.kt`
  - `presentation/ui/weeklyforecast/WeeklyForecastUiState.kt`

- **Navigation**
  - `presentation/navigation/Screen.kt`
  - `presentation/navigation/NavGraph.kt`

**Dependencies:** `:core`, `:domain`

**Gradle Configuration:**
```kotlin
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.weatherforecast.feature"
    compileSdk = 36

    defaultConfig {
        minSdk = 28
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
}
```

---

### 5. `:app` - Application Entry Point

**Responsibility:** Application entry point, Application class, and MainActivity

**Contents:**
- `MainActivity.kt`
- `WeatherApp.kt` (Application class)
- `AndroidManifest.xml`
- Resources (strings, themes, etc.)

**Dependencies:** `:core`, `:domain`, `:data`, `:feature`

**Gradle Configuration:** Keep existing configuration but update dependencies to:
```kotlin
dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":feature"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity.compose)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.datastore.preferences)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
```

---

## Module Dependency Graph

```
:app
├── :core (Leaf module)
├── :domain (Pure Kotlin)
├── :data
│   ├── :core
│   └── :domain
└── :feature
    ├── :core
    └── :domain
```

---

## Implementation Steps (Phase-by-Phase Execution)

### Phase 1: Create Core Module

1. Create `core/` directory structure
2. Create `core/build.gradle.kts`
3. Move the following files from `:app` to `:core`:
   - `data/remote/` entire directory
   - `data/local/` entire directory
   - `data/repository/LocationRepositoryImpl.kt`
   - `presentation/theme/` entire directory
   - `presentation/ui/components/` entire directory
   - `util/` entire directory
   - `di/AppModule.kt`, `di/NetworkModule.kt`, `di/DatabaseModule.kt`, `di/LocationModule.kt`
4. Update `settings.gradle.kts` to include `:core`
5. Update `:app/build.gradle.kts` to add `implementation(project(":core"))`
6. Update all import paths
7. Modify `NetworkModule.kt` to use `com.example.weatherforecast.core.BuildConfig`
8. **Verification:** Compile and run the app to ensure it works properly

### Phase 2: Create Domain Module

1. Create `domain/` directory structure
2. Create `domain/build.gradle.kts`
3. Move the following files from `:app` to `:domain`:
   - `domain/model/` entire directory
   - `domain/repository/` entire directory (interfaces)
   - `domain/usecase/` entire directory
4. Update `settings.gradle.kts` to include `:domain`
5. Update `:app/build.gradle.kts` to add `implementation(project(":domain"))`
6. Update all import paths
7. **Verification:** Compile and run the app to ensure it works properly

### Phase 3: Create Data Module

1. Create `data/` directory structure
2. Create `data/build.gradle.kts`
3. Move the following files from `:app` to `:data`:
   - `data/repository/WeatherRepositoryImpl.kt`
   - `data/repository/CityRepositoryImpl.kt`
   - `data/mapper/` entire directory
   - `di/RepositoryModule.kt`
4. Update `settings.gradle.kts` to include `:data`
5. Update `:app/build.gradle.kts` to add `implementation(project(":data"))`
6. Update `:data/build.gradle.kts` to add dependencies on `:core` and `:domain`
7. Update all import paths
8. **Verification:** Compile and run the app to ensure it works properly

### Phase 4: Create Feature Module

1. Create `feature/` directory structure
2. Create `feature/build.gradle.kts`
3. Move the following files from `:app` to `:feature`:
   - `presentation/ui/home/` entire directory
   - `presentation/ui/citylist/` entire directory
   - `presentation/ui/citysearch/` entire directory
   - `presentation/ui/weeklyforecast/` entire directory
   - `presentation/navigation/` entire directory
4. Update `settings.gradle.kts` to include `:feature`
5. Update `:app/build.gradle.kts` to add `implementation(project(":feature"))`
6. Update `:feature/build.gradle.kts` to add dependencies on `:core` and `:domain`
7. Update all import paths
8. Update navigation references in `MainActivity.kt`
9. **Verification:** Compile and run the app to ensure it works properly

### Phase 5: Clean Up App Module

1. Delete all moved directories and files from `:app`
2. Keep:
   - `MainActivity.kt`
   - `WeatherApp.kt`
   - `AndroidManifest.xml`
   - `res/` directory
3. Update `:app/build.gradle.kts` to remove direct dependencies no longer needed (already provided by modules)
4. **Verification:** Test all functionality thoroughly

---

## Key Files That Need Modification

### New Files That Must Be Created
- `/Users/chris/Sync/workspace/OpenNet/weather_forecast_app/core/build.gradle.kts`
- `/Users/chris/Sync/workspace/OpenNet/weather_forecast_app/domain/build.gradle.kts`
- `/Users/chris/Sync/workspace/OpenNet/weather_forecast_app/data/build.gradle.kts`
- `/Users/chris/Sync/workspace/OpenNet/weather_forecast_app/feature/build.gradle.kts`

### Existing Files That Must Be Modified
- `/Users/chris/Sync/workspace/OpenNet/weather_forecast_app/settings.gradle.kts` - Add all new modules
- `/Users/chris/Sync/workspace/OpenNet/weather_forecast_app/app/build.gradle.kts` - Update dependencies
- `/Users/chris/Sync/workspace/OpenNet/weather_forecast_app/app/src/main/java/com/example/weatherforecast/di/NetworkModule.kt` - Modify BuildConfig reference
- `/Users/chris/Sync/workspace/OpenNet/weather_forecast_app/app/src/main/java/com/example/weatherforecast/MainActivity.kt` - Update navigation reference

### Many Files Need Import Path Updates
All moved files need package path and import statement updates.

---

## Potential Challenges and Solutions

### Challenge 1: API Key Access
**Problem:** API Key is currently in `:app`'s BuildConfig, but `:core` needs to use it

**Solution:** Configure BuildConfig in `:core/build.gradle.kts` to read `local.properties`, and change NetworkModule to reference `com.example.weatherforecast.core.BuildConfig.OPENWEATHER_API_KEY`

### Challenge 2: LocationRepository Placement
**Problem:** LocationRepository interface should be in `:domain`, but implementation is in `:core`

**Solution:**
- Move Location model to `:domain`
- Move LocationRepository interface to `:domain`
- Keep LocationRepositoryImpl and LocationModule in `:core`
- Make `:core` depend on `:domain` (need to adjust dependency relationships)

**Updated dependency relationships:**
```
:app
├── :core
│   └── :domain
├── :domain
├── :data
│   ├── :core
│   └── :domain
└── :feature
    ├── :core
    └── :domain
```

### Challenge 3: Hilt Cross-Module Aggregation
**Solution:** All Hilt modules continue to use `@InstallIn(SingletonComponent::class)`, and KSP will automatically aggregate. Ensure `:app` has the `@HiltAndroidApp` annotation.

### Challenge 4: Compose Theme Access
**Solution:** Theme is in `:core`, `:feature` depends on `:core`, so it can be used directly.

---

## Expected Benefits

1. **Build Performance:** When modifying a single module, only that module and its dependents need to be recompiled
2. **Code Organization:** Clear module boundaries, easy to understand and maintain
3. **Future Expansion:** Can further split `:feature` into multiple feature modules when needed
4. **Test Isolation:** Each module can be tested independently

---

## Module Structure After Completion

```
weather_forecast_app/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/weatherforecast/
│   │   │   ├── MainActivity.kt
│   │   │   └── WeatherApp.kt
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── core/
│   ├── src/main/java/com/example/weatherforecast/core/
│   │   ├── data/remote/
│   │   ├── data/local/
│   │   ├── data/repository/LocationRepositoryImpl.kt
│   │   ├── presentation/theme/
│   │   ├── presentation/ui/components/
│   │   ├── util/
│   │   └── di/
│   └── build.gradle.kts
├── domain/
│   ├── src/main/java/com/example/weatherforecast/domain/
│   │   ├── model/
│   │   ├── repository/
│   │   └── usecase/
│   └── build.gradle.kts
├── data/
│   ├── src/main/java/com/example/weatherforecast/data/
│   │   ├── repository/
│   │   ├── mapper/
│   │   └── di/
│   └── build.gradle.kts
├── feature/
│   ├── src/main/java/com/example/weatherforecast/feature/
│   │   └── presentation/
│   │       ├── ui/
│   │       └── navigation/
│   └── build.gradle.kts
└── settings.gradle.kts
```
