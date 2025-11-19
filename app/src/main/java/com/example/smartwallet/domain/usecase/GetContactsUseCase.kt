package com.example.smartwallet.domain.usecase

import com.example.smartwallet.domain.model.User
import com.example.smartwallet.domain.repository.P2PRepository
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(
    private val repository: P2PRepository
) {
    suspend operator fun invoke(): List<User> = repository.getContacts()
}
