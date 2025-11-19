# Smart Wallet

A demo Android application built with Kotlin, Jetpack Compose, and Clean Architecture showcasing two primary experiences:

- **Receipt Tracker** for capturing and managing purchase receipts.
- **Peer-to-Peer (P2P) Payments** with a mock contacts list and fake API.

## Tech Stack
- Jetpack Compose + Material 3 UI
- Hilt for dependency injection
- Room for local persistence
- Retrofit/OkHttp abstractions with a fake API implementation
- Coroutines + Flow
- Clean Architecture with dedicated domain/data/presentation layers

## Project Structure
```
app/
  data/      // Room, repositories, fake API
  domain/    // Models, repositories, use cases
  presentation/ // Compose screens + view models
```

## Running
The repository ships with a simple `gradlew` wrapper script that delegates to a local Gradle installation. Use Android Studio Iguana or newer, sync the project, and run the `app` configuration on an emulator or device.

## Testing
Basic unit tests cover repository behaviors and the main view models. Execute `./gradlew test` (with Gradle 8.6+) to run them.
