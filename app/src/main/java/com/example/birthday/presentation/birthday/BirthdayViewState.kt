package com.example.birthday.presentation.birthday

import com.example.birthday.presentation.base.ViewState
import com.example.birthday.presentation.base.navigation.AppRoute

data class BirthdayViewState(
    var childName: String? = null,
    override var appRoute: AppRoute? = null,
) : ViewState