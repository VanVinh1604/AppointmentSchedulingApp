package com.example.appointmentschedulingapp.ui.features.booking

// BookingUiState.kt
import com.example.appointmentschedulingapp.domain.model.Clinic

data class BookingUiState(

    val bookingId: String = "",

    // --- TRẠNG THÁI HỆ THỐNG ---
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,

    // --- DỮ LIỆU STEP 1: CHỌN THÔNG TIN KHÁM ---
    val selectedClinic: Clinic? = null, // Lưu cả object Clinic thay vì mỗi tên
    val selectedSpecialty: String = "", // Chuyên khoa đã chọn
    val selectedService: String = "",   // Dịch vụ đã chọn
    val selectedDate: String = "",      // Ngày khám (dd/mm/yyyy)
    val selectedTime: String = "",      // Giờ khám (hh:mm)
    val selectedRoom: String = "",


    // --- DỮ LIỆU STEP 2: CHỌN HỒ SƠ BỆNH NHÂN ---
    val selectedPatientId: String = "", // ID hồ sơ được chọn
    val patientName: String = "",       // Tên bệnh nhân để hiển thị tóm tắt

    // --- ĐIỀU HƯỚNG ---
    val currentStep: Int = 1
)