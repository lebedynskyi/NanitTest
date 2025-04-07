package com.example.birthday.presentation.welcome

import com.example.birthday.presentation.base.BaseViewModel
import com.example.birthday.presentation.base.UiEvent
import com.example.birthday.presentation.base.navigation.AppRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor() : BaseViewModel<WelcomeViewState>() {
    override fun createViewState() = WelcomeViewState()
    override fun copyViewState() = viewState.value.copy()

    override fun onUiEvent(event: UiEvent) {
        super.onUiEvent(event)

        when (event) {
            is WelcomeUIEvent.NameChanged -> handleNameChanged(event.name)
            is WelcomeUIEvent.DateChanged -> handleDateChanged(event.date)
            is WelcomeUIEvent.OnShowBirthday -> handleShowBirthday()
        }
    }

    private fun handleNameChanged(name: String) {
        updateView {
            childName = name
        }
    }

    private fun handleDateChanged(newDate: Long) {
        val instant = Instant.ofEpochMilli(newDate)
        val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        updateView {
            birthdayDate = date
        }
    }

    private fun handleShowBirthday() {
        updateView{
            appRoute = AppRoute.Birthday
        }

    }
}
