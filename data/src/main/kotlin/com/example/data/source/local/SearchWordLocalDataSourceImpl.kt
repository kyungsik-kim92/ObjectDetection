package com.example.data.source.local

import com.example.data.database.dao.ExcelDao
import com.example.data.database.entity.ExcelEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchWordLocalDataSourceImpl @Inject constructor(
    private val excelDao: ExcelDao
) : SearchWordLocalDataSource {
    override val excelList: Flow<List<ExcelEntity>>
        get() = excelDao.getAll()

    override fun inserts(list: List<ExcelEntity>) {
        excelDao.inserts(*list.toTypedArray())
    }
}