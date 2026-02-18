package com.example.kurs_engapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TutorProfileDao {
    @Query("SELECT * FROM tutor_profile WHERE id = 0")
    fun observeProfile(): Flow<TutorProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: TutorProfileEntity)
}