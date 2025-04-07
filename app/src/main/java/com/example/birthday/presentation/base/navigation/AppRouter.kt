package com.example.birthday.presentation.base.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.birthday.presentation.base.UiEvent
import com.example.birthday.presentation.base.ViewState
import com.example.birthday.presentation.birthday.BirthdayPage
import com.example.birthday.presentation.birthday.BirthdayViewState
import com.example.birthday.presentation.welcome.WelcomePage
import com.example.birthday.presentation.welcome.WelcomeViewModel

fun NavGraphBuilder.buildNavigationGraph(controller: NavHostController) {
    composable(AppRoute.Welcome.route) {
        val viewModel = hiltViewModel<WelcomeViewModel>()
        LaunchedEffect(Unit) {
            viewModel.onUiEvent(UiEvent.ScreenLoaded)
        }

        val viewState by viewModel.viewState.collectAsState()
        CheckNavigation(viewState, controller)
        WelcomePage(viewState, viewModel::onUiEvent)
    }

    composable(AppRoute.Birthday.route) {
        BirthdayPage(BirthdayViewState("asd"))
    }
}

@Composable
private fun CheckNavigation(
    viewState: ViewState,
    controller: NavHostController
) {
    LaunchedEffect(viewState.appRoute) {
        val route = viewState.appRoute?.route
        if (route != null) {
            viewState.appRoute = null
            controller.navigate(route)
        }
    }
}