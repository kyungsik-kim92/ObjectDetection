package com.example.data.source.remote

import com.example.model.api.DictionaryResponse
import com.example.model.api.ExcelResponse
import com.example.model.common.Result

interface SearchWordRemoteDataSource {
    suspend fun searchMeanWord(word: String): Result<DictionaryResponse>
    suspend fun getExcelData(): List<ExcelResponse>
}