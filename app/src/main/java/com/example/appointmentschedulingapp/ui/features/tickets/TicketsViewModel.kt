package com.example.appointmentschedulingapp.ui.features.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentschedulingapp.domain.model.Booking
import com.example.appointmentschedulingapp.domain.usecase.CancelBookingUseCase
import com.example.appointmentschedulingapp.domain.usecase.GetBookingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketsViewModel @Inject constructor(
    private val getBookingsUseCase: GetBookingsUseCase,
    private val cancelBookingUseCase: CancelBookingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TicketsUiState())
    val uiState = _uiState.asStateFlow()

    init { loadBookings() }

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
                .onSuccess { loadBookings() }
                .onFailure { error -> _uiState.update { it.copy(errorMessage = error.message) } }
        }
    }

    private fun applyFilter(bookings: List<Booking>, filter: TicketFilter): List<Booking> =
        when (filter) {
            TicketFilter.ALL       -> bookings
            TicketFilter.UNPAID    -> bookings.filter {
                it.status == BookingStatus.UNPAID || it.status == BookingStatus.CONFIRMED
            }
            TicketFilter.PAID      -> bookings.filter { it.status == BookingStatus.PAID }
            TicketFilter.COMPLETED -> bookings.filter { it.status == BookingStatus.COMPLETED }
            TicketFilter.CANCELLED -> bookings.filter { it.status == BookingStatus.CANCELLED }
        }
}