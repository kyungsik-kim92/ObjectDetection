package com.example.domain.repo

import com.example.model.api.DictionaryResponse
import com.example.model.api.ExcelResponse
import com.example.model.common.Result
import kotlinx.coroutines.flow.Flow


interface SearchWordRepository {
    val excelList: Flow<List<ExcelResponse>>
    suspend fun searchMeanWord(word: String): Result<DictionaryResponse>
}