package com.example.smartwallet.domain.model

data class User(
    val id: String,
    val displayName: String,
    val phoneNumber: String,
    val avatarUrl: String?,
    val balance: Double
)
