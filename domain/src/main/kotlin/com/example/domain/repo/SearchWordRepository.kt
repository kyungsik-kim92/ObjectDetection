package com.example.domain.repo

import com.example.domain.model.WordItem
import com.example.domain.model.api.DictionaryResponse
import kotlinx.coroutines.flow.Flow


interface SearchWordRepository {
    val excelList: Flow<List<WordItem>>
    suspend fun updateExcelList()
    suspend fun searchMeanWord(word: String): DictionaryResponse
}