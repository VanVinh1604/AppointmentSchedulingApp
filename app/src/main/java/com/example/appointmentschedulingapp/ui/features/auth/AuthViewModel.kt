package com.example.appointmentschedulingapp.ui.features.auth

import android.app.Activity
import android.util.Log.e
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentschedulingapp.core.helper.ErrorHelper
import com.example.appointmentschedulingapp.domain.usecase.SendOtpUseCase
import com.example.appointmentschedulingapp.domain.usecase.VerifyOtpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sendOtpUseCase: SendOtpUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _event = Channel<AuthEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    private var timerJob: Job? = null


    fun sendOtp(activity: Activity, phone: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            sendOtpUseCase(activity, phone)
                .onSuccess { verificationId ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            phone = phone,
                            verificationId = verificationId
                        )
                    }

                    sendEvent(AuthEvent.NavigateToOtp(phone))
                    startOtpCountdown()
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = ErrorHelper.toFriendlyMessage(e)                        )
                    }
                }
        }
    }
    fun verifyOtp(otp: String) {
        val verificationId = _uiState.value.verificationId ?: return

        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(isLoading = true)
            }

            verifyOtpUseCase(verificationId, otp)
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(isLoading = false)
                    }

                    _event.send(AuthEvent.NavigateToHome)
                }
                .onFailure { exception ->
                    val message = ErrorHelper.toFriendlyMessage(exception)

                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            error = message
                        )
                    }

                    _event.send(AuthEvent.ShowError(message))
                }
        }
    }

    private suspend fun sendEvent(event: AuthEvent) {
        _event.send(event)
    }

    fun startOtpCountdown() {
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            for (second in 60 downTo 0) {
                _uiState.update {
                    it.copy(resendSeconds = second)
                }
                delay(1000)
            }
        }
    }
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}