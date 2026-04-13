package com.example.appointmentschedulingapp.data.remote.location.dto

data class ProvinceDto(
    val code: Int,
    val name: String,
    val wards: List<WardDto> = emptyList()
)

data class WardDto(
    val code: Int,
    val name: String
)