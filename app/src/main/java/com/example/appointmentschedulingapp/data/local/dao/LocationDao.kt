package com.example.appointmentschedulingapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appointmentschedulingapp.data.local.entity.location.ProvinceEntity
import com.example.appointmentschedulingapp.data.local.entity.location.WardEntity

@Dao
interface LocationDao {

    @Query("SELECT * FROM provinces")
    suspend fun getProvinces(): List<ProvinceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProvinces(
        provinces: List<ProvinceEntity>
    )

    @Query("SELECT * FROM wards WHERE provinceCode = :provinceCode")
    suspend fun getWardsByProvince(
        provinceCode: Int
    ): List<WardEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWards(
        wards: List<WardEntity>
    )
}