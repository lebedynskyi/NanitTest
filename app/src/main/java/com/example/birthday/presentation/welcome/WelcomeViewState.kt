package com.example.birthday.presentation.welcome

import android.net.Uri
import com.example.birthday.presentation.base.ViewState
import com.example.birthday.presentation.base.navigation.AppRoute
import java.time.LocalDateTime

data class WelcomeViewState(
    override var appRoute: AppRoute? = null,
    var childName: String? = null,
    var childBirthday: LocalDateTime? = null,
    var childAvatarUri: Uri? = null,
    var showBtnEnabled: Boolean = false
) : ViewState