package com.example.data.network

import com.example.model.api.ExcelResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface SheetApi {
    @GET("api/v1/{apiKey}")
    suspend fun getSheetExcelData(@Path("apiKey") apiKey: String): List<ExcelResponse>
}
