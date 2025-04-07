package com.example.birthday.presentation.base.theme

import android.app.Activity
import android.os.Build
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.birthday.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController

enum class AppTheme {
    FOX {
        override val bgImage = R.drawable.bg_fox
    },

    PELICAN {
        override val bgImage = R.drawable.bg_pelican
    },

    ELEPHANT {
        override val bgImage = R.drawable.bg_elephant
    };

    @get:DrawableRes
    abstract val bgImage: Int
}

class ThemeController(
    default: AppTheme
) {
    var theme by mutableStateOf(default)
        private set

    fun switchTo(theme: AppTheme) {
        this.theme = theme
    }
}

private val PelicanColorScheme = lightColorScheme(
    primary = Color(0xFFDAF1F6),
    onPrimary = Color(0xFF394562),
    background = Color(0xFFDAF1F6),
    onBackground = Color(0xFF394562),
    secondary = Color(0xFFEF7B7B),
    onSecondary = Color.White,
    tertiary = Pink40
)

private val FoxColorScheme = lightColorScheme(
    primary = Color(0xFF6FC5AF),
    onPrimary = Color(0xFF394562),
    background = Color(0xFFC5E8DF),
    onBackground = Color(0xFF394562),
    secondary = Color(0xFFEF7B7B),
    onSecondary = Color.White,
    tertiary = Pink40
)

private val ElephantColorScheme = lightColorScheme(
    primary = Color(0xFFFEEFCB),
    onPrimary = Color(0xFF394562),
    background = Color(0xFFFEEFCB),
    onBackground = Color(0xFF394562),
    secondary = Color(0xFFEF7B7B),
    onSecondary = Color.White,
    tertiary = Pink40
)

val LocalTheme = staticCompositionLocalOf<ThemeController> {
    error("No ThemeController provided")
}

@Composable
fun BirthdayTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val themeController = remember { ThemeController(AppTheme.FOX) }

    val bgColor = MaterialTheme.colorScheme.background.toArgb()
    val colorScheme = when (themeController.theme) {
        AppTheme.FOX -> FoxColorScheme
        AppTheme.ELEPHANT -> ElephantColorScheme
        AppTheme.PELICAN -> PelicanColorScheme
    }

    val activity = LocalActivity.current as ComponentActivity
    LaunchedEffect(themeController.theme) {
        if (Build.VERSION.SDK_INT >= 29) {
            activity.enableEdgeToEdge(
                navigationBarStyle = SystemBarStyle.light(bgColor, bgColor)
            )
        } else {
            WindowCompat.setDecorFitsSystemWindows(activity.window, false)
            WindowCompat.getInsetsController(activity.window, activity.window.decorView).apply {
                isAppearanceLightNavigationBars = true
            }
            activity.window.navigationBarColor = bgColor
        }
    }

    CompositionLocalProvider(LocalTheme provides themeController) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
        ) {
            content()
        }
    }
}