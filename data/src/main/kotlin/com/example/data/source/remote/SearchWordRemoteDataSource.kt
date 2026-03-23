package com.example.data.source.remote

import com.example.domain.model.api.DictionaryResponse
import com.example.domain.model.api.ExcelResponse

interface SearchWordRemoteDataSource {
    suspend fun searchMeanWord(word: String): DictionaryResponse
    suspend fun getExcelData(): List<ExcelResponse>
}