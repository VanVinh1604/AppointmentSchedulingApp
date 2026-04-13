package com.example.appointmentschedulingapp.data.local.mapper

import com.example.appointmentschedulingapp.data.local.entity.PatientProfileEntity
import com.example.appointmentschedulingapp.domain.model.PatientProfile

fun PatientProfile.toEntity(userId: String) = PatientProfileEntity(
    id = id ?: "",
    userId = userId,
    fullName = fullName,
    dateOfBirth = dateOfBirth,
    gender = gender,
    phoneNumber = phoneNumber,
    address = address,
    identityCard = identityCard ?: "",
    healthInsuranceNumber = healthInsuranceNumber ?: "",
    healthInsuranceExpiry = healthInsuranceExpiry ?: "",
    relationship = relationship ?: "",
    emergencyContact = emergencyContact ?: "",
    allergies = allergies ?: "",
    medicalHistory = medicalHistory ?: "",
    isDefault = isDefault
)

fun PatientProfileEntity.toDomain() = PatientProfile(
    id = id,
    fullName = fullName,
    dateOfBirth = dateOfBirth,
    gender = gender,
    phoneNumber = phoneNumber,
    address = address,
    identityCard = identityCard,
    healthInsuranceNumber = healthInsuranceNumber,
    healthInsuranceExpiry = healthInsuranceExpiry,
    relationship = relationship,
    emergencyContact = emergencyContact,
    allergies = allergies,
    medicalHistory = medicalHistory,
    isDefault = isDefault
)