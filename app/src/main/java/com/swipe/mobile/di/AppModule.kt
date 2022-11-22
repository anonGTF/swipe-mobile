package com.swipe.mobile.di

import com.swipe.mobile.data.local.Preferences
import com.swipe.mobile.data.remote.ApiService
import com.swipe.mobile.data.remote.RetrofitInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideApi(): ApiService = RetrofitInstance().createApi()

    @Singleton
    @Provides
    fun providePreferences(): Preferences = Preferences.instance
}