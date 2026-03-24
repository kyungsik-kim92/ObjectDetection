package com.example.data.di

import com.example.data.network.DictionaryApi
import com.example.data.network.SheetApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideDictionaryApi(json: Json): DictionaryApi {
        return Retrofit.Builder()
            .baseUrl(DICTIONARY_URL)
            .addConverterFactory(json.asConverterFactory("application/json; charset=UTF-8".toMediaType()))
            .build()
            .create(DictionaryApi::class.java)
    }

    @Singleton
    @Provides
    fun provideSheetApi(json: Json): SheetApi {
        return Retrofit.Builder()
            .baseUrl(SHEET_URL)
            .addConverterFactory(json.asConverterFactory("application/json; charset=UTF-8".toMediaType()))
            .build()
            .create(SheetApi::class.java)
    }

    private const val SHEET_URL = "https://sheetdb.io/"
    private const val DICTIONARY_URL = "https://api.dictionaryapi.dev/"
}