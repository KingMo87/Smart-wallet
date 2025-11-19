package com.example.smartwallet.di

import com.example.smartwallet.data.remote.FakeP2PApiService
import com.example.smartwallet.data.remote.P2PApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideP2PApiService(): P2PApiService = FakeP2PApiService()
}
