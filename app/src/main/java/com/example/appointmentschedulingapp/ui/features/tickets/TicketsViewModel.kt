package com.example.appointmentschedulingapp.ui.features.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentschedulingapp.domain.model.Booking
import com.example.appointmentschedulingapp.domain.usecase.bookingUscase.CancelBookingUseCase
import com.example.appointmentschedulingapp.domain.usecase.bookingUscase.GetBookingsUseCase
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

    // Trong TicketsViewModel.kt

    private fun applyFilter(bookings: List<Booking>, filter: TicketFilter): List<Booking> {
        // BƯỚC 1: Sắp xếp theo thứ tự mới nhất (Dựa trên ID hoặc Timestamp nếu có)
        // Giả sử ID của bạn là Firebase Push ID hoặc có chứa timestamp,
        // reversed() sẽ đưa các item mới add gần đây lên đầu.
        val sortedBookings = bookings.reversed()

        // BƯỚC 2: Lọc dữ liệu (Bỏ qua các dữ liệu rác/đã xóa)
        // Giả sử bạn có thêm một field 'isDeleted' hoặc dựa vào status để loại bỏ
        val activeBookings = sortedBookings.filter { booking ->
            // Loại bỏ các booking không có ID hợp lệ hoặc trạng thái rác
            booking.id.isNotEmpty() && booking.status != BookingStatus.CANCELLED // Hoặc status khác tùy logic xóa của bạn
        }

        // BƯỚC 3: Áp dụng Filter từ UI
        return when (filter) {
            TicketFilter.ALL -> activeBookings
            TicketFilter.UNPAID -> activeBookings.filter {
                it.status == BookingStatus.UNPAID || it.status == BookingStatus.CONFIRMED
            }
            TicketFilter.PAID -> activeBookings.filter { it.status == BookingStatus.PAID }
            TicketFilter.COMPLETED -> activeBookings.filter { it.status == BookingStatus.COMPLETED }
            TicketFilter.CANCELLED -> activeBookings.filter { it.status == BookingStatus.CANCELLED }
        }
    }
}