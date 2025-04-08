package com.example.birthday.presentation.birthday

import android.net.Uri
import com.example.birthday.presentation.base.UiEvent

sealed interface BirthdayPageUiEvent : UiEvent {
    data class OnAvatarChanged(val uri: Uri) : BirthdayPageUiEvent
}