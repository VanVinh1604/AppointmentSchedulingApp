package com.example.appointmentschedulingapp.di

import com.example.appointmentschedulingapp.domain.repository.LocationRepository
import com.example.appointmentschedulingapp.domain.usecase.location.GetProvincesUseCase
import com.example.appointmentschedulingapp.domain.usecase.location.GetWardsUseCase
import com.example.appointmentschedulingapp.domain.usecase.location.LocationUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideLocationUseCases(
        repository: LocationRepository
    ): LocationUseCases {
        return LocationUseCases(
            getProvinces = GetProvincesUseCase(repository),
            getWards = GetWardsUseCase(repository)
        )
    }
}