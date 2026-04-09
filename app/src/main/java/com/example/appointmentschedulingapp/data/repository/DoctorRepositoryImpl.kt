package com.example.appointmentschedulingapp.data.repository

import com.example.appointmentschedulingapp.common.Config
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
    @IoDispatcher private val dispatcher: CoroutineDispatcher // Inject đúng theo ý bạn
) : DoctorRepository {

    private val doctorRef = firebaseDatabase.getReference(Config.FIREBASE_DOCTORS)

    override suspend fun getAllDoctors(): Result<List<Doctor>> = withContext(dispatcher) {
        try {
            val snapshot = doctorRef.get().await()
            val doctors = snapshot.children.mapNotNull {
                it.getValue(Doctor::class.java)?.copy(id = it.key ?: "")
            }
            Result.success(doctors)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getDoctorsByClinic(clinicId: String): Result<List<Doctor>> = withContext(dispatcher) {
        try {
            val snapshot = doctorRef.orderByChild(Config.FIREBASE_CLINIC_ID)
                .equalTo(clinicId)
                .get()
                .await()
            val doctors = snapshot.children.mapNotNull {
                it.getValue(Doctor::class.java)?.copy(id = it.key ?: "")
            }
            Result.success(doctors)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getDoctorById(doctorId: String): Result<Doctor?> = withContext(dispatcher) {
        try {
            val snapshot = doctorRef.child(doctorId).get().await()
            val doctor = snapshot.getValue(Doctor::class.java)?.copy(id = snapshot.key ?: "")
            Result.success(doctor)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}