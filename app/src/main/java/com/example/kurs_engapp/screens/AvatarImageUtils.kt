package com.example.kurs_engapp.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.FileInputStream

internal fun copyPhotoToAppStorage(context: Context, sourceUri: Uri): Uri? {
    return runCatching {
        val destinationFile = java.io.File(context.filesDir, "profile_avatar_${System.currentTimeMillis()}.jpg")
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
        decodeSampledBitmap(context, sourceUri)
    }.getOrNull()
}

private fun decodeSampledBitmap(
    context: Context,
    sourceUri: Uri,
    reqWidth: Int = 1024,
    reqHeight: Int = 1024
): Bitmap? {
    val boundsOptions = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }

    decodeBitmapFromUri(context, sourceUri, boundsOptions)

    val decodeOptions = BitmapFactory.Options().apply {
        inPreferredConfig = Bitmap.Config.ARGB_8888
        inSampleSize = calculateInSampleSize(boundsOptions, reqWidth, reqHeight)
    }

    return decodeBitmapFromUri(context, sourceUri, decodeOptions)
}

private fun decodeBitmapFromUri(context: Context, sourceUri: Uri, options: BitmapFactory.Options): Bitmap? {
    return when (sourceUri.scheme) {
        "file" -> {
            val filePath = sourceUri.path ?: return null
            FileInputStream(filePath).use { input ->
                BitmapFactory.decodeStream(input, null, options)
            }
        }

        else -> {
            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                BitmapFactory.decodeStream(input, null, options)
            }
        }
    }
}

private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    val (height, width) = options.run { outHeight to outWidth }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2

        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize.coerceAtLeast(1)
}