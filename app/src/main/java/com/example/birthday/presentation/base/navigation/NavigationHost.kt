package com.example.birthday.presentation.base.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.birthday.presentation.base.UiEvent
import com.example.birthday.presentation.welcome.WelcomePage
import com.example.birthday.presentation.welcome.WelcomeViewModel

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = AppRoute.Welcome.route
    ) { buildNavigationGraph() }
}

private fun NavGraphBuilder.buildNavigationGraph() {
    composable(AppRoute.Welcome.route) {
        val viewModel = hiltViewModel<WelcomeViewModel>()
        LaunchedEffect(Unit) {
            viewModel.onUiEvent(UiEvent.ScreenLoaded)
        }

        val viewState by viewModel.viewState.collectAsState()
        WelcomePage(viewState, viewModel::onUiEvent)
    }
}
