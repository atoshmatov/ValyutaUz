# Project Context for Claude - ValyutaUZ

## 🎯 Project Overview

**Name:** ValyutaUZ - O'zbekiston Valyuta Kurslariga Monitor  
**Purpose:** Android app showing Central Bank exchange rates with 6-hour offline support  
**Status:** 🟢 Active (v1.0.3, Build 13) - Published on Play Store  
**Team:** Ali Toshmatov  

## 📍 Quick Reference

### Obsidian Documentation
- **Main Docs:** [[loyihalar/ValyutaUZ/README]]
- **Tech Stack:** [[loyihalar/ValyutaUZ/Technical-Stack]]
- **Architecture:** [[loyihalar/ValyutaUZ/Project-Structure]]
- **Features:** [[loyihalar/ValyutaUZ/Key-Features]]
- **Issues:** [[loyihalar/ValyutaUZ/Known-Issues]]
- **Daily Work:** [[learning/daily-notes/2026-04-12]]

### GitHub
- Repository: (Check with Ali)
- Branch: main (production), develop (development)

---

## 🎓 Current Understanding

### What Has Been Done ✅

- ✅ **Core Android App** with Jetpack Compose UI
- ✅ **Room Database** for offline storage (SQLite)
- ✅ **Retrofit API** integration with CBU (Central Bank)
- ✅ **6-Hour Offline Cache** strategy
- ✅ **Dark/Light Theme** support (Material 3)
- ✅ **Multi-Language** support (Uzbek/English)
- ✅ **Published** on Play Store (v1.0.3)
- ✅ **Converter** screen with real-time conversion
- ✅ **Daily Notifications** about rate changes
- ✅ **Responsive UI** for all screen sizes

### What Needs To Be Done 🔄

- [ ] Fix offline sync edge cases (when network switches)
- [ ] Improve UI animations and transitions
- [ ] Add comprehensive unit tests
- [ ] Enhance notification system
- [ ] Performance optimization for large datasets
- [ ] Accessibility improvements

### Important Context

**Architecture:** Clean Architecture + MVVM Pattern

```
Presentation (Compose)
    ↓
ViewModel (State Management)
    ↓
Domain (Business Logic)
    ↓
Repository (Data Abstraction)
    ↓
Local (Room DB) + Remote (Retrofit API)
```

**Key Technologies:**
- **UI Framework:** Jetpack Compose + Material Design 3
- **Language:** Kotlin 1.8+
- **Database:** Room (SQLite)
- **Networking:** Retrofit + OkHttp
- **DI:** Hilt
- **Navigation:** Voyager (Bottom tabs)
- **Async:** Kotlin Coroutines
- **Image Loading:** Coil

---

## 💻 How To Work With This Project

### Prerequisites

- Android Studio 2024 or later
- JDK 11 or higher
- Android SDK 36+
- Gradle 8+
- Git

### Setup

```bash
# Clone repository
git clone <repo-url>
cd ValyutaUz

# Build debug version
./gradlew assembleDebug

# Install on device/emulator
./gradlew installDebug

# Run tests
./gradlew test
```

### Build & Run Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Install debug build
./gradlew installDebug

# Run on emulator
./gradlew installDebug
adb shell am start -n uz.toshmatov.currency.debug/uz.toshmatov.currency.MainActivity

# Format code with ktlint
./gradlew ktlintFormat

# Run lint checks
./gradlew lint

# Clean build
./gradlew clean build
```

### Key Files & Directories

| Path | Purpose |
|------|---------|
| `app/src/main/java/uz/toshmatov/currency/` | Source code |
| `app/src/main/.../presentation/` | UI Screens (Compose) |
| `app/src/main/.../data/` | API & Database layer |
| `app/src/main/.../domain/` | Business logic |
| `app/src/main/.../di/` | Dependency Injection setup |
| `app/build.gradle.kts` | Dependencies & config |
| `gradle/libs.versions.toml` | Centralized versions |

### Important Modules

```
core/
  ├── theme/           → Design system (colors, fonts)
  ├── uicompoenent/    → Reusable Compose components
  ├── extensions/      → Kotlin extensions
  ├── logger/          → Timber logging
  ├── connect/         → Network connectivity
  └── mapper/          → Data mapping

data/
  ├── local/
  │   ├── room/        → Room database entities & DAOs
  │   ├── prefs/       → DataStore preferences
  │   └── repository/  → Local repository impl
  └── remote/
      ├── api/         → Retrofit service
      └── repository/  → Remote repository impl

presentation/
  ├── home/            → Home screen (rates list)
  ├── detail/          → Detail screen (single rate)
  ├── converter/       → Converter screen
  ├── settings/        → Settings screen
  └── viewmodel/       → Screens' ViewModels

