package com.example.kurs_engapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tutor_profile")
data class TutorProfileEntity(
    @PrimaryKey val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val subject: String,
    val experience: String,
    val level: String,
    val avatarUri: String?
)