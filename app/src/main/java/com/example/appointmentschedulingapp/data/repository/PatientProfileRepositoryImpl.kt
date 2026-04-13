package com.example.appointmentschedulingapp.data.repository

import com.example.appointmentschedulingapp.common.Config
import com.example.appointmentschedulingapp.data.local.dao.PatientProfileDao
import com.example.appointmentschedulingapp.data.local.mapper.toDomain
import com.example.appointmentschedulingapp.data.local.mapper.toEntity
import com.example.appointmentschedulingapp.di.IoDispatcher
import com.example.appointmentschedulingapp.domain.model.PatientProfile
import com.example.appointmentschedulingapp.domain.repository.PatientRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class PatientProfileRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseAuth: FirebaseAuth,
    private val patientProfileDao: PatientProfileDao,   // ← inject DAO
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : PatientRepository {

    private val patientRef =
        firebaseDatabase.getReference(Config.FIREBASE_PATIENTS)

    private fun requireUid(): String =
        firebaseAuth.currentUser?.uid
            ?: throw IllegalStateException("User chưa đăng nhập")

    // --- CREATE: ghi Firebase trước, cache Room sau ---
    override suspend fun createPatientProfile(
        profile: PatientProfile
    ): Result<Unit> = withContext(dispatcher) {
        try {
            val userId = requireUid()
            val profileId = UUID.randomUUID().toString()
            val profileWithId = profile.copy(id = profileId)

            // 1. Ghi lên Firebase
            patientRef
                .child(userId)
                .child(profileId)
                .setValue(profileWithId)
                .await()

            // 2. Cache xuống Room
            patientProfileDao.insertProfile(profileWithId.toEntity(userId))

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPatientProfileById(
        profileId: String
    ): Result<PatientProfile> = withContext(dispatcher) {
        try {
            // Room trước
            val cached = patientProfileDao.getProfileById(profileId)
            if (cached != null) return@withContext Result.success(cached.toDomain())

            // Firebase fallback
            val userId = requireUid()
            val snapshot = patientRef.child(userId).child(profileId).get().await()
            val profile = snapshot.getValue(PatientProfile::class.java)
                ?: return@withContext Result.failure(Exception("Không tìm thấy hồ sơ"))
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updatePatientProfile(
        profile: PatientProfile
    ): Result<Unit> = withContext(dispatcher) {
        try {
            val userId = requireUid()
            val profileId = profile.id

            // Firebase trước
            patientRef.child(userId).child(profileId).setValue(profile).await()

            // Room sau
            patientProfileDao.updateProfile(profile.toEntity(userId))

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deletePatientProfile(
        profileId: String
    ): Result<Unit> = withContext(dispatcher) {
        try {
            val userId = requireUid()

            // Firebase trước
            patientRef.child(userId).child(profileId).removeValue().await()

            // Room sau
            patientProfileDao.deleteProfile(profileId)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    // --- GET: đọc Room trước (fast), sync Firebase sau (fresh) ---
    override suspend fun getPatientProfiles(): Result<List<PatientProfile>> =
        withContext(dispatcher) {
            try {
                val userId = requireUid()

                // 1. Trả cache ngay nếu có
                val cached = patientProfileDao.getProfiles(userId)
                if (cached.isNotEmpty()) {
                    // sync ngầm Firebase → Room (không block)
                    refreshFromFirebase(userId)
                    return@withContext Result.success(cached.map { it.toDomain() })
                }

                // 2. Cache trống → fetch Firebase, cache lại, trả về
                val fresh = fetchFromFirebase(userId)
                patientProfileDao.clearProfiles(userId)
                patientProfileDao.insertProfiles(fresh.map { it.toEntity(userId) })

                Result.success(fresh)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    // --- OBSERVE (optional): reactive Flow từ Room ---
    override fun observePatientProfiles(): Flow<List<PatientProfile>> {
        val userId = runCatching { requireUid() }.getOrDefault("")
        return patientProfileDao
            .observeProfiles(userId)
            .map { list -> list.map { it.toDomain() } }
    }

    // --- Private helpers ---
    private suspend fun fetchFromFirebase(userId: String): List<PatientProfile> {
        val snapshot = patientRef.child(userId).get().await()
        return snapshot.children.mapNotNull {
            it.getValue(PatientProfile::class.java)
        }
    }

    private suspend fun refreshFromFirebase(userId: String) {
        try {
            val fresh = fetchFromFirebase(userId)
            patientProfileDao.clearProfiles(userId)
            patientProfileDao.insertProfiles(fresh.map { it.toEntity(userId) })
        } catch (_: Exception) {
            // silent fail — cache vẫn còn dùng được
        }
    }
}