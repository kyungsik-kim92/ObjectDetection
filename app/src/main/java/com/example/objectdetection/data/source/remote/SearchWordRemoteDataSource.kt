package com.example.objectdetection.data.source.remote

import com.example.objectdetection.network.response.DictionaryResponse
import com.example.objectdetection.util.Result

interface SearchWordRemoteDataSource {

    suspend fun searchMeanWord(word: String) : Result<DictionaryResponse>


}