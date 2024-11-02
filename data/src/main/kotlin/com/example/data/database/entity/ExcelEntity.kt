package com.example.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.database.DBTable


@Entity(tableName = DBTable.EXCEL_TABLE_NAME)
data class ExcelEntity(
    @PrimaryKey
    val word: String,
    val mean: String
)