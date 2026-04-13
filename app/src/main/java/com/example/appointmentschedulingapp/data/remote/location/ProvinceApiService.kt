package com.example.appointmentschedulingapp.data.remote.location

import com.example.appointmentschedulingapp.data.remote.location.dto.ProvinceDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProvinceApiService {

    @GET("api/v2/p/")
    suspend fun getProvinces(): List<ProvinceDto>

    @GET("api/v2/p/{code}")
    suspend fun getProvinceDetail(
        @Path("code") code: Int,
        @Query("depth") depth: Int = 2
    ): ProvinceDto
}