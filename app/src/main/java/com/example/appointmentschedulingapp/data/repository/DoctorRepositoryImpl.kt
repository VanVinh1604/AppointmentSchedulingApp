package com.example.appointmentschedulingapp.data.repository

import com.example.appointmentschedulingapp.common.Config
import com.example.appointmentschedulingapp.data.local.dao.DoctorDao
import com.example.appointmentschedulingapp.data.local.mapper.toDomain
import com.example.appointmentschedulingapp.data.local.mapper.toEntity
import com.example.appointmentschedulingapp.di.IoDispatcher
import com.example.appointmentschedulingapp.domain.model.Doctor
import com.example.appointmentschedulingapp.domain.repository.DoctorRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DoctorRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val doctorDao: DoctorDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : DoctorRepository {

    private val doctorRef = firebaseDatabase.getReference(Config.FIREBASE_DOCTORS)

    override suspend fun getAllDoctors(): Result<List<Doctor>> = withContext(dispatcher) {
        runCatching {
            val cached = doctorDao.getAllDoctors()
            if (cached.isNotEmpty()) {
                refreshAllFromFirebase()
                return@runCatching cached.map { it.toDomain() }
            }
            fetchAllFromFirebase().also { fresh ->
                doctorDao.insertDoctors(fresh.map { it.toEntity(clinicId = it.id) })
            }
        }
    }

    override suspend fun getDoctorsByClinic(clinicId: String): Result<List<Doctor>> =
        withContext(dispatcher) {
            runCatching {
                val cached = doctorDao.getDoctorsByClinic(clinicId)
                if (cached.isNotEmpty()) {
                    refreshByClinicFromFirebase(clinicId)
                    return@runCatching cached.map { it.toDomain() }
                }
                fetchByClinicFromFirebase(clinicId).also { fresh ->
                    doctorDao.clearDoctorsByClinic(clinicId)
                    doctorDao.insertDoctors(fresh.map { it.toEntity(clinicId) })
                }
            }
        }

    override suspend fun getDoctorById(doctorId: String): Result<Doctor?> =
        withContext(dispatcher) {
            runCatching {
                val cached = doctorDao.getDoctorById(doctorId)
                if (cached != null) return@runCatching cached.toDomain()

                doctorRef.child(doctorId).get().await()
                    .getValue(Doctor::class.java)
                    ?.copy(id = doctorId)
            }
        }

    private suspend fun fetchAllFromFirebase(): List<Doctor> {
        val snapshot = doctorRef.get().await()
        return snapshot.children.mapNotNull {
            it.getValue(Doctor::class.java)?.copy(id = it.key ?: "")
        }
    }

    private suspend fun fetchByClinicFromFirebase(clinicId: String): List<Doctor> {
        val snapshot = doctorRef
            .orderByChild(Config.FIREBASE_CLINIC_ID)
            .equalTo(clinicId).get().await()
        return snapshot.children.mapNotNull {
            it.getValue(Doctor::class.java)?.copy(id = it.key ?: "")
        }
    }

    private suspend fun refreshAllFromFirebase() {
        try {
            val fresh = fetchAllFromFirebase()
            doctorDao.insertDoctors(fresh.map { it.toEntity(clinicId = "") })
        } catch (_: Exception) { }
    }

    private suspend fun refreshByClinicFromFirebase(clinicId: String) {
        try {
            val fresh = fetchByClinicFromFirebase(clinicId)
            doctorDao.clearDoctorsByClinic(clinicId)
            doctorDao.insertDoctors(fresh.map { it.toEntity(clinicId) })
        } catch (_: Exception) { }
    }
}