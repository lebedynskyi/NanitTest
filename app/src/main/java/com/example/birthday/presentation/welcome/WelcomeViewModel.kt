package com.example.birthday.presentation.welcome

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.example.birthday.domain.AppPrefs
import com.example.birthday.presentation.base.BaseViewModel
import com.example.birthday.presentation.base.UiEvent
import com.example.birthday.presentation.base.navigation.AppRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val appPrefs: AppPrefs
) : BaseViewModel<WelcomeViewState>() {
    override fun createViewState() = WelcomeViewState()
    override fun copyViewState() = viewState.value.copy()

    override fun onUiEvent(event: UiEvent) {
        super.onUiEvent(event)

        when (event) {
            is UiEvent.ScreenLoaded -> handleScreenLoaded()
            is WelcomeUIEvent.NameChanged -> handleNameChanged(event.name)
            is WelcomeUIEvent.DateChanged -> handleDateChanged(event.date)
            is WelcomeUIEvent.OnShowBirthday -> handleShowBirthdayClick()
            is WelcomeUIEvent.OnAvatarSelected -> handleAvatarSelected(event.uri)
        }
    }

    private fun handleScreenLoaded() {
        viewModelScope.launch(Dispatchers.IO) {
            val imageUri = appPrefs.getAvatarImage()
            val name = appPrefs.getName()
            val birthday = appPrefs.getBirthday()
            updateView {
                childAvatarUri = imageUri
                childName = name
                childBirthday = birthday
                showBtnEnabled = !name.isNullOrBlank() && birthday != null
            }
        }
    }

    private fun handleNameChanged(name: String) {
        appPrefs.saveName(name)
        updateView {
            childName = name
            showBtnEnabled = childBirthday != null && !childName.isNullOrBlank()
        }
    }

    private fun handleDateChanged(newDate: Long) {
        val instant = Instant.ofEpochMilli(newDate)
        val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        appPrefs.saveBirthday(date)
        updateView {
            childBirthday = date
            showBtnEnabled = childBirthday != null && !childName.isNullOrBlank()
        }
    }

    private fun handleShowBirthdayClick() {
        updateView {
            appRoute = AppRoute.Birthday
        }
    }

    private fun handleAvatarSelected(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val savedUri = appPrefs.saveAvatarImage(uri)
            updateView {
                childAvatarUri = savedUri
            }
        }
    }
}
