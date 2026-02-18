package com.example.kurs_engapp.data

import com.example.kurs_engapp.model.TutorProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfileRepository(private val tutorProfileDao: TutorProfileDao) {

    fun observeProfile(): Flow<TutorProfile?> = tutorProfileDao.observeProfile().map { entity ->
        entity?.toModel()
    }

    suspend fun saveProfile(profile: TutorProfile) {
        tutorProfileDao.upsert(profile.toEntity())
    }
}

private fun TutorProfileEntity.toModel(): TutorProfile = TutorProfile(
    firstName = firstName,
    lastName = lastName,
    middleName = middleName,
    subject = subject,
    experience = experience,
    level = level,
    avatarUri = avatarUri
)

private fun TutorProfile.toEntity(): TutorProfileEntity = TutorProfileEntity(
    firstName = firstName,
    lastName = lastName,
    middleName = middleName,
    subject = subject,
    experience = experience,
    level = level,
    avatarUri = avatarUri
)