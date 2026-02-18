package com.example.kurs_engapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProfileAvatarEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileAvatarDao(): ProfileAvatarDao
}