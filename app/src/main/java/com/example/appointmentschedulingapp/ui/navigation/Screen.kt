
package com.example.appointmentschedulingapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    // 5 tab chính
    object Home : Screen("Home", "Trang chủ", Icons.Default.Home)
    object Ticket : Screen("Ticket", "Phiếu Khám", Icons.Default.ConfirmationNumber)
    object Notification : Screen("Notification", "Thông báo", Icons.Default.Notifications)
    object Profile : Screen("Profile", "Hồ sơ", Icons.Default.AccountBox)
    object CreatePatientProfile : Screen("create_patient_profile", "Tạo hồ sơ bệnh nhân", Icons.Default.PersonAdd)
    object Account : Screen("Account", "Tài khoản", Icons.Default.Person)

    // Booking Flow
    object SelectClinic : Screen("select_clinic", "Chọn cơ sở y tế", Icons.Default.Apartment)

    object ClinicDetail : Screen(
        "clinic_detail/{clinicId}",
        "Chi tiết cơ sở",
        Icons.Default.Info
    ) {
        fun createRoute(clinicId: String): String {
            return "clinic_detail/$clinicId"
        }
    }

    object BookingStep1 : Screen("booking_step1", "Chọn thông tin khám", Icons.Default.List)
    object BookingStep2 : Screen("booking_step2", "Chọn dịch vụ", Icons.Default.MedicalServices)
    object BookingStep3 : Screen("booking_step3", "Xác nhận thông tin", Icons.Default.Schedule)

    object BookingStep4 : Screen("booking_step4", "Xác nhận thanh toan", Icons.Default.CheckCircle)
    // Auth
    object Auth : Screen("auth", "Đăng nhập/Đăng ký", Icons.Default.Lock)
    object OtpVerification : Screen(
        "otp_verification/{phoneNumber}",
        "Xác thực OTP",
        Icons.Default.Lock
    )

    object PrivatePolicy : Screen("privacy_policy", "Chính sách bảo mật", Icons.Default.Security)
    object TermOfService : Screen("term_of_service", "Điều khoản dịch vụ", Icons.Default.Description)
    object TermsOfUse : Screen("terms_of_use", "Quy định sử dụng", Icons.Default.Gavel)
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Ticket,
    Screen.Notification,
    Screen.Profile,
    Screen.Account
)