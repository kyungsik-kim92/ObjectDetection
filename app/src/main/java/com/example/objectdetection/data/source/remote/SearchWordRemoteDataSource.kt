package com.example.objectdetection.data.source.remote

import com.example.objectdetection.network.response.DictionaryResponse
import com.example.objectdetection.network.response.ExcelResponse
import com.example.objectdetection.util.Result
import kotlinx.coroutines.flow.Flow

interface SearchWordRemoteDataSource {

    val excelList : Flow<List<ExcelResponse>>
    suspend fun searchMeanWord(word: String) : Result<DictionaryResponse>


}