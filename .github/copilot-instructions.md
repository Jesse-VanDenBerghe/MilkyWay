# Copilot Instructions for MilkyWay Repository

## Project Overview

**MilkyWay** is a Kotlin Multiplatform (KMP) baby feeding timer application targeting both Android and iOS platforms using Compose Multiplatform. The app helps parents track their baby's feeding times with features including a timeline view of feeding history, an active timer screen with visual feedback, and post-feeding session summaries.

### Key Technologies & Versions
- **Kotlin**: 2.2.20
- **Compose Multiplatform**: 1.9.0
- **Gradle**: 8.14.3
- **Java**: 17 (required, not 11)
- **Android SDK**: Targets 24-36 (min: 24, target: 36, compile: 36)
- **iOS**: 16+
- **Build System**: Gradle with Kotlin Multiplatform plugin and SQLDelight for database

### Repository Size & Type
- Small to medium multiplatform mobile project (~1000 lines of Kotlin source code)
- Shared UI architecture using Compose for both Android and iOS
- Single Gradle module structure with platform-specific source sets

## Build & Development Commands

### Prerequisites
**ALWAYS** verify Java 17 is available before building:
```bash
java -version  # Should show Java 17.x.x
```

### Core Build Tasks

#### Clean Build (Always run before first build)
```bash
./gradlew clean
```
**Purpose**: Removes all build artifacts and generated files
**Duration**: ~5-10 seconds
**When to use**: Before initial build, after major dependency changes, or when experiencing strange build errors

#### Full Build (All targets)
**IMPORTANT**: The standard `./gradlew build` command will fail due to an existing lint error. Use one of these instead:
```bash
# Option 1 (RECOMMENDED for CI/PR validation): Skip lint checks
./gradlew build -x lint

# Option 2 (For development): Skip all validation and lint
./gradlew build -x lint -x lintVitalRelease
```
**Duration**: First run ~3-5 minutes, subsequent runs ~1-2 minutes (caching enabled)
**Output**: Builds all platforms (Android debug/release, iOS arm64 and simulator arm64)

#### Android-Specific Builds
```bash
# Build Android debug APK
./gradlew :composeApp:assembleDebug

# Build Android release APK  
./gradlew :composeApp:assembleRelease

# Install debug APK to connected device/emulator
./gradlew :composeApp:installDebug
```

### Testing

#### Run All Tests (Kotlin Multiplatform Unit Tests)

NOTE: We are building Android first, so don't validate until Android tests passed and approved by the user.

```bash
./gradlew allTests
```
**Duration**: ~10-15 seconds
**Note**: Tests are written in `src/commonTest/` using Kotlin's `kotlin.test` framework

#### Run Android Unit Tests Only
```bash
./gradlew testDebugUnitTest
```

#### Run iOS Simulator Tests
```bash
./gradlew :composeApp:iosSimulatorArm64Test
```

#### Run Specific Test Class
```bash
./gradlew testDebugUnitTest --tests "com.jessevandenberghe.milkyway.ui.screens.timer.TimerStateTest"
```

### Linting & Code Quality

**CRITICAL ISSUE**: The project currently has a lint error in `composeApp/src/androidMain/kotlin/com/jessevandenberghe/milkyway/utils/BrightnessControl.android.kt` at line 11 where `LocalContext` is incorrectly cast to `Activity` (should use `LocalActivity` instead).

```bash
# View all lint issues
./gradlew lint

# Run lint fix (may not resolve all issues)
./gradlew lintFix

# Update lint baseline to accept current errors
./gradlew updateLintBaseline
```

### Gradle Configuration Cache
The project uses Gradle configuration cache (`org.gradle.configuration-cache=true` in `gradle.properties`). This dramatically speeds up builds after the first run. If you encounter configuration cache issues, use `--no-build-cache` flag.

## Project Structure & Key Locations

### Module Organization
```
composeApp/
├── src/
│   ├── commonMain/          # Shared code for all platforms
│   │   ├── kotlin/          # All business logic, UI components, and navigation
│   │   ├── sqldelight/      # Database schema (.sq files)
│   │   └── composeResources/ # Shared images/resources
│   ├── commonTest/          # Unit tests for shared code (Kotlin Test framework)
│   ├── androidMain/         # Android-specific implementations (Platform.android.kt, MainActivity.kt)
│   └── iosMain/             # iOS-specific implementations (MainViewController.kt, Platform.ios.kt)
├── build.gradle.kts         # Module-level build configuration (main build file)
└── build/                   # Generated files (ignore)

iosApp/                       # iOS Xcode project wrapper
├── iosApp.xcodeproj/        # Xcode project file
└── iosApp/                  # SwiftUI entry point

gradle/
└── libs.versions.toml       # Centralized dependency versions (managed catalog)
```

### Key Source Files

**Package Root**: `com.jessevandenberghe.milkyway`

- **App.kt** (`commonMain/kotlin/com/jessevandenberghe/milkyway/`)
  - Main Compose UI entry point with Material 3 theming
  - Sets up the root Box with navigation graph

