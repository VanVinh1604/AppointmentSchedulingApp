package com.example.appointmentschedulingapp.data.repository

import com.example.appointmentschedulingapp.common.Config
import com.example.appointmentschedulingapp.di.IoDispatcher
import com.example.appointmentschedulingapp.domain.model.Department
import com.example.appointmentschedulingapp.domain.repository.DepartmentRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DepartmentRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : DepartmentRepository {

    override suspend fun getDepartmentsByClinic(
        clinicId: String
    ): Result<List<Department>> = withContext(dispatcher) {
        try {
            val snapshot = firebaseDatabase
                .getReference(Config.FIREBASE_DEPARTMENTS)
                .orderByChild(Config.FIREBASE_CLINIC_ID)
                .equalTo(clinicId)
                .get()
                .await()

            val departments = snapshot.children.mapNotNull {
                it.getValue(Department::class.java)
            }

            Result.success(departments)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}