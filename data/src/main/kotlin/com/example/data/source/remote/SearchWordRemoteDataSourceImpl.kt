package com.example.data.source.remote

import com.example.data.network.DictionaryApi
import com.example.data.network.SheetApi
import com.example.model.api.DictionaryResponse
import com.example.model.api.ExcelResponse
import com.example.model.common.Result
import javax.inject.Inject


class SearchWordRemoteDataSourceImpl @Inject constructor(
    private val dictionaryApi: DictionaryApi,
    private val sheetApi: SheetApi
) : SearchWordRemoteDataSource {

    override suspend fun searchMeanWord(word: String): Result<DictionaryResponse> {
        return try {
            val getDictionaryResponse = dictionaryApi.getDictionaryMean(word).execute().body()!!
            Result.Success(getDictionaryResponse)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getExcelData(): List<ExcelResponse> =
        sheetApi.getSheetExcelData()

}