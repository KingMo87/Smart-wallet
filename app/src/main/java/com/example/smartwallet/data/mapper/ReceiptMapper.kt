package com.example.smartwallet.data.mapper

import com.example.smartwallet.data.local.entity.ReceiptEntity
import com.example.smartwallet.domain.model.Receipt

fun ReceiptEntity.toDomain(): Receipt = Receipt(
    id = id,
    merchantName = merchantName,
    date = date,
    amount = amount,
    currency = currency,
    category = category,
    paymentMethod = paymentMethod,
    imageUri = imageUri,
    notes = notes,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Receipt.toEntity(): ReceiptEntity = ReceiptEntity(
    id = id,
    merchantName = merchantName,
    date = date,
    amount = amount,
    currency = currency,
    category = category,
    paymentMethod = paymentMethod,
    imageUri = imageUri,
    notes = notes,
    createdAt = createdAt,
    updatedAt = updatedAt
)
