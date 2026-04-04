package com.example.appointmentschedulingapp.data.repository

import com.example.appointmentschedulingapp.common.Config
import com.example.appointmentschedulingapp.domain.model.HomeAction
import com.example.appointmentschedulingapp.domain.repository.HomeRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : HomeRepository {

    override suspend fun getQuickActions(): List<HomeAction> {
        val snapshot = firebaseDatabase
            .getReference(Config.FIREBASE_HOME_ACTIONS)
            .get()
            .await()

        return snapshot.children.mapNotNull { child ->
            child.getValue(HomeAction::class.java)
        }
    }
}