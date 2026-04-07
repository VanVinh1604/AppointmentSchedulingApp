package com.example.appointmentschedulingapp.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentschedulingapp.domain.model.Clinic
import com.example.appointmentschedulingapp.domain.model.HomeAction
import com.example.appointmentschedulingapp.domain.usecase.GetClinicsUseCase
import com.example.appointmentschedulingapp.domain.usecase.GetHomeActionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeActionsUseCase: GetHomeActionsUseCase,
    private val getClinicsUseCase: GetClinicsUseCase

) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    private val _event = MutableSharedFlow<HomeEvent>()
    val event = _event.asSharedFlow()

    private val _trustedClinics = MutableStateFlow<List<Clinic>>(emptyList())
    val trustedClinics: StateFlow<List<Clinic>> =
        _trustedClinics.asStateFlow()

    init {
        loadActions()
        loadTrustedClinics()

    }

    private fun loadTrustedClinics() {
        viewModelScope.launch {
            _trustedClinics.value =
                getClinicsUseCase().take(3)
        }
    }
    private fun loadActions() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val actions = getHomeActionsUseCase()

            _uiState.value = HomeUiState(
                actions = actions,
                isLoading = false
            )
        }
    }

    // Trong HomeViewModel.kt
    fun onActionClicked(action: HomeAction) {
        val route = when (action.iconName) {
            "clinic" -> "select_clinic"
            "specialty" -> "specialty_screen"
            // Thêm các trường hợp khác ở đây...
            else -> null // Trả về null thay vì route sai
        }

        route?.let {
            viewModelScope.launch {
                _event.emit(HomeEvent.Navigate(it))
            }
        } ?: run {
            // Log hoặc thông báo "Tính năng đang phát triển" thay vì chuyển trang
            println("Feature not implemented for: ${action.title}")
        }
    }

}