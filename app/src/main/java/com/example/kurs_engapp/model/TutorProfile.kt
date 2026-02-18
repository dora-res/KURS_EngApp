package com.example.kurs_engapp.model

data class TutorProfile(
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val subject: String,
    val experience: String,
    val level: String,
    val avatarUri: String?
)