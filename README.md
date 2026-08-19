<div align="center">

# 🏃 RunTracker

**Aplikasi Pelacak Lari Profesional untuk Android**

_Tracking GPS, Analisis Statistik, dan Interval Training — semua dalam satu genggaman._

<br/>

![Kotlin](https://img.shields.io/badge/Kotlin-2.1.0-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material3-4285F4?logo=jetpackcompose&logoColor=white)
![Android](https://img.shields.io/badge/Android-SDK%2035-3DDC84?logo=android&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green)

<br/>

</div>

---

## ✨ Fitur Utama

| Fitur | Deskripsi |
|-------|-----------|
| 🗺️ **Peta Real-time** | Peta OpenStreetMap dengan rute polyline dan lokasi marker saat berlari |
| 📏 **GPS Tracking** | Pelacakan jarak, kecepatan, dan durasi secara real-time |
| 🔥 **Kalori** | Perhitungan kalori berdasarkan berat badan dan jarak tempuh |
| 📊 **Statistik** | Ringkasan mingguan, bulanan, dan total keseluruhan |
| 🏆 **Rekor Terbaik** | Catatan jarak terjauh, pace terbaik, dan kecepatan tertinggi |
| ⏱️ **Interval Training** | Mode lari/jalan interval yang bisa dikonfigurasi (detik, set) |
| 📜 **Riwayat Lari** | Semua sesi lari tersimpan dengan detail lengkap |
| ⚙️ **Pengaturan** | Berat badan, satuan (km/mil), target harian, pengumuman suara |
| 🧭 **Kalman Filter** | Smoothing GPS untuk akurasi lokasi yang lebih baik |

---

## 📱 Tampilan

<div align="center">

### Home
Layar utama dengan tombol mulai, statistik mingguan, dan rekor terbaik.

### Running
Peta real-time dengan rute, timer, kecepatan, dan jarak.

### History
Daftar semua sesi lari dengan detail lengkap.

### Stats
Statistik mingguan, bulanan, dan rekor personal.

### Interval
Konfigurasi interval training (lari/jalan/set).

### Settings
Pengaturan profil, satuan, target, dan suara.

</div>

---

## 🛠️ Tech Stack

| Komponen | Teknologi |
|----------|-----------|
| **Bahasa** | Kotlin 2.1.0 |
| **UI** | Jetpack Compose + Material3 |
| **Database** | Room (SQLite) |
| **DI** | Hilt (Dagger) |
| **Peta** | osmdroid (OpenStreetMap) |
| **GPS** | Fused Location Provider + Foreground Service |
| **Architecture** | MVVM + StateFlow |
| **Min SDK** | 24 (Android 7.0) |
| **Target SDK** | 35 (Android 15) |

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Ladybug atau lebih baru
- JDK 17
- HP Android dengan USB Debugging aktif (untuk testing)

### Build & Install

```bash
# Clone repository
git clone https://github.com/Doninugraha2304/mobile-run-tracking.git

# Masuk ke direktori
cd mobile-run-tracking

# Build APK
./gradlew assembleDebug

# Install ke HP (pastikan HP terhubung via USB)
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## 📂 Struktur Proyek

```
app/src/main/kotlin/com/runtracker/app/
├── data/
│   ├── db/            # Room Database & DAO
│   └── repository/    # Repository Layer
├── di/                # Hilt Dependency Injection
├── service/
│   └── LocationService.kt   # GPS Foreground Service
├── ui/
│   ├── components/    # Reusable Composables
│   ├── navigation/    # App Navigation
│   ├── screens/       # Home, Running, History, Stats, Interval, Settings
│   └── theme/         # Colors, Theme, Dimensions
├── util/
│   ├── GpsKalmanFilter.kt  # Kalman Filter + Stationary Detection
│   ├── LocationUtils.kt    # Formatting Helpers
│   └── PreferencesManager.kt
└── viewmodel/         # ViewModels
```

---

## ⚡ GPS Accuracy

Aplikasi menggunakan **Kalman Filter** untuk smoothing data GPS dari sensor HP:

- **Noise Reduction**: Mengurangi drift GPS saat diam
- **Stationary Detection**: Mendeteksi ketika pengguna diam dan membekukan marker
- **Velocity Damping**: Melicinkan transisi kecepatan untuk akurasi lebih baik

---

## 🤝 Contributing

Kontribusi selalu diterima! Silakan fork repository ini dan buat pull request.

1. Fork Project
2. Buat Feature Branch (`git checkout -b fitur/nama-fitur`)
3. Commit Perubahan (`git commit -m 'Tambah fitur: nama-fitur'`)
4. Push ke Branch (`git push origin fitur/nama-fitur`)
5. Buka Pull Request

---

## 📄 License

Project ini menggunakan lisensi MIT. Silakan lihat [LICENSE](LICENSE) untuk informasi lebih lanjut.

---

<div align="center">

**Dibuat dengan ❤️ oleh [Doninugraha2304](https://github.com/Doninugraha2304)**

</div>
