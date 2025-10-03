package com.example.objectdetection.di

import com.example.data.database.ApiKeyProvider
import com.example.objectdetection.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiKeyModule {

    @Provides
    @Singleton
    fun provideApiKey(): ApiKeyProvider {
        return object : ApiKeyProvider {
            override fun getApiKey(): String = BuildConfig.GOOGLE_SHEETS_API_KEY
        }
    }
}