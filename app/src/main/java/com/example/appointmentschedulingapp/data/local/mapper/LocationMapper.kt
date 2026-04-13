package com.example.appointmentschedulingapp.data.local.mapper

import com.example.appointmentschedulingapp.data.local.entity.location.ProvinceEntity
import com.example.appointmentschedulingapp.data.local.entity.location.WardEntity
import com.example.appointmentschedulingapp.data.remote.location.dto.ProvinceDto
import com.example.appointmentschedulingapp.data.remote.location.dto.WardDto
import com.example.appointmentschedulingapp.domain.model.location.Province
import com.example.appointmentschedulingapp.domain.model.location.Ward

fun WardDto.toEntity(provinceCode: Int) = WardEntity(
    code = code,
    provinceCode = provinceCode,
    name = name
)

fun WardEntity.toDomain() = Ward(
    code = code,
    name = name
)

fun ProvinceDto.toEntity() = ProvinceEntity(
    code = code,
    name = name
)

fun ProvinceEntity.toDomain() = Province(
    code = code,
    name = name
)