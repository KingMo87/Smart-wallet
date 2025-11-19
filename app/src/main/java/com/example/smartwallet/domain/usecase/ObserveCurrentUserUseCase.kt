package com.example.smartwallet.domain.usecase

import com.example.smartwallet.domain.model.User
import com.example.smartwallet.domain.repository.P2PRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCurrentUserUseCase @Inject constructor(
    private val repository: P2PRepository
) {
    operator fun invoke(): Flow<User> = repository.currentUser
}
