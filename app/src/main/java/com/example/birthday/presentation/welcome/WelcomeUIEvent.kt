package com.example.birthday.presentation.welcome

import com.example.birthday.presentation.base.UiEvent

sealed class WelcomeUIEvent : UiEvent {
    data class NameChanged(val name: String) : UiEvent
}