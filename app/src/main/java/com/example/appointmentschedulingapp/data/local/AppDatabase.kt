package com.example.appointmentschedulingapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.appointmentschedulingapp.common.Config
import com.example.appointmentschedulingapp.data.local.dao.ClinicDao
import com.example.appointmentschedulingapp.data.local.dao.DoctorDao
import com.example.appointmentschedulingapp.data.local.entity.ClinicEntity
import com.example.appointmentschedulingapp.data.local.entity.DoctorEntity

@Database(
    entities = [
        ClinicEntity::class,
        DoctorEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun clinicDao(): ClinicDao
    abstract fun doctorDao(): DoctorDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    Config.DATABASE_NAME
                ).build().also { INSTANCE = it }
            }
        }
    }
}