di/
  ├── NetworkModule.kt → API client & Retrofit setup
  ├── DatabaseModule.kt → Room DB setup
  └── RepositoryModule.kt → Repository bindings
```

---

## 📊 Screens & Features

| Screen | Features | Status |
|--------|----------|--------|
| **Home** | List all rates, real-time update, refresh | ✅ Done |
| **Detail** | Single rate details, history, trends | ✅ Done |
| **Converter** | Convert between currencies | ✅ Done |
| **Settings** | Language, theme, notifications | ✅ Done |
| **Info** | App information, version | ✅ Done |

---

## 🔄 API Integration

**API Source:** O'zbekiston Markaziy Banki (Central Bank)  
**Endpoint:** https://cbu.uz/uz/arkhiv-kursov-valyut/  
**Update Frequency:** Hourly (usually at XX:00)  
**Response Format:** XML/JSON

**Key Points:**
- API is public (no authentication needed)
- Updates happen at specific times
- Must cache responses (use Room DB)
- Must handle network errors gracefully

---

## 💾 Database Strategy

### Offline Support

- **Cache Duration:** 6 hours from last successful fetch
- **Strategy:** 
  - If offline + last update < 6 hours → Show cached data
  - If offline + last update > 6 hours → Show empty screen with message
  - If online → Always fetch fresh data and update cache

### Room Schema

```
Currency (Entity)
├── code (String) - Primary Key
├── rate (Double)
├── date (LocalDate)
├── lastUpdated (Long)
└── isCached (Boolean)
```

---

## ⚠️ Important Reminders

1. **Offline First:** App must work offline for 6 hours minimum
2. **Material 3:** Follow Material Design 3 guidelines
3. **Compose:** Always use Compose best practices
4. **Min SDK:** Must support Android 5.0+ (API 21)
5. **Testing:** Test on multiple screen sizes and devices
6. **Themes:** Always test both Light and Dark modes
7. **Performance:** Monitor recomposition and memory usage
8. **Network:** Handle network errors gracefully

---

## 🤝 How Claude Should Help

When working on this project, please:

1. **Understand Architecture:** Use Clean Architecture + MVVM patterns
2. **Follow Conventions:** Use same code style and patterns as existing code
3. **Offline First:** Always consider 6-hour offline requirement
4. **Material 3:** Use Material 3 design system consistently
5. **Test Thoroughly:** 
   - Test on API 21+ devices
   - Test offline/online switching
   - Test both light and dark themes
6. **Reference Docs:** Check [[loyihalar/ValyutaUZ/Technical-Stack]] for dependency versions
7. **Optimize:** Monitor performance, especially in lists

---

## 📝 Current Session Notes

**Last Updated:** 2026-04-12 15:30  
**Status:** Project documentation completed  
**What's Being Worked On:** Foundation documentation for Claude Code integration  
**Expected Outcome:** Claude should now understand project fully without re-explaining

---

## 🔧 Common Tasks

### Add a New Feature

1. Create feature branch: `git checkout -b feature/new-feature`
2. Implement in appropriate layer (presentation → domain → data)
3. Follow existing patterns
4. Test offline scenarios
5. Test both themes
6. Create commit with meaningful message
7. Update CLAUDE.md with progress

### Fix a Bug

1. Create bug branch: `git checkout -b fix/bug-name`
2. Locate issue (check Known-Issues in Obsidian)
3. Write minimal reproduction
4. Fix with minimal changes
5. Test fix works
6. Commit with "fix: " prefix
7. Close issue if in tracker

### Optimize Performance

1. Profile using Android Profiler
2. Identify bottleneck
3. Implement fix
4. Measure improvement
5. Document changes
6. Test doesn't break other features

---

## 📚 References

- **Android Docs:** https://developer.android.com/
- **Jetpack Compose:** https://developer.android.com/jetpack/compose
- **Kotlin:** https://kotlinlang.org/
- **Material Design 3:** https://m3.material.io/
- **Room Database:** https://developer.android.com/training/data-storage/room

---

## 🚀 Quick Commands Summary

```bash
# Development
./gradlew assembleDebug        # Build debug
./gradlew installDebug         # Install on device
./gradlew test                 # Run tests

# Code Quality
./gradlew ktlintFormat         # Format code
./gradlew lint                 # Run lint checks

# Release
./gradlew assembleRelease      # Build release APK

# Utilities
./gradlew clean                # Clean build
./gradlew build --refresh      # Force update deps
```

---

**Last Session:** Documentation and Claude integration setup  
**Next Session:** Begin work on next feature or bug fix - Claude will have full context!

---

*For detailed documentation, see [[loyihalar/ValyutaUZ/README]]*
