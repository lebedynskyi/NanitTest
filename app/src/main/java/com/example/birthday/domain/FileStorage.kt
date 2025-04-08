package com.example.birthday.domain

import android.net.Uri
import java.io.File

interface FileStorage {
    suspend fun saveImage(uri: Uri): File?
}