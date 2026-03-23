package com.example.data.source.remote

import com.example.data.network.DictionaryApi
import com.example.data.network.SheetApi
import com.example.domain.model.api.DictionaryResponse
import com.example.domain.model.api.ExcelResponse
import javax.inject.Inject


class SearchWordRemoteDataSourceImpl @Inject constructor(
    private val dictionaryApi: DictionaryApi,
    private val sheetApi: SheetApi
) : SearchWordRemoteDataSource {

    override suspend fun searchMeanWord(word: String): DictionaryResponse {
        return try {
            dictionaryApi.getDictionaryMean(word)
        } catch (e: retrofit2.HttpException) {
            if (e.code() == 404) {
                DictionaryResponse()
            } else {
                throw e
            }
        } catch (e: Exception) {
            DictionaryResponse()
        }
    }

    override suspend fun getExcelData(): List<ExcelResponse> =
        sheetApi.getSheetExcelData()

}