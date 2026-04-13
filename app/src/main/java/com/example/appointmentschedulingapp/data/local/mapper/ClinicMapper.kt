package com.example.appointmentschedulingapp.data.local.mapper

import com.example.appointmentschedulingapp.data.local.entity.ClinicEntity
import com.example.appointmentschedulingapp.domain.model.Clinic
import org.json.JSONArray

fun Clinic.toEntity() = ClinicEntity(
    id = id, name = name, address = address,
    district = district, city = city,
    imageUrl = imageUrl, bannerUrl = bannerUrl,
    rating = rating, reviewsCount = reviewsCount,
    type = type,
    specialties = JSONArray(specialties).toString(),
    description = description,
    services = JSONArray(services).toString(),
    openTime = openTime, closeTime = closeTime,
    isOpen24Hours = isOpen24Hours,
    consultationFee = consultationFee,
    emergencySupport = emergencySupport,
    latitude = latitude, longitude = longitude,
    phoneNumber = phoneNumber, website = website,
    insuranceSupported = insuranceSupported,
    parkingAvailable = parkingAvailable,
    totalDoctors = totalDoctors
)

fun ClinicEntity.toDomain(): Clinic {
    fun parseList(json: String): List<String> = runCatching {
        val arr = JSONArray(json)
        (0 until arr.length()).map { arr.getString(it) }
    }.getOrDefault(emptyList())

    return Clinic(
        id = id, name = name, address = address,
        district = district, city = city,
        imageUrl = imageUrl, bannerUrl = bannerUrl,
        rating = rating, reviewsCount = reviewsCount,
        type = type,
        specialties = parseList(specialties),
        description = description,
        services = parseList(services),
        openTime = openTime, closeTime = closeTime,
        isOpen24Hours = isOpen24Hours,
        consultationFee = consultationFee,
        emergencySupport = emergencySupport,
        latitude = latitude, longitude = longitude,
        phoneNumber = phoneNumber, website = website,
        insuranceSupported = insuranceSupported,
        parkingAvailable = parkingAvailable,
        totalDoctors = totalDoctors
    )
}