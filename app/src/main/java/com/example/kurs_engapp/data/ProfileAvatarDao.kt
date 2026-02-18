package com.example.kurs_engapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileAvatarDao {
    @Query("SELECT avatarUri FROM profile_avatar WHERE id = 0")
    fun observeAvatarUri(): Flow<String?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: ProfileAvatarEntity)
}