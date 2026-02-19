package com.example.kurs_engapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val firstName: String,
    val lastName: String,
    val currentLevel: String,
    val goal: String,
    val phoneNumber: String,
    val lessonDateTime: String,
    val avatarUri: String?
)