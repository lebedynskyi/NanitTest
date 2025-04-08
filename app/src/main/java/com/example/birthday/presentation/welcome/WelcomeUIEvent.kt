package com.example.birthday.presentation.welcome

import android.net.Uri
import com.example.birthday.presentation.base.UiEvent

sealed interface WelcomeUIEvent : UiEvent {
    data class NameChanged(val name: String) : WelcomeUIEvent
    data class DateChanged(val date: Long) : WelcomeUIEvent
    data object OnShowBirthday : WelcomeUIEvent
    data class OnAvatarSelected(val uri: Uri) : WelcomeUIEvent
}