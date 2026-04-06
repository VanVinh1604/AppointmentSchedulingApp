package com.example.appointmentschedulingapp.data.repository

import android.app.Activity
import com.example.appointmentschedulingapp.di.IoDispatcher
import com.example.appointmentschedulingapp.domain.repository.AuthRepository
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : AuthRepository {

    override suspend fun sendOtp(
        activity: Activity,
        phone: String
    ): Result<String> = withContext(dispatcher) {
        try {
            callbackFlow<Result<String>> {
                val callbacks =
                    object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                        override fun onVerificationCompleted(
                            credential: com.google.firebase.auth.PhoneAuthCredential
                        ) {
                            // auto verify nếu Firebase đọc SMS được
                        }

                        override fun onVerificationFailed(
                            e: FirebaseException
                        ) {
                            trySend(Result.failure(e))
                        }

                        override fun onCodeSent(
                            verificationId: String,
                            token: PhoneAuthProvider.ForceResendingToken
                        ) {
                            trySend(Result.success(verificationId))
                        }
                    }

                val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                    .setPhoneNumber("+84$phone")
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(activity)
                    .setCallbacks(callbacks)
                    .build()

                PhoneAuthProvider.verifyPhoneNumber(options)

                awaitClose { }
            }.first()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyOtp(
        verificationId: String,
        otp: String
    ): Result<Boolean> = withContext(dispatcher) {
        try {
            val credential = PhoneAuthProvider.getCredential(
                verificationId,
                otp
            )

            firebaseAuth.signInWithCredential(credential).await()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}