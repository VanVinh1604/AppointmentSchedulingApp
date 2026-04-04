package com.example.appointmentschedulingapp.di

import android.content.Context
import com.example.appointmentschedulingapp.data.local.AppDatabase
import com.example.appointmentschedulingapp.common.Config
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return AppDatabase.getInstance(context)
    }

//    @Provides
//    fun provideEventDao(db: AppDatabase) = db.eventDao()

    @Provides
    fun provideClinicDao(db: AppDatabase) = db.clinicDao()

    @Provides
    fun provideDoctorDao(db: AppDatabase) = db.doctorDao()
}