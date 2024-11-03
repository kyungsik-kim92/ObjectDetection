package com.example.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.database.DBTable
import com.example.model.WordItem
import com.example.model.api.ExcelResponse


@Entity(tableName = DBTable.EXCEL_TABLE_NAME)
data class ExcelEntity(
    @PrimaryKey
    val word: String,
    val mean: String
) {
    fun toWordItem(): WordItem =
        WordItem(word, mean)
}

fun ExcelResponse.toExcelEntity(): ExcelEntity =
    ExcelEntity(word, mean)