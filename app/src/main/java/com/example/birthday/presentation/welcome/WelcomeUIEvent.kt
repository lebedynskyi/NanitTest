package com.example.birthday.presentation.welcome

import com.example.birthday.presentation.base.UiEvent

sealed class WelcomeUIEvent : UiEvent {
    data class NameChanged(val name: String) : UiEvent
    data class DateChanged(val date: Long) : UiEvent
    data object OnShowBirthday: UiEvent
}