package com.example.kurs_engapp.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kurs_engapp.R
import com.example.kurs_engapp.model.TutorProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileInputStream

@Composable
fun ProfileScreen(
    profile: TutorProfile,
    onEditClick: () -> Unit
) {
    val context = LocalContext.current
    val avatarBitmap by produceState<Bitmap?>(initialValue = null, key1 = profile.avatarUri) {
        value = withContext(Dispatchers.IO) {
            profile.avatarUri?.let { loadAvatarBitmap(context, Uri.parse(it)) }
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
                .height(220.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF4811BF), Color.Transparent),
                        startY = -220f,
                        endY = 300f
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 100.dp)
            ) {
                Text(text = "Репетитор", fontSize = 62.sp, color = Color(0xFF4811BF))
                Text(text = "это вы", fontSize = 26.sp, color = Color(0xFF8C6BE8))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .size(190.dp)
                .align(Alignment.CenterHorizontally)
                .border(
                    width = 6.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF9F82F0), Color(0xFF4811BF))
                    ),
                    shape = CircleShape
                )
                .clip(CircleShape)
                .background(Color(0xFFF4F4F4)),
            contentAlignment = Alignment.Center
        ) {
            if (avatarBitmap == null) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Фото профиля по умолчанию",
                    tint = Color(0xFF9F82F0),
                    modifier = Modifier.size(84.dp)
                )
            } else {
                Image(
                    bitmap = avatarBitmap!!.asImageBitmap(),
                    contentDescription = "Фото профиля",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "${profile.lastName} ${profile.firstName}\n${profile.middleName}",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            lineHeight = 32.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .border(
                    width = 4.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF5D2BD6), Color(0xFF9D81F0))
                    ),
                    shape = RoundedCornerShape(32.dp)
                ),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F4F4)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = profile.subject, fontSize = 20.sp, modifier = Modifier.fillMaxWidth())
                Text(
                    text = "Стаж ${profile.experience} лет",
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(text = "Уровень ${profile.level}", fontSize = 20.sp, modifier = Modifier.fillMaxWidth())
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
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFA58AEF))
                    .clickable(onClick = onEditClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Редактирование",
                    tint = Color(0xFF4811BF),
                    modifier = Modifier.size(40.dp)
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.students_basic),
                contentDescription = "Ученики",
                tint = Color(0xFFA58AEF),
                modifier = Modifier.size(64.dp)
            )
        }
    }
}

internal fun copyPhotoToAppStorage(context: Context, sourceUri: Uri): Uri? {
    return runCatching {
        val destinationFile = java.io.File(context.filesDir, "profile_avatar.jpg")
        context.contentResolver.openInputStream(sourceUri)?.use { input ->
            destinationFile.outputStream().use { output ->
                input.copyTo(output)
            }
        } ?: return null

        Uri.fromFile(destinationFile)
    }.getOrNull()
}

internal fun loadAvatarBitmap(context: Context, sourceUri: Uri): Bitmap? {
    return runCatching {
        val decodeOptions = BitmapFactory.Options().apply {
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }

        when (sourceUri.scheme) {
            "file" -> {
                val filePath = sourceUri.path ?: return@runCatching null
                FileInputStream(filePath).use { input ->
                    BitmapFactory.decodeStream(input, null, decodeOptions)
                }
            }

            else -> {
                context.contentResolver.openInputStream(sourceUri)?.use { input ->
                    BitmapFactory.decodeStream(input, null, decodeOptions)
                }
            }
        }
    }.getOrNull()
}