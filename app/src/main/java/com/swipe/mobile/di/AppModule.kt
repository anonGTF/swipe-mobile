package com.swipe.mobile.di

import com.swipe.mobile.data.repository.DonationRepository
import com.swipe.mobile.data.repository.ReportRepository
import com.swipe.mobile.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository = UserRepository()

    @Provides
    @Singleton
    fun provideReportRepository(): ReportRepository = ReportRepository()

    @Provides
    @Singleton
    fun provideDonationRepository(): DonationRepository = DonationRepository()
}