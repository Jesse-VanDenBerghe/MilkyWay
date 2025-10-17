# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Project Overview

MilkyWay is a Kotlin Multiplatform project targeting Android and iOS platforms using Compose Multiplatform. The project uses a shared UI approach where most of the application logic and UI is written once in common code and shared across platforms.

The app is designed as a baby feeding timer application with three main features:
- **Timeline Screen**: Displays feeding history in reverse chronological order with time between feedings
- **Timer Screen**: Active feeding tracking with stopwatch, virtual bottle animation, and burp timer
- **Post-Feeding Summary**: Review and edit session details before saving

## Architecture

### Module Structure
- **composeApp/**: The main multiplatform module containing all shared code
  - `commonMain/`: Shared code for all platforms (UI, business logic)
  - `androidMain/`: Android-specific implementations and entry points
  - `iosMain/`: iOS-specific implementations and platform bindings
  - `commonTest/`: Shared unit tests

- **iosApp/**: iOS application wrapper containing Xcode project and SwiftUI integration

### Key Components
- `App.kt`: Main Compose UI entry point with Material 3 theming
- `Platform.kt`: Expect/actual pattern for platform-specific implementations
- `Greeting.kt`: Example business logic component
- `MainActivity.kt`: Android activity entry point
- `MainViewController.kt`: iOS view controller bridge

### Dependency Management
- Uses Gradle Version Catalogs (`gradle/libs.versions.toml`) for dependency management
- Compose Multiplatform 1.9.0 with Kotlin 2.2.20
- Targets Android SDK 24-36 and iOS 16+

## Development Commands

### Build Commands
- **Build all targets**: `./gradlew build`
- **Build Android only**: `./gradlew :composeApp:assembleDebug`
- **Build release**: `./gradlew :composeApp:assembleRelease`
- **Clean build**: `./gradlew clean`

### Testing Commands
- **Run all tests**: `./gradlew allTests`
- **Run common tests only**: `./gradlew testDebugUnitTest`
- **Run iOS simulator tests**: `./gradlew iosSimulatorArm64Test`
- **Run Android tests**: `./gradlew connectedDebugAndroidTest`
- **Run single test class**: `./gradlew testDebugUnitTest --tests "ClassName"`

### Code Quality
- **Run lint**: `./gradlew lint`
- **Fix lint issues**: `./gradlew lintFix`
- **Update lint baseline**: `./gradlew updateLintBaseline`

### Platform-Specific Development

#### Android Development
- Use `./gradlew installDebug` to install on connected device/emulator
- Android-specific code goes in `composeApp/src/androidMain/kotlin/`
- Uses Activity-based architecture with Compose UI

#### iOS Development
- Open `iosApp/iosApp.xcodeproj` in Xcode for iOS-specific development
- iOS framework is built as static library (`isStatic = true`)
- iOS-specific code goes in `composeApp/src/iosMain/kotlin/`
- MainViewController.kt bridges Kotlin to SwiftUI

## Development Patterns

### Adding New Features
1. Implement shared logic in `commonMain/`
2. Use expect/actual pattern for platform-specific implementations
3. Place platform-specific code in respective `androidMain/` or `iosMain/` folders
4. Add tests in `commonTest/` for shared logic

### Resource Management
- Uses Compose Resources for cross-platform asset management
- Resources are defined in `composeapp.generated.resources`
- Images and other assets are shared across platforms

### Testing Strategy
- Unit tests in `commonTest/` for shared business logic
- Platform-specific tests can be added to respective test directories
- UI tests can be run on both Android emulator and iOS simulator

## Package Structure
All code is organized under `com.jessevandenberghe.milkyway` package namespace.