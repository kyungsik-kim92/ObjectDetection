package com.example.objectdetection.data.source.remote

import com.example.objectdetection.network.DictionaryApi
import com.example.objectdetection.network.response.DictionaryResponse
import com.example.objectdetection.util.Result
import javax.inject.Inject

class SearchWordRemoteDataSourceImpl @Inject constructor(
    private val dictionaryApi: DictionaryApi
) : SearchWordRemoteDataSource {
    override suspend fun searchMeanWord(word: String): Result<DictionaryResponse> {
        return try {
            val getDictionaryResponse = dictionaryApi.getDictionaryMean(word).execute().body()!!
            Result.Success(getDictionaryResponse)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}