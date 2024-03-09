package com.example.objectdetection.network

import com.example.objectdetection.network.response.ExcelResponse
import retrofit2.http.GET

interface SheetApi {

    companion object {
        private const val URL = "api/v1/ahfewsnq0tnw0"

    }

    @GET(URL)
    suspend fun getSheetExcelData(): List<ExcelResponse>
}
