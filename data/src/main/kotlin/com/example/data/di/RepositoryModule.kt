package com.example.data.di

import com.example.domain.repo.FirebaseRepository
import com.example.data.repo.FirebaseRepositoryImpl
import com.example.domain.repo.SearchWordRepository
import com.example.data.repo.SearchWordRepositoryImpl
import com.example.data.source.remote.FirebaseRemoteDataSource
import com.example.data.source.remote.FirebaseRemoteDataSourceImpl
import com.example.data.source.remote.SearchWordRemoteDataSource
import com.example.data.source.remote.SearchWordRemoteDataSourceImpl
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
    abstract fun bindFirebaseRepository(firebaseRepositoryImpl: FirebaseRepositoryImpl): FirebaseRepository

    @Binds
    @Singleton
    abstract fun bindFirebaseRemoteDataSource(firebaseRemoteDataSourceImpl: FirebaseRemoteDataSourceImpl): FirebaseRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindSearchWordRepository(searchWordRepositoryImpl: SearchWordRepositoryImpl): SearchWordRepository

    @Singleton
    @Binds
    abstract fun bindSearchWordRemoteDataSource(searchWordRemoteDataSourceImpl: SearchWordRemoteDataSourceImpl): SearchWordRemoteDataSource

}