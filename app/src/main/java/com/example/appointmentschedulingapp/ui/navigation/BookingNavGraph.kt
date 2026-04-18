package com.example.appointmentschedulingapp.ui.navigation


import android.widget.Toast

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.appointmentschedulingapp.ui.features.booking.*
import com.example.appointmentschedulingapp.ui.features.booking.steps.BookingStep1Screen
import com.example.appointmentschedulingapp.ui.features.booking.steps.BookingStep2Screen
import com.example.appointmentschedulingapp.ui.features.booking.steps.BookingStep3Screen
import com.example.appointmentschedulingapp.ui.features.booking.steps.BookingStep4Screen
import com.example.appointmentschedulingapp.ui.features.booking.steps.BookingReceiptScreen


fun NavGraphBuilder.bookingNavGraph(
    isLoggedIn: Boolean,
    navController: NavHostController
) {
    navigation(
        route = "booking_graph",
        startDestination = Screen.SelectClinic.route
    ) {
        composable(Screen.SelectClinic.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("booking_graph")
            }

            val bookingViewModel: BookingViewModel = hiltViewModel(parentEntry)

            SelectClinicScreen(
                bookingViewModel = bookingViewModel,
                onBack = { navController.popBackStack() },
                onNavigateToDetail = { clinicId ->
                    navController.navigate(Screen.ClinicDetail.createRoute(clinicId))
                },
                onNavigateToBookingStep1 = {
                    navController.navigate(Screen.BookingStep1.route)
                }
            )
        }

        composable(Screen.BookingStep1.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("booking_graph")
            }

            val bookingViewModel: BookingViewModel = hiltViewModel(parentEntry)
            val uiState by bookingViewModel.uiState.collectAsState()

            BookingStep1Screen(
                uiState = uiState,
                onBack = { navController.popBackStack() },
                onNext = {
                    navController.navigate("booking_step2")
                },
                onEvent = bookingViewModel::onEvent
            )
        }

        composable(Screen.BookingStep2.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("booking_graph")
            }

            val bookingViewModel: BookingViewModel = hiltViewModel(parentEntry)
            val uiState by bookingViewModel.uiState.collectAsState()

            val created =
                backStackEntry.savedStateHandle
                    .getStateFlow("patient_profile_created", false)
                    .collectAsState()

            LaunchedEffect(created.value) {
                if (created.value) {
                    bookingViewModel.loadPatientProfiles()
                    backStackEntry.savedStateHandle["patient_profile_created"] = false
                }
            }

            BookingStep2Screen(
                uiState = uiState,
                onLoadPatients = { bookingViewModel.loadPatientProfiles() },
                onPatientSelected = { id, name ->
                    bookingViewModel.onEvent(
                        BookingEvent.SelectPatient(id, name)
                    )
                    navController.navigate(Screen.BookingStep3.route)
                },
                onBack = { navController.popBackStack() },
                onCreatePatient = {
                    navController.navigate(Screen.CreatePatientProfile.route)
                },
                isLoggedIn = isLoggedIn,
                navController = navController
            )
        }

        composable(Screen.BookingStep3.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("booking_graph")
            }

            val bookingViewModel: BookingViewModel = hiltViewModel(parentEntry)
            val uiState by bookingViewModel.uiState.collectAsState()

            BookingStep3Screen(
                uiState = uiState,
                onBack = {
                    navController.popBackStack()
                },
                onConfirm = {
                    navController.navigate(Screen.BookingStep4.route)
                }

            )
        }


        composable(
            route = Screen.BookingStep4.route
            // ✅ Bỏ hết deepLinks và arguments
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("booking_graph")
            }
            val bookingViewModel: BookingViewModel = hiltViewModel(parentEntry)
            val uiState by bookingViewModel.uiState.collectAsState()

            // ✅ Bỏ orderId và hasProcessedResult — không cần nữa
            // ✅ Bỏ LaunchedEffect(orderId) — MomoCallbackBus trong ViewModel xử lý

            // Khi isSuccess thay đổi, chuyển trang sang Receipt
            LaunchedEffect(uiState.isSuccess, uiState.errorMessage) {

                val isSuccess = uiState.isSuccess
                val isError = uiState.errorMessage != null

                // 🔥 CHẶN navigate nếu có lỗi (cancel)
                if (isSuccess && !isError) {
                    android.util.Log.d("BookingNavGraph", "Payment verified! Navigating to receipt")

                    navController.navigate(Screen.BookingReceipt.route) {
                        popUpTo("booking_graph") { inclusive = false }
                        launchSingleTop = true
                    }
                }
            }

            BookingStep4Screen(
                uiState = uiState,
                onBack = { navController.popBackStack() },
                onPaymentSelected = { method -> bookingViewModel.onEvent(BookingEvent.SelectPaymentMethod(method)) },
                onConfirmPayment = { bookingViewModel.onEvent(BookingEvent.ConfirmBooking) },
                // bookingNavGraph.kt
                onOpenPaymentUrl = { url ->
                    try {
                        val intent = androidx.browser.customtabs.CustomTabsIntent.Builder().build()
                        intent.launchUrl(navController.context, android.net.Uri.parse(url))
                    } catch (e: Exception) {
                        Toast.makeText(navController.context, "Không thể mở trang thanh toán", Toast.LENGTH_LONG).show()
                    }
                },
                onNavigateToReceipt = {
                    navController.navigate(Screen.BookingReceipt.route) {
                        popUpTo("booking_step1") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                // Trong NavGraph / nơi gọi BookingStep4Screen:
                onVerifyPayment = { bookingId ->
                    // Không có resultCode → chỉ observe Firebase để kiểm tra trạng thái
                    bookingViewModel.checkPendingPayment(bookingId)
                }
            )
        }

        composable(Screen.BookingReceipt.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("booking_graph")
            }

            val bookingViewModel: BookingViewModel = hiltViewModel(parentEntry)
            val uiState by bookingViewModel.uiState.collectAsState()

            BookingReceiptScreen(
                uiState = uiState,
                bookingId = uiState.bookingId,
                onBackHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0)
                    }
                },
                onViewBooking = {
//                    navController.navigate(Screen.BookingHistory.route)
                }
            )
        }


    }
}