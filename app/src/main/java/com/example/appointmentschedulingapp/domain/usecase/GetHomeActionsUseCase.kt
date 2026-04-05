package com.example.appointmentschedulingapp.domain.usecase

import com.example.appointmentschedulingapp.domain.model.HomeAction
import com.example.appointmentschedulingapp.domain.repository.HomeRepository
import javax.inject.Inject

class GetHomeActionsUseCase @Inject constructor(
    private val repository: HomeRepository,
) {
    suspend operator fun invoke(): List<HomeAction> {
             return repository.getQuickActions()

    }
}