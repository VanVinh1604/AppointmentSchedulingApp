package com.example.appointmentschedulingapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.appointmentschedulingapp.ui.features.tickets.TicketDetailScreen
import com.example.appointmentschedulingapp.ui.features.tickets.TicketsScreen
import com.example.appointmentschedulingapp.ui.features.tickets.TicketsViewModel

fun NavGraphBuilder.ticketsNavGraph(navController: NavHostController) {
    navigation(
        route = "tickets_graph",
        startDestination = Screen.Ticket.route
    ) {
        composable(Screen.Ticket.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("tickets_graph")
            }

            val vm: TicketsViewModel = hiltViewModel(parentEntry)

            TicketsScreen(
                viewModel = vm,
                onViewDetail = { booking ->
                    vm.selectBooking(booking)
                    navController.navigate(
                        Screen.TicketDetail.createRoute(booking.id)
                    )
                }
            )
        }

        composable(Screen.TicketDetail.route) { backStackEntry ->
            val bookingId = backStackEntry.arguments
                ?.getString("bookingId")
                ?: return@composable

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("tickets_graph")
            }

            val vm: TicketsViewModel = hiltViewModel(parentEntry)
            val uiState by vm.uiState.collectAsState()

            // Ưu tiên dùng selectedBooking (đã được lưu khi click)
            // Nếu không có, sẽ tìm từ danh sách bookings
            // Nếu vẫn không có, sẽ load từ repository
            val booking = uiState.selectedBooking
                ?: uiState.bookings.find { it.id == bookingId }
                ?: run {
                    // Load booking nếu không có trong state
                    remember(bookingId) {
                        vm.loadBookingById(bookingId)
                    }
                    null
                }

            if (booking != null) {
                TicketDetailScreen(
                    booking = booking,
                    onBack = { navController.popBackStack() }
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Đang tải thông tin phiếu khám...")
                }
            }
        }
    }
}