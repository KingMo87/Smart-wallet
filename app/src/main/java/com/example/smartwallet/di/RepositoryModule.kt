package com.example.smartwallet.di

import com.example.smartwallet.data.repository.P2PRepositoryImpl
import com.example.smartwallet.data.repository.ReceiptRepositoryImpl
import com.example.smartwallet.domain.repository.P2PRepository
import com.example.smartwallet.domain.repository.ReceiptRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindReceiptRepository(impl: ReceiptRepositoryImpl): ReceiptRepository

    @Binds
    @Singleton
    abstract fun bindP2PRepository(impl: P2PRepositoryImpl): P2PRepository
}
