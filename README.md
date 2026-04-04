🏥 Appointment Scheduling App
Ứng dụng Android đặt lịch khám bệnh được xây dựng theo hướng MVVM + Clean Architecture, tập trung vào trải nghiệm đặt lịch nhanh, dễ mở rộng và phù hợp cho các bài toán thực tế như quản lý cơ sở y tế, bác sĩ, khung giờ khám và hồ sơ bệnh nhân.

✨ Features
👨‍⚕️ Booking Flow
•	Chọn cơ sở y tế / bệnh viện / phòng khám
•	Xem chi tiết cơ sở y tế
•	Chọn chuyên khoa
•	Chọn dịch vụ khám
•	Chọn ngày khám
•	Chọn khung giờ khám
•	Chọn hồ sơ bệnh nhân
•	Xác nhận lịch hẹn
•	Sinh mã booking ID
🏥 Clinic Management
•	Danh sách cơ sở y tế
•	Chi tiết bệnh viện / phòng khám
•	Chuyên khoa theo từng cơ sở
•	Danh sách dịch vụ
•	Phòng khám khả dụng
•	Khung giờ khám theo buổi sáng / chiều
👤 User Features
•	Đăng nhập bằng số điện thoại
•	Xác thực OTP
•	Hồ sơ cá nhân
•	Danh sách phiếu khám
•	Thông báo
•	Chính sách & điều khoản
🔜 Roadmap
•	Payment integration
•	Booking reminder bằng WorkManager
•	Local notifications
•	Doctor availability realtime
•	Firebase sync lịch trống
•	Medical history
•	QR ticket check-in

🧱 Architecture
Dự án áp dụng:
•	MVVM Architecture
•	Clean Architecture
•	Repository Pattern
•	Single Source of Truth với StateFlow
•	Navigation Graph modularization
📂 Project Structure
app/src/main/java/com/example/appointmentschedulingapp
│
├── data/
│   ├── local/
│   └── repository/
│
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
│
├── ui/
│   ├── features/
│   ├── navigation/
│   └── theme/
│
├── di/
└── common/

🛠️ Tech Stack
📱 Android
•	Kotlin
•	Jetpack Compose
•	Material 3
•	Navigation Compose
•	ViewModel
•	StateFlow
•	Coroutines
🧠 Architecture & DI
•	MVVM
•	Clean Architecture
•	Repository Pattern
•	Hilt Dependency Injection
☁️ Backend / Cloud
•	Firebase Realtime Database
•	Firebase Authentication (OTP)
•	Google Services
💾 Local Storage
•	Room Database
•	DAO Pattern
⚙️ Async / Background
•	Kotlin Coroutines
•	Flow / StateFlow
•	(roadmap) WorkManager

📦 Main Modules
Booking
•	BookingViewModel
•	CreateBookingUseCase
•	BookingRepository
•	BookingRepositoryImpl
Clinic
•	ClinicViewModel
•	GetClinicsUseCase
•	ClinicRepository
Home
•	quick actions
•	promotion banners
•	trust section
•	doctor suggestions

🗃️ Data Models
Một số model chính:
•	Clinic
•	Booking
•	Doctor
•	Department
•	TimeSlot
•	Appointment
•	PatientProfile
•	PaymentTransaction
Thiết kế model theo hướng:
•	dễ scale
•	tách entity rõ ràng
•	hỗ trợ realtime booking
•	doctor slot availability

🚀 How to Run
1) Clone project
   git clone git@github.com:VanVinh1604/AppointmentSchedulingApp.git
2) Open Android Studio
   Mở project bằng Android Studio Hedgehog hoặc mới hơn.
3) Firebase config
   Đặt file:
   app/google-services.json
4) Run app
   ./gradlew installDebug

🎯 Learning Goals
Project này được xây dựng để nâng cao kỹ năng:
•	Android app architecture
•	scalable booking workflow
•	Firebase realtime data
•	clean code organization
•	multi-step form flow
•	reusable Compose UI
•	production-ready navigation

👨‍💻 Author
Bùi Văn Vinh
•	Android Developer Intern
•	Software Engineering Student
•	Focus: Android Native, Kotlin, Clean Architecture

📌 Future Improvements
•	REST API backend bằng Spring Boot / Node.js...
•	doctor schedule optimization
•	payment gateway (MoMo / VNPay)
•	reminder notification system
•	hospital dashboard
•	admin CMS
•	analytics
•	CI/CD pipeline

Đây là project cá nhân phục vụ học tập, phát triển kỹ năng Android Native và hướng tới sản phẩm có thể scale thực tế trong lĩnh vực healthcare booking.
