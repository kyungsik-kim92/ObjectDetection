package com.example.data.source.local

import com.example.data.database.entity.ExcelEntity
import kotlinx.coroutines.flow.Flow

interface SearchWordLocalDataSource {

    val excelList: Flow<List<ExcelEntity>>

    fun inserts(list: List<ExcelEntity>)
}