package com.example.appointmentschedulingapp.data.repository

import com.example.appointmentschedulingapp.common.Config
import com.example.appointmentschedulingapp.domain.model.Clinic
import com.example.appointmentschedulingapp.domain.repository.ClinicRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ClinicRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : ClinicRepository {

    override suspend fun getClinics(): List<Clinic> {
        val snapshot = firebaseDatabase.getReference(Config.FIREBASE_CLINICS).get().await()

        return snapshot.children.mapNotNull {
            it.getValue(Clinic::class.java)
        }
    }

    override suspend fun getClinicById(id: String): Clinic? {
        val snapshot = firebaseDatabase.getReference(Config.FIREBASE_CLINICS)
            .child(id)
            .get()
            .await()
        return snapshot.getValue(Clinic::class.java)
    }
}