package com.example.appointmentschedulingapp.ui.navigation

import androidx.compose.runtime.Composable

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.appointmentschedulingapp.domain.model.User
import com.example.appointmentschedulingapp.ui.features.account.AccountScreen
import com.example.appointmentschedulingapp.ui.features.account.settingContent.PrivacyPolicyScreen
import com.example.appointmentschedulingapp.ui.features.account.settingContent.TermsOfServiceScreen
import com.example.appointmentschedulingapp.ui.features.account.settingContent.TermsOfUseScreen
import com.example.appointmentschedulingapp.ui.features.auth.AuthRoute
import com.example.appointmentschedulingapp.ui.features.auth.AuthViewModel
import com.example.appointmentschedulingapp.ui.features.auth.OtpRoute
import com.example.appointmentschedulingapp.ui.features.clinicDetail.ClinicDetailScreen
import com.example.appointmentschedulingapp.ui.features.doctor.DoctorDetailScreen

import com.example.appointmentschedulingapp.ui.features.home.HomeScreen
import com.example.appointmentschedulingapp.ui.features.notifications.NotificationsScreen
import com.example.appointmentschedulingapp.ui.features.patient.CreatePatientProfileScreen
import com.example.appointmentschedulingapp.ui.features.profile.ProfileDetailScreen
import com.example.appointmentschedulingapp.ui.features.profile.ProfileScreen
@Composable
fun AppNavGraph(navController: NavHostController,
                session: User,
                onLogout: () -> Unit) {

    val authViewModel: AuthViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,

    ) {

        bookingNavGraph(isLoggedIn = session.isLoggedIn, navController = navController)
        ticketsNavGraph(
            navController = navController,
            isLoggedIn = session.isLoggedIn
        )

        // Màn hình Home đã có file riêng
        composable(Screen.Home.route) {
            HomeScreen(
                session = session,
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }

        composable(
            route = "doctor_detail/{doctorId}",
            arguments = listOf(
                navArgument("doctorId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getString("doctorId") ?: ""

            // Gọi màn hình Detail đã thiết kế chuyên nghiệp ở bước trước
            DoctorDetailScreen(
                doctorId = doctorId,
                viewModel = hiltViewModel(),
                onBack = { navController.popBackStack() },
                onBookNow = { id ->
                    // Sau khi chọn bác sĩ, thường sẽ nhảy thẳng vào bước chọn lịch khám
                    navController.navigate("booking_step_1/$id")
                }
            )
        }

//        // Các màn hình khác tạm thời để Text để không bị lỗi Build
//        composable(Screen.Ticket.route) {
//            TicketsScreen()
//        }

        composable(Screen.Account.route) {
            AccountScreen(
                isLoggedIn = session.isLoggedIn,
                phoneNumber = session.phoneNumber,
                onLogout = onLogout,

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
            AuthRoute(
                viewModel = authViewModel,
                onBack = { navController.popBackStack() },
                onNavigateOtp = { phone ->
                    navController.navigate("otp_verification/$phone")
                }
            )
        }

        composable(
            route = "otp_verification/{phoneNumber}",
            arguments = listOf(navArgument("phoneNumber") {
                type = NavType.StringType
            })
        ) {
            OtpRoute(
                viewModel = authViewModel,
                onBack = { navController.popBackStack() },
                onSuccess = {
                    val hasPreviousScreen = navController.previousBackStackEntry != null

                    if (hasPreviousScreen) {
                        navController.popBackStack(
                            route = Screen.Auth.route,
                            inclusive = true
                        )
                    } else {
                        // Nếu không có backstack (vào thẳng Login), về Home
                        navController.navigate(Screen.Home.route) {
                            popUpTo(0)
                        }
                    }
                }
            )
        }

        composable(
            route = Screen.ClinicDetail.route,
            arguments = listOf(
                navArgument("clinicId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val clinicId = backStackEntry.arguments?.getString("clinicId") ?: ""

            ClinicDetailScreen(
                clinicId = clinicId,
                onBack = { navController.popBackStack() },
                onBookNow = { id ->  // ✅ nhận id từ lambda parameter
                    // ✅ Navigate vào booking_graph trước, rồi mới đến route con
                    navController.navigate("booking_graph") {
                        launchSingleTop = true
                    }
                    navController.navigate("booking_step1_with_clinic/$id")
                }
            )
        }

        composable(Screen.CreatePatientProfile.route) {
            CreatePatientProfileScreen(
                onBack = { isCreated ->
                    if (isCreated) {
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("patient_profile_created", true)
                    }
                    navController.popBackStack()
                }
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
            ProfileScreen(
                onNavigateToCreateProfile = {
                    navController.navigate(Screen.CreatePatientProfile.route)
                },
                onNavigateToDetail = { profileId ->           // ← thêm
                    navController.navigate(Screen.ProfileDetail.createRoute(profileId))
                },
                isLoggedIn = session.isLoggedIn,
                navController = navController
            )
        }

        composable(
            route = Screen.ProfileDetail.route,
            arguments = listOf(navArgument("profileId") { type = NavType.StringType })
        ) {
            ProfileDetailScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}