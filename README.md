# Voitto - Finnish Budgeting App

A free Android mobile app designed to help low-income families in Finland manage their monthly budget and avoid financial issues.

## 🚀 Features

- **💰 Safe-to-Spend Calculation** - AI-powered prediction of available spending money
- **🔔 Forgotten Expenses Reminders** - Never miss infrequent but crucial bills
- **💡 Saving Tips & Challenges** - Localized Finnish saving strategies
- **🏛️ Resource Hub** - Links to Finnish support services (Kela, Takuusäätiö, etc.)
- **📊 Cash Flow Visualization** - Clear, animated financial overview
- **🎨 Calming UI Design** - Stress-free financial management experience

## 🛠️ Technical Stack

- **Platform**: Android (Kotlin, Jetpack Compose, Material 3)
- **Architecture**: MVVM + Clean Architecture
- **Database**: Room (SQLite) with local encryption
- **Dependency Injection**: Hilt
- **Notifications**: WorkManager for scheduled reminders
- **AI**: On-device ML for expense prediction
- **Localization**: Finnish (primary), Swedish (secondary)

## 📱 Target Audience

- Low-income families in Finland
- Users with limited financial literacy
- Android devices (API 24+)

## 🔧 Development Setup

### Prerequisites
- Android Studio Arctic Fox or later
- JDK 11 or higher
- Android SDK 24+

### Building the App

1. Clone the repository:
```bash
git clone https://github.com/T20kolat/Voitto-Android.git
cd Voitto-Android
```

2. Open in Android Studio:
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

3. Build and run:
   - Connect your Android device (enable USB debugging)
   - Click the "Run" button (▶️) in Android Studio

## 🧪 Testing

The project includes comprehensive testing setup:

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Run lint checks
./gradlew lint

# Run code quality checks
./gradlew detekt
./gradlew ktlintCheck
```

## 🚀 CI/CD Pipeline

This project uses GitHub Actions for automated building and testing:

### Workflows

1. **Android CI** (`.github/workflows/android-ci.yml`)
   - Runs on every push and pull request
   - Executes unit tests, lint checks, and builds APKs
   - Uploads build artifacts

2. **Code Quality** (`.github/workflows/code-quality.yml`)
   - Static code analysis with Detekt
   - Code formatting checks with KtLint
   - Test coverage reporting

3. **Release** (`.github/workflows/android-release.yml`)
   - Triggered by version tags (e.g., `v1.0.0`)
   - Creates GitHub releases with APK files
   - Automated release notes

### Build Status

[![Android CI](https://github.com/T20kolat/Voitto-Android/actions/workflows/android-ci.yml/badge.svg)](https://github.com/T20kolat/Voitto-Android/actions/workflows/android-ci.yml)
[![Code Quality](https://github.com/T20kolat/Voitto-Android/actions/workflows/code-quality.yml/badge.svg)](https://github.com/T20kolat/Voitto-Android/actions/workflows/code-quality.yml)

## 📦 Building APKs

### Debug Build
```bash
./gradlew assembleDebug
```
Output: `app/build/outputs/apk/debug/app-debug.apk`

### Release Build
```bash
./gradlew assembleRelease
```
Output: `app/build/outputs/apk/release/app-release.apk`

## 🎯 Performance

The app is optimized for low-end Android devices:
- Database indexes for fast queries
- Memory-efficient animations
- Background data loading
- Optimized type converters
- Performance monitoring utilities

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📞 Support

For support and questions, please open an issue on GitHub.

---

**Made with ❤️ for Finnish families**
