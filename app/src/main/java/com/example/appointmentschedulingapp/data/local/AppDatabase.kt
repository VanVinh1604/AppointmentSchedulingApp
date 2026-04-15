package com.example.appointmentschedulingapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.appointmentschedulingapp.common.Config
import com.example.appointmentschedulingapp.data.local.dao.BookingDao
import com.example.appointmentschedulingapp.data.local.dao.ClinicDao
import com.example.appointmentschedulingapp.data.local.dao.DoctorDao
import com.example.appointmentschedulingapp.data.local.dao.LocationDao
import com.example.appointmentschedulingapp.data.local.dao.NotificationDao
import com.example.appointmentschedulingapp.data.local.dao.PatientProfileDao
import com.example.appointmentschedulingapp.data.local.entity.BookingEntity
import com.example.appointmentschedulingapp.data.local.entity.ClinicEntity
import com.example.appointmentschedulingapp.data.local.entity.DoctorEntity
import com.example.appointmentschedulingapp.data.local.entity.NotificationEntity
import com.example.appointmentschedulingapp.data.local.entity.PatientProfileEntity
import com.example.appointmentschedulingapp.data.local.entity.location.ProvinceEntity
import com.example.appointmentschedulingapp.data.local.entity.location.WardEntity

@Database(
    entities = [
        ClinicEntity::class,
        DoctorEntity::class,
        PatientProfileEntity::class,
        BookingEntity::class,
        ProvinceEntity::class,
        WardEntity::class,
        NotificationEntity::class
    ],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun clinicDao(): ClinicDao
    abstract fun doctorDao(): DoctorDao

    abstract fun patientProfileDao(): PatientProfileDao
    abstract fun bookingDao(): BookingDao

    abstract fun locationDao(): LocationDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    Config.DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}