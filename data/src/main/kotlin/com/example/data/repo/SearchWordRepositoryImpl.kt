package com.example.data.repo


import com.example.data.source.remote.SearchWordRemoteDataSource
import com.example.domain.repo.SearchWordRepository
import com.example.model.api.DictionaryResponse
import com.example.model.api.ExcelResponse
import com.example.model.common.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchWordRepositoryImpl @Inject constructor(
    private val searchWordRemoteDataSource: SearchWordRemoteDataSource
) : SearchWordRepository {

    override val excelList: Flow<List<ExcelResponse>>
        get() = searchWordRemoteDataSource.excelList

    override suspend fun searchMeanWord(word: String): Result<DictionaryResponse> =
        searchWordRemoteDataSource.searchMeanWord(word)

}