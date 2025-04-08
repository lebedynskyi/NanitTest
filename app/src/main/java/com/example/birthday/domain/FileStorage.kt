package com.example.birthday.domain

import android.graphics.Bitmap
import android.net.Uri
import java.io.File

interface FileStorage {
    suspend fun saveImage(uri: Uri): File?
    suspend fun saveImage(bitmap: Bitmap): File?
}