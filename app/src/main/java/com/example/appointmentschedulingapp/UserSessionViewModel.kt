package com.example.appointmentschedulingapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentschedulingapp.domain.model.User
import com.example.appointmentschedulingapp.domain.repository.UserSessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSessionViewModel @Inject constructor(
    private val repo: UserSessionRepository
) : ViewModel() {

    val session: StateFlow<User> = repo.sessionFlow
        .stateIn(
            scope          = viewModelScope,
            started        = SharingStarted.Companion.Eagerly,
            initialValue   = User()
        )

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            repo.clearSession()
            onDone()
        }
    }
}