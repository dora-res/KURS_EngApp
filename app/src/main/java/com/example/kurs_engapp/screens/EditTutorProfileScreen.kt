package com.example.kurs_engapp.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.kurs_engapp.model.TutorProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun EditTutorProfileScreen(
    profile: TutorProfile,
    onCancel: () -> Unit,
    onSave: (TutorProfile) -> Unit
) {
    val context = LocalContext.current

    var firstName by remember(profile) { mutableStateOf(profile.firstName) }
    var lastName by remember(profile) { mutableStateOf(profile.lastName) }
    var middleName by remember(profile) { mutableStateOf(profile.middleName) }
    var subject by remember(profile) { mutableStateOf(profile.subject) }
    var experience by remember(profile) { mutableStateOf(profile.experience) }
    var level by remember(profile) { mutableStateOf(profile.level) }
    var avatarUri by remember(profile) { mutableStateOf(profile.avatarUri) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        val savedPhotoUri = uri?.let { copyPhotoToAppStorage(context, it) }
        avatarUri = savedPhotoUri?.toString()
    }

    val avatarBitmap by produceState<Bitmap?>(initialValue = null, key1 = avatarUri) {
        value = withContext(Dispatchers.IO) {
            avatarUri?.let { loadAvatarBitmap(context, Uri.parse(it)) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F4F4))
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(72.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .border(
                        width = 5.dp,
                        brush = Brush.linearGradient(colors = listOf(Color(0xFF9F82F0), Color(0xFF4811BF))),
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .background(Color(0xFFF4F4F4)),
                contentAlignment = Alignment.Center
            ) {
                if (avatarBitmap == null) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Пустой аватар",
                        tint = Color(0xFF9F82F0),
                        modifier = Modifier.size(84.dp)
                    )
                } else {
                    Image(
                        bitmap = avatarBitmap!!.asImageBitmap(),
                        contentDescription = "Аватар",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            IconButton(
                onClick = { photoPickerLauncher.launch("image/*") },
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 12.dp)
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x1A4811BF))
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Загрузить аватар",
                    tint = Color(0xFF4811BF),
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        ProfileTextField(value = firstName, onValueChange = { firstName = it }, label = "Имя")
        ProfileTextField(value = lastName, onValueChange = { lastName = it }, label = "Фамилия")
        ProfileTextField(value = middleName, onValueChange = { middleName = it }, label = "Отчество")
        ProfileTextField(value = subject, onValueChange = { subject = it }, label = "Специализация")
        ProfileTextField(value = experience, onValueChange = { experience = it }, label = "Стаж")
        ProfileTextField(value = level, onValueChange = { level = it }, label = "Уровень")

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .background(Color(0xFF4811BF)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onCancel) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Отмена",
                    tint = Color(0xFFA58AEF),
                    modifier = Modifier.size(56.dp)
                )
            }

            IconButton(
                onClick = {
                    onSave(
                        TutorProfile(
                            firstName = firstName,
                            lastName = lastName,
                            middleName = middleName,
                            subject = subject,
                            experience = experience,
                            level = level,
                            avatarUri = avatarUri
                        )
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Сохранить",
                    tint = Color(0xFFA58AEF),
                    modifier = Modifier.size(56.dp)
                )
            }
        }
    }
}

@Composable
private fun ProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFA58AEF),
            unfocusedContainerColor = Color(0xFFA58AEF),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = Color(0xFF4811BF),
            unfocusedTextColor = Color(0xFF4811BF),
            focusedLabelColor = Color(0xFF6A35DF),
            unfocusedLabelColor = Color(0xFF6A35DF)
        ),
        label = { Text(label) }
    )
}