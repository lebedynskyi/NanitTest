package com.example.birthday.domain

import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDateTime
import javax.inject.Inject

private const val AVATAR_IMAGE_KEY = "AVATAR_IMAGE_KEY"
private const val NAME_KEY = "AVATAR_NAME_KEY"
private const val BIRTHDAY_KEY = "AVATAR_BIRTHDAY_KEY"

class AppPrefs @Inject constructor(
    private val prefsStore: KeyValueStorage,
    private val fileStore: FileStorage,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun saveAvatarImage(uri: Uri): Uri? = withContext(coroutineDispatcher) {
        val file = fileStore.saveImage(uri)
        val path = file?.absolutePath
        prefsStore.put(AVATAR_IMAGE_KEY, path)
        file?.toUri()
    }

    suspend fun saveScreenShotImage(bitmap: Bitmap): Uri? = withContext(coroutineDispatcher) {
        val file = fileStore.saveImage(bitmap)
        file?.toUri()
    }

    fun getAvatarImage(): Uri? {
        val path = prefsStore.getString(AVATAR_IMAGE_KEY)
        return path?.let { File(it).toUri() }
    }

    fun saveName(name: String) {
        prefsStore.put(NAME_KEY, name)
    }

    fun getName(): String? {
        return prefsStore.getString(NAME_KEY)
    }

    fun saveBirthday(date: LocalDateTime) {
        prefsStore.put(BIRTHDAY_KEY, date.toString())
    }

    fun getBirthday(): LocalDateTime? {
        val dateStr = prefsStore.getString(BIRTHDAY_KEY)
        return dateStr?.let(LocalDateTime::parse)
    }
}
