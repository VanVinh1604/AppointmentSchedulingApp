package com.example.appointmentschedulingapp.data.remote.location

import com.example.appointmentschedulingapp.data.local.dao.LocationDao
import com.example.appointmentschedulingapp.data.local.mapper.toDomain
import com.example.appointmentschedulingapp.data.local.mapper.toEntity
import com.example.appointmentschedulingapp.di.IoDispatcher
import com.example.appointmentschedulingapp.domain.model.location.Province
import com.example.appointmentschedulingapp.domain.model.location.Ward
import com.example.appointmentschedulingapp.domain.repository.LocationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.map

class LocationRepositoryImpl @Inject constructor(
    private val api: ProvinceApiService,
    private val locationDao: LocationDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : LocationRepository {

    override suspend fun getProvinces(): Result<List<Province>> =
        withContext(dispatcher) {
            runCatching {
                val cached = locationDao.getProvinces()
                if (cached.isNotEmpty()) {
                    return@runCatching cached.map { it.toDomain() }
                }

                val remote = api.getProvinces()
                val entities = remote.map { it.toEntity() }

                locationDao.insertProvinces(entities)

                entities.map { it.toDomain() }
            }
        }

    override suspend fun getWardsByProvince(
        code: Int
    ): Result<List<Ward>> =
        withContext(dispatcher) {
            runCatching {
                val cached = locationDao.getWardsByProvince(code)
                if (cached.isNotEmpty()) {
                    return@runCatching cached.map { it.toDomain() }
                }

                val remote = api.getProvinceDetail(code).wards
                val entities = remote.map { it.toEntity(code) }

                locationDao.insertWards(entities)

                entities.map { it.toDomain() }
            }
        }
}