package com.example.birthday.presentation.welcome

import com.example.birthday.presentation.base.ViewState
import java.time.LocalDateTime

data class WelcomeViewState(
    var childName: String? = null,
    var birthdayDate: LocalDateTime? = null
): ViewState