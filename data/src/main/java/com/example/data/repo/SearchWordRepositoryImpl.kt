package com.example.data.repo

import com.example.data.source.remote.SearchWordRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchWordRepositoryImpl @Inject constructor(
    private val searchWordRemoteDataSource: SearchWordRemoteDataSource
) : com.example.domain.repo.SearchWordRepository {

    override val excelList: Flow<List<com.example.data.network.response.ExcelResponse>>
        get() = searchWordRemoteDataSource.excelList

    override suspend fun searchMeanWord(word: String): Result<com.example.data.network.response.DictionaryResponse> =
        searchWordRemoteDataSource.searchMeanWord(word)

}