package com.example.data.source.remote

import com.example.data.network.DictionaryApi
import com.example.data.network.SheetApi
import com.example.model.api.DictionaryResponse
import com.example.model.api.ExcelResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.example.model.common.Result


class SearchWordRemoteDataSourceImpl @Inject constructor(
    private val dictionaryApi: DictionaryApi,
    private val sheetApi: SheetApi
) : SearchWordRemoteDataSource {


    override val excelList: Flow<List<ExcelResponse>>
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