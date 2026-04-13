package com.example.appointmentschedulingapp.data.repository

import com.example.appointmentschedulingapp.common.Config
import com.example.appointmentschedulingapp.data.local.dao.ClinicDao
import com.example.appointmentschedulingapp.data.local.mapper.toDomain
import com.example.appointmentschedulingapp.data.local.mapper.toEntity
import com.example.appointmentschedulingapp.di.IoDispatcher
import com.example.appointmentschedulingapp.domain.model.Clinic
import com.example.appointmentschedulingapp.domain.repository.ClinicRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ClinicRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val clinicDao: ClinicDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ClinicRepository {

    override suspend fun getClinics(): List<Clinic> = withContext(dispatcher) {
        // 1. Trả cache ngay nếu có
        val cached = clinicDao.getClinics()
        if (cached.isNotEmpty()) {
            refreshFromFirebase() // sync ngầm, không block
            return@withContext cached.map { it.toDomain() }
        }

        // 2. Cache trống → fetch Firebase, cache lại
        val fresh = fetchClinicsFromFirebase()
        clinicDao.clearClinics()
        clinicDao.insertClinics(fresh.map { it.toEntity() })
        fresh
    }

    override suspend fun getClinicById(id: String): Clinic? = withContext(dispatcher) {
        // Room trước
        val cached = clinicDao.getClinicById(id)
        if (cached != null) return@withContext cached.toDomain()

        // Firebase fallback
        firebaseDatabase.getReference(Config.FIREBASE_CLINICS)
            .child(id).get().await()
            .getValue(Clinic::class.java)
    }

    private suspend fun fetchClinicsFromFirebase(): List<Clinic> {
        val snapshot = firebaseDatabase
            .getReference(Config.FIREBASE_CLINICS).get().await()
        return snapshot.children.mapNotNull { it.getValue(Clinic::class.java) }
    }

    private suspend fun refreshFromFirebase() {
        try {
            val fresh = fetchClinicsFromFirebase()
            clinicDao.clearClinics()
            clinicDao.insertClinics(fresh.map { it.toEntity() })
        } catch (_: Exception) { }
    }
}