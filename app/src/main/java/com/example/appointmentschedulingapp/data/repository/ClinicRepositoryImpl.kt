package com.example.appointmentschedulingapp.data.repository

import com.example.appointmentschedulingapp.common.Config
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
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ClinicRepository {

    override suspend fun getClinics(): List<Clinic> =
        withContext(dispatcher){
        val snapshot = firebaseDatabase.getReference(Config.FIREBASE_CLINICS).get().await()

       snapshot.children.mapNotNull {
            it.getValue(Clinic::class.java)
        }
    }

    override suspend fun getClinicById(id: String): Clinic? =
        withContext(dispatcher){

        val snapshot = firebaseDatabase.getReference(Config.FIREBASE_CLINICS)
            .child(id)
            .get()
            .await()
        snapshot.getValue(Clinic::class.java)
        }
}