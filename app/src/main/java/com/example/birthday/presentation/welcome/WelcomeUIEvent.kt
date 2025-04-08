package com.example.birthday.presentation.welcome

import android.net.Uri
import com.example.birthday.presentation.base.UiEvent

sealed class WelcomeUIEvent : UiEvent {
    data class NameChanged(val name: String) : UiEvent
    data class DateChanged(val date: Long) : UiEvent
    data object OnShowBirthday : UiEvent
    data class OnAvatarSelected(val uri: Uri) : UiEvent
}