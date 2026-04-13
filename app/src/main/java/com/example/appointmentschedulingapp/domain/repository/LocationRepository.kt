package com.example.appointmentschedulingapp.domain.repository

import com.example.appointmentschedulingapp.domain.model.location.Province
import com.example.appointmentschedulingapp.domain.model.location.Ward

interface LocationRepository {
    suspend fun getProvinces(): Result<List<Province>>
    suspend fun getWardsByProvince(code: Int): Result<List<Ward>>
}