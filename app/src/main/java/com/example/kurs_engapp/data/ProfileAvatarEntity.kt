package com.example.kurs_engapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile_avatar")
data class ProfileAvatarEntity(
    @PrimaryKey val id: Int = 0,
    val avatarUri: String?
)