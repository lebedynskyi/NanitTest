package com.example.birthday.presentation.birthday

import android.net.Uri
import com.example.birthday.presentation.base.ViewState
import com.example.birthday.presentation.base.navigation.AppRoute

data class BirthdayViewState(
    var childName: String? = null,
    var childAvatarUri: Uri? = null,
    var childAge: Int? = null,
    var childAgeType: BirthdayType? = null,
    override var appRoute: AppRoute? = null,
) : ViewState

enum class BirthdayType {
    MONTH, YEAR
}