package com.example.appointmentschedulingapp.ui.features.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentschedulingapp.domain.model.Booking
import com.example.appointmentschedulingapp.domain.usecase.bookingUscase.CancelBookingUseCase
import com.example.appointmentschedulingapp.domain.usecase.bookingUscase.GetBookingsUseCase
import com.example.appointmentschedulingapp.domain.usecase.bookingUscase.ObserveBookingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketsViewModel @Inject constructor(
    private val getBookingsUseCase: GetBookingsUseCase,
    private val observeBookingsUseCase: ObserveBookingsUseCase,
    private val cancelBookingUseCase: CancelBookingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TicketsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Seed Room trước, sau đó observe
        viewModelScope.launch {
            getBookingsUseCase() // đảm bảo Room có data
            observeBookingsUseCase()
                .collect { bookings ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            bookings = bookings,
                            filteredBookings = applyFilter(bookings, state.selectedFilter)
                        )
                    }
                }
        }
    }
    fun loadBookings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getBookingsUseCase()
                .onSuccess { bookings ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading        = false,
                            bookings         = bookings,
                            filteredBookings = applyFilter(bookings, state.selectedFilter)
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message ?: "Không thể tải danh sách phiếu khám")
                    }
                }
        }
    }

    fun selectFilter(filter: TicketFilter) {
        _uiState.update { state ->
            state.copy(
                selectedFilter   = filter,
                filteredBookings = applyFilter(state.bookings, filter)
            )
        }
    }

    fun selectBooking(booking: Booking) {
        _uiState.update { it.copy(selectedBooking = booking) }
    }

    fun loadBookingById(bookingId: String) {
        viewModelScope.launch {
            getBookingsUseCase(bookingId)
                .onSuccess { booking ->
                    _uiState.update { it.copy(selectedBooking = booking) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(errorMessage = error.message ?: "Không thể tải thông tin phiếu khám") }
                }
        }
    }

    fun cancelBooking(bookingId: String) {
        viewModelScope.launch {
            cancelBookingUseCase(bookingId)
                .onFailure { error ->
                    _uiState.update { it.copy(errorMessage = error.message) }
                }
            // ✅ Không cần loadBookings() — Room Flow tự emit lại
        }
    }

    // Trong TicketsViewModel.kt

    private fun applyFilter(bookings: List<Booking>, filter: TicketFilter): List<Booking> {
        // ✅ Sort theo createdAt thay vì reversed()
        val active = bookings
            .filter { it.id.isNotEmpty() }
            .sortedByDescending { it.createdAt }

        return when (filter) {
            TicketFilter.ALL -> active
            TicketFilter.UNPAID -> active.filter {
                it.status == BookingStatus.UNPAID || it.status == BookingStatus.CONFIRMED
            }
            TicketFilter.PAID -> active.filter { it.status == BookingStatus.PAID }
            TicketFilter.COMPLETED -> active.filter { it.status == BookingStatus.COMPLETED }
            TicketFilter.CANCELLED -> active.filter { it.status == BookingStatus.CANCELLED }
        }
    }

}