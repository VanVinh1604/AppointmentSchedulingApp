# 🏥 Appointment Scheduling App

Modern Android healthcare booking application built with **MVVM + Clean Architecture**, focused on scalable appointment workflows, OTP authentication, and real-time clinic scheduling.

![Kotlin](https://img.shields.io/badge/Kotlin-2.0-blue)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Latest-brightgreen)
![Architecture](https://img.shields.io/badge/Architecture-MVVM%20%2B%20Clean-orange)
![Firebase](https://img.shields.io/badge/Firebase-OTP-yellow)
![Platform](https://img.shields.io/badge/Platform-Android-success)

---

## 📱 App Preview

> Add your app screenshots inside `screenshots/` folder.

| Home | Booking | OTP |
|---|---|---|
| ![](screenshots/home.png) | ![](screenshots/booking.png) | ![](screenshots/otp.png) |

---

## ✨ Features

### 👨‍⚕️ Booking Flow
- [x] Select clinic / hospital / medical center
- [x] View clinic details
- [x] Choose department
- [x] Select medical service
- [x] Pick appointment date
- [x] Choose available time slot
- [x] Select patient profile
- [x] Confirm booking
- [x] Generate booking ID

### 🏥 Clinic Management
- [x] Clinic list
- [x] Clinic detail screen
- [x] Department by clinic
- [x] Service catalog
- [x] Available examination rooms
- [x] Morning / afternoon time slots

### 👤 User Features
- [x] Phone number login
- [x] Firebase OTP authentication
- [x] Personal profile
- [x] Appointment history
- [x] Notifications
- [x] Policy & terms

---

## 🧱 Architecture

This project follows **MVVM + Clean Architecture**.

```text
Presentation Layer
 └── Jetpack Compose + ViewModel + StateFlow

Domain Layer
 └── UseCases + Repository Contracts + Business Models

Data Layer
 └── Firebase + Room + Repository Implementations
```

### ✅ Architectural Principles
- **MVVM Architecture**
- **Clean Architecture**
- **Repository Pattern**
- **Single Source of Truth with StateFlow**
- **Navigation Graph Modularization**
- **Unidirectional Data Flow (UDF)**

---

## 📂 Project Structure

```bash
app/src/main/java/com/example/appointmentschedulingapp
├── data/
│   ├── local/
│   ├── remote/
│   └── repository/
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
├── ui/
│   ├── features/
│   ├── navigation/
│   └── theme/
├── di/
└── common/
```

---

## 🛠️ Tech Stack

| Category | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM, Clean Architecture |
| Dependency Injection | Hilt |
| Backend / Cloud | Firebase Realtime Database |
| Authentication | Firebase Phone OTP |
| Local Storage | Room Database |
| Async | Coroutines, Flow, StateFlow |
| Background Jobs | WorkManager (roadmap) |

---

## 📦 Main Modules

### Booking
- `BookingViewModel`
- `CreateBookingUseCase`
- `BookingRepository`
- `BookingRepositoryImpl`

### Clinic
- `ClinicViewModel`
- `GetClinicsUseCase`
- `ClinicRepository`

### Home
- Quick actions
- Promotion banners
- Trust section
- Doctor suggestions

---

## 🗃️ Core Data Models

- `Clinic`
- `Booking`
- `Doctor`
- `Department`
- `TimeSlot`
- `Appointment`
- `PatientProfile`
- `PaymentTransaction`

### 🎯 Design Goals
- Easy to scale
- Clear entity separation
- Realtime booking support
- Doctor slot availability
- Backend migration ready

---

## 🚀 Getting Started

### 1) Clone project
```bash
git clone git@github.com:VanVinh1604/AppointmentSchedulingApp.git
```

### 2) Open Android Studio
Use **Android Studio Hedgehog or newer**.

### 3) Firebase configuration
Place your Firebase config file here:

```bash
app/google-services.json
```

### 4) Run app
```bash
./gradlew installDebug
```

---

## 🎯 Learning Goals

This project was built to strengthen:

- Android app architecture
- Scalable booking workflow
- Firebase realtime data handling
- Clean code organization
- Multi-step form flow
- Reusable Compose UI components
- Production-ready navigation
- Modern Android development best practices

---

## 🚀 Future Roadmap

- [ ] Payment integration (MoMo / VNPay)
- [ ] Booking reminder with WorkManager
- [ ] Local push notifications
- [ ] Doctor realtime availability
- [ ] Firebase slot synchronization
- [ ] Medical history module
- [ ] QR ticket check-in
- [ ] REST API backend with Spring Boot / Node.js
- [ ] Hospital admin dashboard
- [ ] Analytics tracking
- [ ] CI/CD pipeline

---

## 🌟 Engineering Highlights

- Multi-step booking state management
- OTP authentication flow
- StateFlow Single Source of Truth
- Reusable Compose components
- Modular navigation graph
- Scalable domain-driven package structure
- Firebase-first architecture with backend migration roadmap

---

## 👨‍💻 Author

**Bùi Văn Vinh**
- Android Developer Intern
- Software Engineering Student
- Focus: **Android Native, Kotlin, Clean Architecture**

---

## 📌 About This Project

This is a personal project built for **learning, portfolio development, and real-world healthcare booking scalability exploration**.

The goal is not only to practice Android Native development, but also to design an application architecture that can evolve into a **production-ready medical appointment platform**.

