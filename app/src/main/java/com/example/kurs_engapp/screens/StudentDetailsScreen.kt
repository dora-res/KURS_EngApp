package com.example.kurs_engapp.screens

import android.graphics.Bitmap
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.draw.shadow
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
import com.example.kurs_engapp.model.Student
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun StudentDetailsScreen(
    student: Student,
    onBackClick: () -> Unit,
    onEditClick: (Student) -> Unit,
    onDeleteClick: (Student) -> Unit
) {
    val context = LocalContext.current
    val avatarBitmap by produceState<Bitmap?>(initialValue = null, key1 = student.avatarUri) {
        value = withContext(Dispatchers.IO) {
            student.avatarUri?.let { loadAvatarBitmap(context, Uri.parse(it)) }
        }
    }
    val lessonSchedule = buildLessonSchedule(student.lessonDateTime)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F4F4))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF4811BF), Color.Transparent),
                        startY = -180f,
                        endY = 300f
                    )
                ),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier.padding(top = 120.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = student.firstName.ifBlank { "Имя" },
                    color = Color(0xFF4811BF),
                    fontSize = 38.sp,
                    lineHeight = 36.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = student.lastName.ifBlank { "Фамилия" },
                    color = Color(0xFF4811BF),
                    fontSize = 38.sp,
                    lineHeight = 36.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(178.dp)
                        .border(
                            width = 6.dp,
                            brush = Brush.linearGradient(
                                listOf(
                                    Color(0xFF5D2BD6),
                                    Color(0xFF9D81F0)
                                )
                            ),
                            shape = CircleShape
                        )
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (avatarBitmap != null) {
                        Image(
                            bitmap = avatarBitmap!!.asImageBitmap(),
                            contentDescription = "Аватар ученика",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(35.dp))

                Text(
                    text = "Уровень ${student.currentLevel}",
                    color = Color.Black,
                    fontSize = 30.sp,
                    lineHeight = 30.sp
                )
                Text(
                    text = "Цель ${student.goal}",
                    color = Color.Black,
                    fontSize = 30.sp,
                    lineHeight = 30.sp
                )
                Text(
                    text = student.phoneNumber,
                    color = Color.Black,
                    fontSize = 30.sp,
                    lineHeight = 30.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .border(
                            width = 4.dp,
                            brush = Brush.linearGradient(
                                listOf(
                                    Color(0xFF5D2BD6),
                                    Color(0xFF9D81F0)
                                )
                            ),
                            shape = RoundedCornerShape(34.dp)
                        ),
                    shape = RoundedCornerShape(34.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F4F4)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        lessonSchedule.forEach { lesson ->
                            Text(
                                text = lesson,
                                color = Color.Black,
                                fontSize = 27.sp,
                                lineHeight = 32.sp
                            )
                        }
                    }
                }
            }

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .background(Color(0xFF4811BF)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Назад к списку",
                tint = Color(0xFFA58AEF),
                modifier = Modifier
                    .size(60.dp)
                    .clickable(onClick = onBackClick)
            )

            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Редактировать ученика",
                tint = Color(0xFFA58AEF),
                modifier = Modifier
                    .size(58.dp)
                    .clickable { onEditClick(student) }
            )

            Icon(
                painter = painterResource(id = R.drawable.trash_basic),
                contentDescription = "Удалить ученика",
                tint = Color(0xFFA58AEF),
                modifier = Modifier
                    .size(58.dp)
                    .clickable { onDeleteClick(student) }
            )
        }
    }
}


private fun buildLessonSchedule(lessonDateTime: String): List<String> {
    if (lessonDateTime.isBlank()) {
        return listOf("--.--, ---, --:--", "--.--, ---, --:--", "--.--, ---, --:--")
    }

    val splitSchedule = lessonDateTime
        .split("\n", ";", "|")
        .map { it.trim() }
        .filter { it.isNotBlank() }

    if (splitSchedule.size >= 3) {
        return splitSchedule.take(3)
    }

    val baseDate = parseDateTimeOrNull(lessonDateTime)
    if (baseDate != null) {
        val calendar = Calendar.getInstance().apply { time = baseDate }
        val dateFormat = SimpleDateFormat("dd.MM", Locale("ru"))
        val timeFormat = SimpleDateFormat("HH:mm", Locale("ru"))

        return List(3) { index ->
            if (index > 0) {
                calendar.add(Calendar.DAY_OF_MONTH, 7)
            }
            val dayOfWeek = formatDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))
            "${dateFormat.format(calendar.time)}, $dayOfWeek, ${timeFormat.format(calendar.time)}"
        }
    }

    return splitSchedule.take(3).let {
        if (it.size < 3) {
            it + List(3 - it.size) { "--.--, ---, --:--" }
        } else {
            it
        }
    }
}

private fun parseDateTimeOrNull(input: String) = try {
    SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("ru")).parse(input)
} catch (_: ParseException) {
    null
}

private fun formatDayOfWeek(day: Int): String = when (day) {
    Calendar.MONDAY -> "пн"
    Calendar.TUESDAY -> "вт"
    Calendar.WEDNESDAY -> "ср"
    Calendar.THURSDAY -> "чт"
    Calendar.FRIDAY -> "пт"
    Calendar.SATURDAY -> "сб"
    Calendar.SUNDAY -> "вс"
    else -> "---"
}