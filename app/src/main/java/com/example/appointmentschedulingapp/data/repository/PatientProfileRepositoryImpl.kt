package com.example.appointmentschedulingapp.data.repository

import com.example.appointmentschedulingapp.common.Config
import com.example.appointmentschedulingapp.di.IoDispatcher
import com.example.appointmentschedulingapp.domain.model.PatientProfile
import com.example.appointmentschedulingapp.domain.repository.PatientRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class PatientProfileRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseAuth: FirebaseAuth,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : PatientRepository {

    private val patientRef =
        firebaseDatabase.getReference(Config.FIREBASE_PATIENTS)

    override suspend fun createPatientProfile(
        profile: PatientProfile
    ): Result<Unit> = withContext(dispatcher) {
        try {
            val userId = firebaseAuth.currentUser?.uid
                ?: return@withContext Result.failure(Exception("User chưa đăng nhập"))

            val profileId = UUID.randomUUID().toString()

            patientRef
                .child(userId)
                .child(profileId)
                .setValue(profile.copy(id = profileId))
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPatientProfiles(): Result<List<PatientProfile>> =
        withContext(dispatcher) {
            try {
                val userId = firebaseAuth.currentUser?.uid
                    ?: return@withContext Result.failure(Exception("User chưa đăng nhập"))

                val snapshot = patientRef
                    .child(userId)
                    .get()
                    .await()

                val profiles = snapshot.children.mapNotNull {
                    it.getValue(PatientProfile::class.java)
                }

                Result.success(profiles)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}