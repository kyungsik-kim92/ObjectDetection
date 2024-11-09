package com.example.data.repo


import com.example.data.database.entity.toExcelEntity
import com.example.data.source.local.SearchWordLocalDataSource
import com.example.data.source.remote.SearchWordRemoteDataSource
import com.example.domain.repo.SearchWordRepository
import com.example.model.WordItem
import com.example.model.api.DictionaryResponse
import com.example.model.common.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchWordRepositoryImpl @Inject constructor(
    private val searchWordRemoteDataSource: SearchWordRemoteDataSource,
    private val searchWordLocalDataSource: SearchWordLocalDataSource
) : SearchWordRepository {

    override val excelList: Flow<List<WordItem>>
        get() = searchWordLocalDataSource.excelList.map { list ->
            list.map { item -> item.toWordItem() }
        }

    override suspend fun updateExcelList() {
        searchWordLocalDataSource.inserts(
            searchWordRemoteDataSource.getExcelData().map { it.toExcelEntity() })

    }

    override suspend fun searchMeanWord(word: String): Result<DictionaryResponse> =
        searchWordRemoteDataSource.searchMeanWord(word)

}