package com.example.kurs_engapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TutorProfileEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tutorProfileDao(): TutorProfileDao
}