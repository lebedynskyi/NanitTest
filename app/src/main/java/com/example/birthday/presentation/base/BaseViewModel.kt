package com.example.birthday.presentation.base

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<STATE : ViewState>() : ViewModel() {
    private val _viewState = MutableStateFlow(createViewState())
    val viewState: StateFlow<STATE> = _viewState.asStateFlow()

    abstract fun copyViewState(): STATE
    abstract fun createViewState(): STATE

    @CallSuper
    open fun onUiEvent(event: UiEvent) {
        when (event) {
            // Nothing to do now
        }
    }

    protected fun updateView(block: STATE.() -> Unit) {
        _viewState.update {
            copyViewState().apply(block)
        }
    }
}