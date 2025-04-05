package com.example.birthday.presentation.base.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.birthday.presentation.welcome.WelcomePage

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
        WelcomePage()
    }
}