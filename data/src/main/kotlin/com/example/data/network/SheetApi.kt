package com.example.data.network

import com.example.model.api.ExcelResponse
import retrofit2.http.GET

interface SheetApi {
    companion object {
        private const val URL = "api/v1/1mbyz5l2rbbdk"
    }

    @GET(URL)
    suspend fun getSheetExcelData(): List<ExcelResponse>
}
