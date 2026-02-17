package com.example.kurs_engapp.screens

import android.net.Uri
import android.widget.ImageView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.kurs_engapp.R
import com.example.kurs_engapp.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    val profile by viewModel.profile.collectAsState()
    val context = LocalContext.current

    // TODO: заменить на FontFamily(Font(R.font.gropled)), когда файл шрифта будет добавлен в проект.
    val gropledFont = R.font.gropled_bold

    var selectedPhotoUri by remember { mutableStateOf<Uri?>(null) }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedPhotoUri = uri
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
                        endY = 220f
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Репетитор",
                    fontSize = 62.sp,
                    color = Color(0xFF4811BF)
                )
                Text(
                    text = "это вы",
                    fontSize = 26.sp,
                    color = Color(0xFF8C6BE8)
                )
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
                .background(Color(0xFFF4F4F4))
                .padding(6.dp)
                .clip(CircleShape)
                .background(Color(0xFFF4F4F4))
                .clickable { photoPickerLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (selectedPhotoUri == null) {
                Text(
                    text = "фото",
                    fontSize = 20.sp,
                    color = Color.Black
                )
            } else {
                AndroidView(
                    factory = {
                        ImageView(context).apply {
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                    },
                    update = { imageView -> imageView.setImageURI(selectedPhotoUri) },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

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
                modifier = Modifier.padding(horizontal = 28.dp, vertical = 26.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = profile.subject,
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left
                )
                Text(
                    text = "Стаж ${profile.experience} лет",
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left
                )
                Text(
                    text = "Уровень ${profile.level}",
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left
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
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFA58AEF)),
                contentAlignment = Alignment.Center
            ) {
                if (selectedPhotoUri == null) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Изображение",
                        tint = Color(0xFF4811BF),
                        modifier = Modifier.size(40.dp)
                    )
                } else {
                    AndroidView(
                        factory = {
                            ImageView(context).apply {
                                scaleType = ImageView.ScaleType.CENTER_CROP
                                clipToOutline = true
                            }
                        },
                        update = { imageView -> imageView.setImageURI(selectedPhotoUri) },
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(18.dp))
                    )
                }
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