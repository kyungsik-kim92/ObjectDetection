package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.database.AppDatabase
import com.example.data.database.dao.ExcelDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase = Room.databaseBuilder(
        context, AppDatabase::class.java,
        "app-database"
    ).build()


    @Provides
    @Singleton
    fun provideExcelDao(
        appDatabase: AppDatabase
    ): ExcelDao = appDatabase.excelDao()
}