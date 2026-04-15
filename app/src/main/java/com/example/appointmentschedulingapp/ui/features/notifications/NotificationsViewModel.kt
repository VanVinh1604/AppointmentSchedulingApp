package com.example.appointmentschedulingapp.ui.features.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentschedulingapp.domain.usecase.notification.GetNotificationsUseCase
import com.example.appointmentschedulingapp.domain.usecase.notification.MarkAllAsReadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    getNotificationsUseCase: GetNotificationsUseCase,
    private val markAllAsReadUseCase: MarkAllAsReadUseCase
) : ViewModel() {

    val notifications = getNotificationsUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun markAllAsRead() {
        viewModelScope.launch {
            markAllAsReadUseCase()
        }
    }


}