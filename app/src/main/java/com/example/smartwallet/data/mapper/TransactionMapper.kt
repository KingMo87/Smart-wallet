package com.example.smartwallet.data.mapper

import com.example.smartwallet.data.local.entity.TransactionEntity
import com.example.smartwallet.domain.model.Transaction

fun TransactionEntity.toDomain(): Transaction = Transaction(
    id = id,
    fromUserId = fromUserId,
    toUserId = toUserId,
    amount = amount,
    currency = currency,
    status = status,
    createdAt = createdAt,
    description = description
)

fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
    id = id,
    fromUserId = fromUserId,
    toUserId = toUserId,
    amount = amount,
    currency = currency,
    status = status,
    createdAt = createdAt,
    description = description
)
