package com.example.objectdetection.di

import com.example.objectdetection.data.repo.FirebaseRepository
import com.example.objectdetection.data.repo.FirebaseRepositoryImpl
import com.example.objectdetection.data.repo.SearchWordRepository
import com.example.objectdetection.data.repo.SearchWordRepositoryImpl
import com.example.objectdetection.data.source.remote.FirebaseRemoteDataSource
import com.example.objectdetection.data.source.remote.FirebaseRemoteDataSourceImpl
import com.example.objectdetection.data.source.remote.SearchWordRemoteDataSource
import com.example.objectdetection.data.source.remote.SearchWordRemoteDataSourceImpl
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