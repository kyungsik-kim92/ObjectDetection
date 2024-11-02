package com.example.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.database.DBTable
import com.example.data.database.entity.ExcelEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ExcelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserts(vararg entity: ExcelEntity)

    @Query("SELECT * FROM ${DBTable.EXCEL_TABLE_NAME}")
    fun getAll(): Flow<List<ExcelEntity>>

}