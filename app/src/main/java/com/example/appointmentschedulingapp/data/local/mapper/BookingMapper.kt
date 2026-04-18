package com.example.appointmentschedulingapp.data.local.mapper

import com.example.appointmentschedulingapp.data.local.entity.BookingEntity
import com.example.appointmentschedulingapp.domain.model.Booking
import com.example.appointmentschedulingapp.ui.features.tickets.BookingStatus

fun Booking.toEntity(userId: String) = BookingEntity(
    id = id, userId = userId,
    clinicId = clinicId, clinicName = clinicName,
    clinicAddress = clinicAddress, clinicDistrict = clinicDistrict,
    clinicCity = clinicCity, insuranceSupported = insuranceSupported,
    departmentId = departmentId,
    doctorId = doctorId, doctorName = doctorName,
    patientId = patientId, patientName = patientName,
    patientPhone = patientPhone, patientDateOfBirth = patientDateOfBirth,
    patientHealthInsurance = patientHealthInsurance,
    specialty = specialty, service = service, bookingType = bookingType,
    slotId = slotId, appointmentDate = appointmentDate,
    appointmentTime = appointmentTime, consultationFee = consultationFee,
    paymentMethod = paymentMethod,
    status = status.name,
    createdAt = createdAt,

    synced = false
)

fun BookingEntity.toDomain() = Booking(
    id = id,
    clinicId = clinicId, clinicName = clinicName,
    clinicAddress = clinicAddress, clinicDistrict = clinicDistrict,
    clinicCity = clinicCity, insuranceSupported = insuranceSupported,
    departmentId = departmentId,
    doctorId = doctorId, doctorName = doctorName,
    patientId = patientId, patientName = patientName,
    patientPhone = patientPhone, patientDateOfBirth = patientDateOfBirth,
    patientHealthInsurance = patientHealthInsurance,
    specialty = specialty, service = service, bookingType = bookingType,
    slotId = slotId, appointmentDate = appointmentDate,
    appointmentTime = appointmentTime, consultationFee = consultationFee,
    paymentMethod = paymentMethod,
    status = runCatching { BookingStatus.valueOf(status) }
        .getOrDefault(BookingStatus.PENDING_PAYMENT),
    createdAt = createdAt
)