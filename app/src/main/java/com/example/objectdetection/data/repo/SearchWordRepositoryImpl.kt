package com.example.objectdetection.data.repo

import com.example.objectdetection.data.source.remote.SearchWordRemoteDataSource
import com.example.objectdetection.network.response.DictionaryResponse
import com.example.objectdetection.network.response.ExcelResponse
import com.example.objectdetection.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchWordRepositoryImpl @Inject constructor(
    private val searchWordRemoteDataSource: SearchWordRemoteDataSource
) : SearchWordRepository {

    override val excelList : Flow<List<ExcelResponse>>
        get() = searchWordRemoteDataSource.excelList

    override suspend fun searchMeanWord(word: String): Result<DictionaryResponse> =
        searchWordRemoteDataSource.searchMeanWord(word)

}