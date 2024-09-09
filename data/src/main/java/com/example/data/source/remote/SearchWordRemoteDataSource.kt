package com.example.data.source.remote

import kotlinx.coroutines.flow.Flow

interface SearchWordRemoteDataSource {

    val excelList: Flow<List<com.example.data.network.response.ExcelResponse>>
    suspend fun searchMeanWord(word: String): Result<com.example.data.network.response.DictionaryResponse>


}