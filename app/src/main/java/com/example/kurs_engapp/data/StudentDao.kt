package com.example.kurs_engapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    @Query("SELECT * FROM students ORDER BY id DESC")
    fun observeStudents(): Flow<List<StudentEntity>>

    @Upsert
    suspend fun upsert(student: StudentEntity)

    @Delete
    suspend fun delete(student: StudentEntity)
}