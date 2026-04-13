package com.example.appointmentschedulingapp.data.remote.location

import com.example.appointmentschedulingapp.domain.model.location.Province
import com.example.appointmentschedulingapp.domain.model.location.Ward
import com.example.appointmentschedulingapp.domain.repository.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val api: ProvinceApiService
) : LocationRepository {

    override suspend fun getProvinces(): Result<List<Province>> =
        withContext(Dispatchers.IO) {
            runCatching {
                api.getProvinces().map {
                    Province(it.code, it.name)
                }
            }
        }

    override suspend fun getWardsByProvince(
        code: Int
    ): Result<List<Ward>> =
        withContext(Dispatchers.IO) {
            runCatching {
                api.getProvinceDetail(code).wards.map {
                    Ward(it.code, it.name)
                }
            }
        }
}