package com.example.birthday.data

import android.content.Context
import android.net.Uri
import com.example.birthday.domain.FileStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject

class AndroidFileStorage @Inject constructor(
    @ApplicationContext
    private val context: Context
) : FileStorage {

    override suspend fun saveImage(uri: Uri): File? {
        return cacheImageFromContentUri(uri, "${UUID.randomUUID()}.jpg")
    }

    suspend fun cacheImageFromContentUri(uri: Uri, fileName: String): File? {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri) ?: return@withContext null
                val cacheFile = File(context.cacheDir, fileName)
                val outputStream = FileOutputStream(cacheFile)

                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }

                cacheFile
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
