package com.example.appointmentschedulingapp.data.local.mapper

import com.example.appointmentschedulingapp.data.local.entity.DoctorEntity
import com.example.appointmentschedulingapp.domain.model.Doctor

fun Doctor.toEntity(clinicId: String) = DoctorEntity(
    id = id,
    departmentId = departmentId,
    clinicId = clinicId,
    fullName = fullName,
    title = title,
    rating = rating,
    imageUrl = imageUrl,
    biography = biography
)

fun DoctorEntity.toDomain() = Doctor(
    id = id,
    departmentId = departmentId,
    fullName = fullName,
    title = title,
    rating = rating,
    imageUrl = imageUrl,
    biography = biography
)