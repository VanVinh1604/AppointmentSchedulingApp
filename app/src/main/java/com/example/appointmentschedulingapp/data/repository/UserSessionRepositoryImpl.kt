// data/repository/UserSessionRepositoryImpl.kt
package com.example.appointmentschedulingapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.appointmentschedulingapp.domain.model.User
import com.example.appointmentschedulingapp.domain.repository.UserSessionRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSessionRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val auth: FirebaseAuth
) : UserSessionRepository {

    companion object {
        private val KEY_UID   = stringPreferencesKey("uid")
        private val KEY_PHONE = stringPreferencesKey("phone")
    }

    override val sessionFlow: Flow<User> = dataStore.data.map { prefs ->
        val uid   = prefs[KEY_UID]   ?: ""
        val phone = prefs[KEY_PHONE] ?: ""
        User(
            id        = uid,
            phoneNumber     = phone,
            isLoggedIn = uid.isNotEmpty()
        )
    }

    override suspend fun saveSession(uid: String, phone: String) {
        dataStore.edit { prefs ->
            prefs[KEY_UID]   = uid
            prefs[KEY_PHONE] = phone
        }
    }

    override suspend fun clearSession() {
        dataStore.edit { it.clear() }
        auth.signOut()
    }

    override fun isFirebaseLoggedIn(): Boolean =
        auth.currentUser != null
}