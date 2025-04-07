package com.example.birthday.presentation.base.navigation

sealed class AppRoute(
    val route: String
) {
    data object Welcome : AppRoute("WelcomePage")
    data object Birthday : AppRoute("BirthdayPage")
}