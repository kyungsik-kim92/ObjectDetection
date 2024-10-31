package com.example.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class ExcelEntity(
    @PrimaryKey
    val word: String,
    val mean: String
)