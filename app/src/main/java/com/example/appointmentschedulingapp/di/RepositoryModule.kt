package com.example.appointmentschedulingapp.di

import com.example.appointmentschedulingapp.data.remote.location.LocationRepositoryImpl
import com.example.appointmentschedulingapp.data.repository.AuthRepositoryImpl
import com.example.appointmentschedulingapp.data.repository.BookingRepositoryImpl
import com.example.appointmentschedulingapp.data.repository.ClinicRepositoryImpl
import com.example.appointmentschedulingapp.data.repository.DoctorRepositoryImpl
import com.example.appointmentschedulingapp.data.repository.HomeRepositoryImpl
import com.example.appointmentschedulingapp.data.repository.PatientProfileRepositoryImpl
import com.example.appointmentschedulingapp.data.repository.UserSessionRepositoryImpl
import com.example.appointmentschedulingapp.domain.payment.CashPaymentProcessor
import com.example.appointmentschedulingapp.domain.payment.momoPayment.MomoPaymentProcessor
import com.example.appointmentschedulingapp.domain.payment.PaymentProcessor
import com.example.appointmentschedulingapp.domain.repository.AuthRepository
import com.example.appointmentschedulingapp.domain.repository.BookingRepository
import com.example.appointmentschedulingapp.domain.repository.ClinicRepository
import com.example.appointmentschedulingapp.domain.repository.DoctorRepository
import com.example.appointmentschedulingapp.domain.repository.HomeRepository
import com.example.appointmentschedulingapp.domain.repository.LocationRepository
import com.example.appointmentschedulingapp.domain.repository.PatientRepository
import com.example.appointmentschedulingapp.domain.repository.UserSessionRepository
import dagger.Module



import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.Binds
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindClinicRepository(
        impl: ClinicRepositoryImpl
    ): ClinicRepository

    @Binds
    @Singleton
    abstract fun bindUserSessionRepository(
        impl: UserSessionRepositoryImpl
    ): UserSessionRepository

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        impl: HomeRepositoryImpl
    ): HomeRepository

    @Binds
    @Singleton
    abstract fun bindBookingRepository(
        impl: BookingRepositoryImpl
    ): BookingRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindDoctorRepository(
        doctorRepositoryImpl: DoctorRepositoryImpl
    ): DoctorRepository

    @Binds
    @Singleton
    abstract fun bindPatientProfileRepository(
        impl: PatientProfileRepositoryImpl
    ): PatientRepository

    @Binds
    @IntoSet
    abstract fun bindCashPaymentProcessor(
        processor: CashPaymentProcessor
    ): PaymentProcessor

    @Binds
    @IntoSet
    abstract fun bindMomoPaymentProcessor(
        processor: MomoPaymentProcessor
    ): PaymentProcessor

    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        impl: LocationRepositoryImpl
    ): LocationRepository
}