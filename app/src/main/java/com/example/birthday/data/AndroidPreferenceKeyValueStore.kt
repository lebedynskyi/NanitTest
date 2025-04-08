package com.example.birthday.data

import android.content.Context
import com.example.birthday.domain.KeyValueStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.core.content.edit

private const val PREFS_NAME = "AppPrefs"

class AndroidPreferenceKeyValueStore @Inject constructor(
    @ApplicationContext
    contexts: Context
) : KeyValueStorage {
    private val prefs = contexts.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun getString(key: String, default: String?): String? {
        return prefs.getString(key, default)
    }

    override fun put(key: String, value: String?) {
        prefs.edit { putString(key, value) }
    }
}
