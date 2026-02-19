package com.example.kurs_engapp.di

import android.content.Context
import androidx.room.Room
import com.example.kurs_engapp.data.AppDatabase
import com.example.kurs_engapp.data.ProfileRepository
import com.example.kurs_engapp.data.StudentsRepository
import com.example.kurs_engapp.data.StudentDao
import com.example.kurs_engapp.data.TutorProfileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "eng_app_database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideStudentDao(db: AppDatabase): StudentDao = db.studentDao()

    @Provides
    fun provideTutorProfileDao(db: AppDatabase): TutorProfileDao = db.tutorProfileDao()

    @Provides
    @Singleton
    fun provideStudentsRepository(studentDao: StudentDao): StudentsRepository =
        StudentsRepository(studentDao)

    @Provides
    @Singleton
    fun provideProfileRepository(tutorProfileDao: TutorProfileDao): ProfileRepository =
        ProfileRepository(tutorProfileDao)
}