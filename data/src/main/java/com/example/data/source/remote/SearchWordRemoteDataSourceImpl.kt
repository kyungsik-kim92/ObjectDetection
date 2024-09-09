package com.example.data.source.remote

import com.example.data.network.response.DictionaryResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.example.data.util.Result

class SearchWordRemoteDataSourceImpl @Inject constructor(
    private val dictionaryApi: com.example.data.network.DictionaryApi,
    private val sheetApi: com.example.data.network.SheetApi
) : SearchWordRemoteDataSource {


    override val excelList: Flow<List<com.example.data.network.response.ExcelResponse>>
        get() = flow { emit(sheetApi.getSheetExcelData()) }


    override suspend fun searchMeanWord(word: String): Result<DictionaryResponse> {
        return try {
            val getDictionaryResponse = dictionaryApi.getDictionaryMean(word).execute().body()!!
            Result.Success(getDictionaryResponse)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}