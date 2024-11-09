package com.example.domain.repo

import com.example.model.WordItem
import com.example.model.api.DictionaryResponse
import com.example.model.common.Result
import kotlinx.coroutines.flow.Flow


interface SearchWordRepository {
    val excelList: Flow<List<WordItem>>
    suspend fun updateExcelList()
    suspend fun searchMeanWord(word: String): Result<DictionaryResponse>
}