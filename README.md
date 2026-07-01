<h1 align="center">🌅 Arise</h1>

<p align="center">
  <strong>A smart, beautiful, and persistent alarm clock designed to actually wake you up.</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=flat-square&logo=android" alt="Platform: Android" />
  <img src="https://img.shields.io/badge/Kotlin-100%25-B125EA?style=flat-square&logo=kotlin" alt="Language: Kotlin" />
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=flat-square&logo=jetpack-compose" alt="UI: Jetpack Compose" />
  <img src="https://img.shields.io/badge/Architecture-Clean-FF69B4?style=flat-square" alt="Architecture: Clean" />
</p>

## ✨ Features

- **Dynamic UI:** A stunning, modern interface built entirely with Jetpack Compose Material 3.
- **Smart Scheduling:** Seamlessly handles one-time and recurring alarms based on your selected days.
- **Wake Up Tasks:** Simple swiping isn't enough. Arise forces your brain to wake up with interactive dismissal tasks:
  - 🔢 **Math:** Solve arithmetic problems to shut the alarm off.
  - 📳 **Shake:** Shake your phone vigorously to dismiss.
  - 🧠 **Memory:** A quick memory game to jumpstart your cognition.
- **Wake Up Check:** A built-in failsafe that pings you a few minutes after dismissal to make sure you didn't fall back asleep.

## 🛠️ Tech Stack

- **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) for a reactive, declarative UI.
- **Dependency Injection:** [Dagger Hilt](https://dagger.dev/hilt/) for clean and scalable architecture.
- **Database:** [Room](https://developer.android.com/training/data-storage/room) for local persistence of your schedules.
- **Background Work:** `AlarmManager` and `BroadcastReceiver` combined with Coroutines to accurately trigger and reschedule alarms even when the app is closed.
- **Architecture:** Clean Architecture principles (Domain, Data, and Presentation layers) utilizing MVVM.

[## 📱 Screenshots & Examples]:#

[*(Drop your screenshots in an `assets/` folder and link them here later!)*]:#

### Setting a New Alarm
> Choose your time, set a custom label, pick your repeat days with quick toggles, and select the Task you want to defeat in the morning. Arise calculates exactly how long you have left to sleep!

### The Wake Up Check
> Toggle this on, and Arise will subtly check in on you 5 minutes after your alarm is dismissed. If you don't respond, the alarm fires back up. No more "accidentally" going back to bed.

## 🚀 Getting Started

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/Arise.git
   ```
2. **Open in Android Studio:**
   Open the project using Android Studio.
3. **Build & Run:**
   Sync Gradle and run the `:app` configuration on your emulator or physical device.

## 🤝 Contributing

Pull requests are always welcome! Whether it's adding a new dismissal task (like a barcode scanner or a step counter), improving the UI, or fixing bugs—feel free to open an issue or submit a PR.

---
*~ Built with ♥️ by Harsh*