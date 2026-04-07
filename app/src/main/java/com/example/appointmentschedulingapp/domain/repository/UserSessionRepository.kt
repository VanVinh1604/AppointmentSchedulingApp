// domain/repository/UserSessionRepository.kt
package com.example.appointmentschedulingapp.domain.repository

import com.example.appointmentschedulingapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserSessionRepository {
    val sessionFlow: Flow<User>
    suspend fun saveSession(uid: String, phone: String)
    suspend fun clearSession()
    fun isFirebaseLoggedIn(): Boolean
}