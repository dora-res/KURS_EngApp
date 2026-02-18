package com.example.kurs_engapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.kurs_engapp.data.AppDatabase
import com.example.kurs_engapp.data.ProfileRepository
import com.example.kurs_engapp.screens.ProfileScreen
import com.example.kurs_engapp.viewmodel.ProfileViewModel
import com.example.kurs_engapp.viewmodel.ProfileViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "eng_app_database"
        ).build()
        val repository = ProfileRepository(database.profileAvatarDao())

        setContent {
            Surface(color = Color.White) {
                val profileViewModel: ProfileViewModel = viewModel(
                    factory = ProfileViewModelFactory(repository)
                )
                MaterialTheme {
                    ProfileScreen(viewModel = profileViewModel)
                }
            }
        }
    }
}