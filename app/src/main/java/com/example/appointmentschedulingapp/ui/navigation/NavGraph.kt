package com.example.appointmentschedulingapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.appointmentschedulingapp.ui.features.account.AccountScreen
import com.example.appointmentschedulingapp.ui.features.account.settingContent.PrivacyPolicyScreen
import com.example.appointmentschedulingapp.ui.features.account.settingContent.TermsOfServiceScreen
import com.example.appointmentschedulingapp.ui.features.account.settingContent.TermsOfUseScreen
import com.example.appointmentschedulingapp.ui.features.auth.AuthScreen
import com.example.appointmentschedulingapp.ui.features.auth.OtpVerificationScreen
import com.example.appointmentschedulingapp.ui.features.booking.BookingEvent
import com.example.appointmentschedulingapp.ui.features.booking.BookingViewModel
import com.example.appointmentschedulingapp.ui.features.booking.steps.BookingStep1Screen
import com.example.appointmentschedulingapp.ui.features.booking.ClinicDetailScreen
import com.example.appointmentschedulingapp.ui.features.booking.SelectClinicScreen
import com.example.appointmentschedulingapp.ui.features.booking.steps.BookingStep2Screen
import com.example.appointmentschedulingapp.ui.features.home.HomeScreen
import com.example.appointmentschedulingapp.ui.features.notifications.NotificationsScreen
import com.example.appointmentschedulingapp.ui.features.profile.ProfileScreen
import com.example.appointmentschedulingapp.ui.features.tickets.TicketsScreen

@Composable
fun AppNavGraph(navController: NavHostController) {

//    val bookingViewModel: BookingViewModel = viewModel()
    val sharedBookingViewModel: BookingViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        bookingNavGraph(navController)

        // Màn hình Home đã có file riêng
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }

//        composable(Screen.SelectClinic.route) {
//            // Sử dụng sharedBookingViewModel thay vì gọi lại
//            SelectClinicScreen(
//                bookingViewModel = sharedBookingViewModel, // Đảm bảo SelectClinicScreen có nhận tham số viewModel
//                onBack = { navController.popBackStack() },
//                onNavigateToDetail = { clinicId ->
//                    navController.navigate(Screen.ClinicDetail.createRoute(clinicId))
//                },
//                onNavigateToBookingStep1 = { navController.navigate(Screen.BookingStep1.route) }
//            )
//        }

//        composable(
//            route = Screen.ClinicDetail.route,
//            arguments = listOf(
//                navArgument("clinicId") {
//                    type = NavType.StringType
//                }
//            )
//        ) { backStackEntry ->
//
//            val clinicId = backStackEntry.arguments?.getString("clinicId") ?: ""
//
//            ClinicDetailScreen(
//                clinicId = clinicId,
//                onBack = { navController.popBackStack() }
//            )
//        }

//        composable(Screen.BookingStep1.route) {
//            val uiState by sharedBookingViewModel.uiState.collectAsState()
//
//            LaunchedEffect(Unit) {
//                sharedBookingViewModel.onEvent(
//                    BookingEvent.SetStep(1)
//                )
//            }
//            BookingStep1Screen(
//                uiState = uiState,
//                onBack = { navController.popBackStack() },
//                onNext = {
//                    sharedBookingViewModel.onEvent(
//                        BookingEvent.SetStep(2)
//                    )
//                    navController.navigate("booking_step2")
//                },
//                onUpdateSpecialty = { specialty ->
//                    sharedBookingViewModel.onEvent(
//                        BookingEvent.UpdateSpecialty(specialty)
//                    )
//                }
//            )
//        }
//
//        composable("booking_step2") {
//            val uiState by sharedBookingViewModel.uiState.collectAsState()
//
//            LaunchedEffect(Unit) {
//                sharedBookingViewModel.onEvent(
//                    BookingEvent.SetStep(2)
//                )
//            }
//            BookingStep2Screen(
//                uiState = uiState,
//                onPatientSelected = { id, name ->
//                    sharedBookingViewModel.onEvent(
//                        BookingEvent.SelectPatient(id, name)
//                    )
//                },
//                onBack = { navController.popBackStack() },
//                onNext = { /* Điều hướng sang bước 3 */ }
//            )
//        }

        // Các màn hình khác tạm thời để Text để không bị lỗi Build
        composable(Screen.Ticket.route) {
            TicketsScreen()
        }

        composable(Screen.Account.route) {
            AccountScreen(
                // Truyền hàm điều hướng vào AccountScreen
                onNavigateToTerms = {
                    navController.navigate(Screen.TermsOfUse.route)
                },
                onNavigateToPrivacy = {
                    navController.navigate(Screen.PrivatePolicy.route)
                },
                onNavigateToService = {
                    navController.navigate(Screen.TermOfService.route)
                },

                onNavigateToAuth = {
                    navController.navigate(Screen.Auth.route)
                }
            )
        }


        // Trong AppNavGraph
        composable(Screen.Auth.route) {
            AuthScreen(
                onBack = { navController.popBackStack() },
                onContinue = { phone ->
                    // Điều hướng sang màn OTP và gửi kèm số điện thoại
                    navController.navigate("otp_verification/$phone")
                }
            )
        }

        composable(
            route = "otp_verification/{phoneNumber}",
            arguments = listOf(navArgument("phoneNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            OtpVerificationScreen(
                phoneNumber = phone,
                onBack = { navController.popBackStack() },
                onVerify = { code ->
                    // Sau khi xác thực thành công, quay về Home hoặc Account
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                },
                onResendOtp = { /* Xử lý gửi lại mã */ }
            )
        }
        composable(Screen.TermOfService.route) {
            TermsOfServiceScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.TermsOfUse.route) {
            // SỬA TẠI ĐÂY: Chỉ cần onBack, xóa cái onNavigateToTerms thừa đi
            TermsOfUseScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.PrivatePolicy.route) {
            PrivacyPolicyScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Notification.route) {
            NotificationsScreen()
        }

        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
}