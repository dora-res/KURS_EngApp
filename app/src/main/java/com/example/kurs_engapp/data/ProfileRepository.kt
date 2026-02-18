package com.example.kurs_engapp.data

import kotlinx.coroutines.flow.Flow

class ProfileRepository(private val profileAvatarDao: ProfileAvatarDao) {
    fun observeAvatarUri(): Flow<String?> = profileAvatarDao.observeAvatarUri()

    suspend fun saveAvatarUri(uri: String?) {
        profileAvatarDao.upsert(ProfileAvatarEntity(avatarUri = uri))
    }
}