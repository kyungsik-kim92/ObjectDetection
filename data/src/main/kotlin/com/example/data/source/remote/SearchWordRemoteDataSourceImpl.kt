package com.example.data.source.remote

import com.example.data.database.ApiKeyProvider
import com.example.data.network.DictionaryApi
import com.example.data.network.SheetApi
import com.example.model.api.DictionaryResponse
import com.example.model.api.ExcelResponse
import javax.inject.Inject


class SearchWordRemoteDataSourceImpl @Inject constructor(
    private val dictionaryApi: DictionaryApi,
    private val sheetApi: SheetApi,
    private val apiKeyProvider: ApiKeyProvider
) : SearchWordRemoteDataSource {

    override suspend fun searchMeanWord(word: String): DictionaryResponse =
        dictionaryApi.getDictionaryMean(word)

    override suspend fun getExcelData(): List<ExcelResponse> =
        sheetApi.getSheetExcelData(apiKeyProvider.getApiKey())

}