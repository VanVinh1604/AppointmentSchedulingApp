package com.example.appointmentschedulingapp.domain.repository

import com.example.appointmentschedulingapp.domain.model.HomeAction


interface HomeRepository {
    suspend fun getQuickActions(): List<HomeAction>
}