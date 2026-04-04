package com.example.appointmentschedulingapp.domain.usecase

import com.example.appointmentschedulingapp.di.IoDispatcher
import com.example.appointmentschedulingapp.domain.model.HomeAction
import com.example.appointmentschedulingapp.domain.repository.HomeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetHomeActionsUseCase @Inject constructor(
    private val repository: HomeRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): List<HomeAction> =
        withContext(dispatcher){
             repository.getQuickActions()
    }
}