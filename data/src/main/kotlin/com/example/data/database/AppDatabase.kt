package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.database.dao.ExcelDao
import com.example.data.database.entity.ExcelEntity


@Database(entities = [ExcelEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun excelDao() :ExcelDao
}