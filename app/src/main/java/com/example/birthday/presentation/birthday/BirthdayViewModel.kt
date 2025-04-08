package com.example.birthday.presentation.birthday

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.example.birthday.domain.AppPrefs
import com.example.birthday.presentation.base.BaseViewModel
import com.example.birthday.presentation.base.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.Period
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class BirthdayViewModel @Inject constructor(
    private val appPrefs: AppPrefs
) : BaseViewModel<BirthdayViewState>() {
    override fun createViewState() = BirthdayViewState()
    override fun copyViewState() = viewState.value.copy()

    override fun onUiEvent(event: UiEvent) {
        super.onUiEvent(event)

        when (event) {
            is UiEvent.ScreenLoaded -> handleScreenLoaded()
            is BirthdayPageUiEvent.OnAvatarChanged -> handleAvatarChanged(event.uri)
            is BirthdayPageUiEvent.OnShareScreenShot -> handleShareScreenSHot(event.bitmap)
        }
    }

    private fun handleScreenLoaded() {
        viewModelScope.launch(Dispatchers.IO) {
            val imageUri = appPrefs.getAvatarImage()
            val name = appPrefs.getName()
            val birthday = appPrefs.getBirthday()
            val (ageType, age) = getAge(birthday)

            updateView {
                childAvatarUri = imageUri
                childName = name
                childAge = age
                childAgeType = ageType
            }
        }
    }

    private fun handleAvatarChanged(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val savedUri = appPrefs.saveAvatarImage(uri)
            updateView {
                childAvatarUri = savedUri
            }
        }
    }

    private fun handleShareScreenSHot(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = appPrefs.saveScreenShotImage(bitmap)
            updateView {
                shareUri = result
            }
        }
    }

    private fun getAge(
        birthday: LocalDateTime?,
        now: LocalDateTime = LocalDateTime.now()
    ): Pair<BirthdayType, Int> {
        if (birthday == null) {
            return BirthdayType.MONTH to 0
        }

        val period = Period.between(birthday.toLocalDate(), now.toLocalDate())
        val totalMonths = period.years * 12 + period.months

        return if (totalMonths < 12) {
            BirthdayType.MONTH to abs(totalMonths)
        } else {
            BirthdayType.YEAR to period.years
        }
    }
}