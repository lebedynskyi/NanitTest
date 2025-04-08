package com.example.birthday.presentation.birthday

import android.graphics.Bitmap
import android.net.Uri
import com.example.birthday.presentation.base.UiEvent

sealed interface BirthdayPageUiEvent : UiEvent {
    data class OnAvatarChanged(val uri: Uri) : BirthdayPageUiEvent
    data class OnShareScreenShot(val bitmap: Bitmap) : BirthdayPageUiEvent
}