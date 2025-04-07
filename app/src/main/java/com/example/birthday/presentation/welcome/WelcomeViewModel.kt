package com.example.birthday.presentation.welcome

import com.example.birthday.presentation.base.BaseViewModel
import com.example.birthday.presentation.base.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor() : BaseViewModel<WelcomeViewState>() {
    override fun createViewState() = WelcomeViewState()
    override fun copyViewState() = viewState.value.copy()

    override fun onUiEvent(event: UiEvent) {
        super.onUiEvent(event)

        when (event) {
            is WelcomeUIEvent.NameChanged -> handleNameChanged(event.name)
        }
    }

    private fun handleNameChanged(name: String) {
        updateView {
            childName = name
        }
    }
}