package com.example.kurs_engapp.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.kurs_engapp.R
import com.example.kurs_engapp.model.TutorProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val allowedLevels = listOf("A1", "A2", "B1", "B2", "C1", "C2", "Носитель")

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
    var experience by remember(profile) { mutableStateOf(profile.experience.filter(Char::isDigit)) }
    var level by remember(profile) { mutableStateOf(normalizedLevel(profile.level)) }
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
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF4811BF), Color.Transparent),
                        startY = -120f,
                        endY = 220f
                    )
                )
        )

        Spacer(modifier = Modifier.height(0.dp))

        Column(
            modifier = Modifier.offset(y = (-12).dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
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

                Spacer(modifier = Modifier.width(16.dp))

                IconButton(
                    onClick = { photoPickerLauncher.launch("image/*") },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x1A4811BF))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_upload_avatar),
                        contentDescription = "Загрузить аватар",
                        tint = Color(0xFF4811BF),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                ProfileTextField(value = firstName, onValueChange = { firstName = it }, hint = "Имя")
                ProfileTextField(value = lastName, onValueChange = { lastName = it }, hint = "Фамилия")
                ProfileTextField(value = middleName, onValueChange = { middleName = it }, hint = "Отчество")
                ProfileTextField(value = subject, onValueChange = { subject = it }, hint = "Специализация")
                ProfileTextField(
                    value = experience,
                    onValueChange = { input -> experience = input.filter(Char::isDigit) },
                    hint = "Стаж"
                )
                LevelDropdownField(
                    value = level,
                    onValueChange = { level = it }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .background(Color(0xFF4811BF)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.cancel_basic),
                contentDescription = "Отмена",
                tint = Color(0xFF9F82F0),
                modifier = Modifier
                    .size(56.dp)
                    .clickable(onClick = onCancel)
            )

            Icon(
                painter = painterResource(id = R.drawable.yes_basic),
                contentDescription = "Сохранить",
                tint = Color(0xFF9F82F0),
                modifier = Modifier
                    .size(56.dp)
                    .clickable {
                        onSave(
                            TutorProfile(
                                firstName = firstName,
                                lastName = lastName,
                                middleName = middleName,
                                subject = subject,
                                experience = experience,
                                level = normalizedLevel(level),
                                avatarUri = avatarUri
                            )
                        )
                    }
            )
        }
    }
}

private fun normalizedLevel(input: String): String {
    return allowedLevels.firstOrNull { it.equals(input, ignoreCase = true) } ?: allowedLevels.first()
}

@Composable
private fun LevelDropdownField(
    value: String,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clickable { expanded = true }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFA58AEF),
                unfocusedContainerColor = Color(0xFFA58AEF),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = Color(0xFF4811BF),
                unfocusedTextColor = Color(0xFF4811BF),
                focusedPlaceholderColor = Color(0xFF6A35DF),
                unfocusedPlaceholderColor = Color(0xFF6A35DF)
            ),
            placeholder = { Text("Уровень") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Выбрать уровень",
                    tint = Color(0xFF4811BF)
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            allowedLevels.forEach { level ->
                DropdownMenuItem(
                    text = { Text(level) },
                    onClick = {
                        onValueChange(level)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String
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
            focusedPlaceholderColor = Color(0xFF6A35DF),
            unfocusedPlaceholderColor = Color(0xFF6A35DF)
        ),
        placeholder = { Text(hint) }
    )
}