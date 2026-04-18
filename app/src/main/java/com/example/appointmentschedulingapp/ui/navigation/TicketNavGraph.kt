package com.example.appointmentschedulingapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
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

fun NavGraphBuilder.ticketsNavGraph(isLoggedIn: Boolean,navController: NavHostController) {
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
                isLoggedIn = isLoggedIn, // Truyền trạng thái login
                navController = navController,
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
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: return@composable

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("tickets_graph")
            }
            val vm: TicketsViewModel = hiltViewModel(parentEntry)
            val uiState by vm.uiState.collectAsState()

            // 1. Tìm booking từ state hiện tại
            val booking = uiState.selectedBooking ?: uiState.bookings.find { it.id == bookingId }

            // 2. Nếu không tìm thấy, dùng LaunchedEffect để kích hoạt việc load dữ liệu từ Repository
            if (booking == null) {
                LaunchedEffect(bookingId) {
                    vm.loadBookingById(bookingId)
                }
            }

            // 3. Hiển thị UI tương ứng
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
                    // Có thể thêm CircularProgressIndicator ở đây cho đẹp
                    Text("Đang tải thông tin phiếu khám...")
                }
            }
        }
    }
}