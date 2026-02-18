package com.example.kurs_engapp.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kurs_engapp.data.ProfileRepository
import com.example.kurs_engapp.model.TutorProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _profile = MutableStateFlow(
        TutorProfile(
            firstName = "Иван",
            lastName = "Иванов",
            middleName = "Иванович",
            subject = "Английский язык",
            experience = 5,
            level = "B2–C1"
        )
    )

    val profile = _profile.asStateFlow()

    val selectedPhotoUri = repository.observeAvatarUri()
        .map { savedUri -> savedUri?.let(Uri::parse) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun onPhotoSelected(uri: Uri?) {
        viewModelScope.launch {
            repository.saveAvatarUri(uri?.toString())
        }
    }
}