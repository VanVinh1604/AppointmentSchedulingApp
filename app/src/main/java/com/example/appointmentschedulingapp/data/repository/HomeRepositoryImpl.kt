package com.example.appointmentschedulingapp.data.repository

import com.example.appointmentschedulingapp.common.Config
import com.example.appointmentschedulingapp.di.IoDispatcher
import com.example.appointmentschedulingapp.domain.model.HomeAction
import com.example.appointmentschedulingapp.domain.repository.HomeRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    @IoDispatcher private val dispatcher : CoroutineDispatcher
) : HomeRepository {

    override suspend fun getQuickActions(): List<HomeAction> =
        withContext(dispatcher){

        val snapshot = firebaseDatabase
            .getReference(Config.FIREBASE_HOME_ACTIONS)
            .get()
            .await()

        snapshot.children.mapNotNull { child ->

            child.getValue(HomeAction::class.java)
        }
    }
}