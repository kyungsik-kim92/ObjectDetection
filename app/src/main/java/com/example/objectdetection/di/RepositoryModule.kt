package com.example.objectdetection.di

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
    abstract fun bindFirebaseRepository(firebaseRepositoryImpl: com.example.data.repo.FirebaseRepositoryImpl): com.example.domain.repo.FirebaseRepository

    @Binds
    @Singleton
    abstract fun bindFirebaseRemoteDataSource(firebaseRemoteDataSourceImpl: com.example.data.source.remote.FirebaseRemoteDataSourceImpl): com.example.data.source.remote.FirebaseRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindSearchWordRepository(searchWordRepositoryImpl: com.example.data.repo.SearchWordRepositoryImpl): com.example.domain.repo.SearchWordRepository

    @Singleton
    @Binds
    abstract fun bindSearchWordRemoteDataSource(searchWordRemoteDataSourceImpl: com.example.data.source.remote.SearchWordRemoteDataSourceImpl): com.example.data.source.remote.SearchWordRemoteDataSource

}