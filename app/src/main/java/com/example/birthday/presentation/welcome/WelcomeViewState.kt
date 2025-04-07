package com.example.birthday.presentation.welcome

import com.example.birthday.presentation.base.ViewState
import com.example.birthday.presentation.base.navigation.AppRoute
import java.time.LocalDateTime

data class WelcomeViewState(
    override var appRoute: AppRoute? = null,
    var childName: String? = null,
    var birthdayDate: LocalDateTime? = null
): ViewState