- **navigation/** - Navigation setup using Voyager library
  - Screens and navigation model definitions

- **ui/screens/** - UI screens implementation
  - `timer/` - Active timer screen with state management
  - `timeline/` - Timeline screen showing feeding history
  - Other UI components and utilities

- **data/** - Data layer
  - Repository patterns, data models

- **database/** - SQLDelight database definitions
  - Generated database queries and schema

- **Platform.kt** (expect/actual in common, android, ios)
  - Platform-specific implementations using expect/actual pattern

### Configuration Files

- **build.gradle.kts** (root) - Plugin definitions for all subprojects
- **composeApp/build.gradle.kts** - Module build configuration with dependencies and build variants
- **gradle.properties** - Gradle JVM settings (4GB) and configuration cache
- **settings.gradle.kts** - Project structure and repository configuration
- **gradle/libs.versions.toml** - Centralized version catalog (update here for dependencies)

## Continuous Integration & Validation Pipeline

### GitHub Actions Workflow
Located at: `.github/workflows/android.yml`

The CI pipeline runs on every `push` to `main` and on all `pull_request` events:
1. Checks out code
2. Sets up Java 17
3. Grants execute permission to gradlew
4. Runs `./gradlew build`

**CRITICAL**: The CI currently fails because of the lint error. The repository maintainer needs to either:
- Fix the `BrightnessControl.android.kt` file to use `LocalActivity` instead of casting `LocalContext`
- Or modify the workflow to use `./gradlew build -x lint`

### To Validate Changes Locally (Replicate CI)
```bash
./gradlew clean
./gradlew build -x lint  # Use this to skip the known lint error
./gradlew allTests
```

## Important Architectural Patterns

### Expect/Actual Pattern
Platform-specific code uses Kotlin's expect/actual mechanism:
- Define `expect` function/class in `commonMain`
- Implement `actual` in `androidMain` and `iosMain`
- Example: `Platform.kt`, `BrightnessControl.*.kt`, `DatabaseFactory.*.kt`

### State Management
- Uses Voyager for navigation and screen state management
- ViewModels in `screenModel` pattern with Compose
- State data classes defined in separate files (e.g., `TimerState.kt`)

### Database Access
- SQLDelight for type-safe database queries across platforms
- Schemas defined in `.sq` files in `src/commonMain/sqldelight/`
- Generated code placed in `build/generated/sqldelight/`

## Troubleshooting & Known Issues

### Build Fails with Lint Error
**Error**: `Lint found errors in the project; aborting build.`
**Cause**: Invalid `LocalContext` cast in `BrightnessControl.android.kt`
**Solution**: 
- Use `./gradlew build -x lint` to skip lint
- Or manually fix the file (replace cast with `LocalActivity`)

### Gradle Configuration Cache Issues
**Symptoms**: Unusual build failures, "Configuration cache entry could not be reused"
**Solution**:
```bash
./gradlew build --no-build-cache
```

### Java Version Mismatch
**Symptoms**: Build fails with Java version errors
**Solution**: Verify `java -version` returns 17.x.x. If not, set `JAVA_HOME`:
```bash
export JAVA_HOME=$(which java)
```

### Tests Not Running
**Symptoms**: `allTests` returns no executed tasks
**Solution**:
```bash
./gradlew clean testDebugUnitTest  # Force recompilation
```

### iOS Build Issues
iOS-specific development typically uses Xcode directly for SwiftUI. To rebuild the Kotlin framework:
```bash
./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64
```

## Development Workflow

### Making Code Changes
1. Make changes in appropriate source set:
   - Shared logic → `commonMain/kotlin/`
   - Android-only → `androidMain/kotlin/`
   - iOS-only → `iosMain/kotlin/`
2. Add tests in `commonTest/kotlin/` for shared code
3. Run `./gradlew build -x lint` to verify compilation
4. Run `./gradlew allTests` to verify tests pass

### Adding Dependencies
1. Add version in `gradle/libs.versions.toml` under `[versions]`
2. Add library reference in `[libraries]` section
3. Add to appropriate `sourceSets.*.dependencies` in `composeApp/build.gradle.kts`
4. Run `./gradlew build -x lint` to verify

### Modifying Database Schema
1. Create or modify `.sq` file in `src/commonMain/sqldelight/`
2. Run `./gradlew build` - SQLDelight generates Kotlin code
3. Generated code appears in `build/generated/sqldelight/`

## Trust These Instructions

These instructions have been validated through:
- Running `./gradlew clean` and `./gradlew build -x lint` successfully
- Verifying test commands with `./gradlew allTests` and `./gradlew testDebugUnitTest`
- Confirming iOS build with `./gradlew :composeApp:iosSimulatorArm64Test`
- Documenting the known lint error and its workaround

If you encounter information in the repository that contradicts these instructions, please verify the contradiction by:
1. Attempting the command as documented here first
2. Checking if file paths or command outputs have changed
3. Only then searching for updated documentation
