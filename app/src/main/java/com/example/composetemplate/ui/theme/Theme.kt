package com.example.composetemplate.ui.theme

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.SettingsSuggest
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

private val LightColorPalette = lightColors(
    primary = PrimaryColor,
    primaryVariant = PrimaryDarkColor,
    onPrimary = PrimaryTextColor,
    secondary = SecondaryColor,
    secondaryVariant = SecondaryDarkColor,
    onSecondary = SecondaryTextColor,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)

private val DarkColorPalette = darkColors(
    primary = PrimaryColor,
    primaryVariant = PrimaryLightColor,
    onPrimary = PrimaryTextColor,
    secondary = SecondaryColor,
    secondaryVariant = SecondaryLightColor,
    onSecondary = SecondaryTextColor,
    background = Color.Black,
    onBackground = Color.White,
    surface = SurfaceDark,
    onSurface = Color.White
)

@Composable
fun ComposeAndroidTemplateTheme(
    theme: Int = Theme.FOLLOW_SYSTEM.themeValue,
    content: @Composable () -> Unit
) {
    val autoColors = if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette

    val colors = when (theme) {
        1 -> LightColorPalette
        2 -> DarkColorPalette
        else -> autoColors
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

// To be used to set the preferred theme inside settings
enum class Theme(
    val themeName: String,
    val icon: ImageVector,
    val themeValue: Int
) {
    FOLLOW_SYSTEM(
        themeName = "Follow System Settings",
        icon = Icons.Outlined.SettingsSuggest,
        themeValue = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    ),
    LIGHT_THEME(
        themeName = "Light Theme",
        icon = Icons.Outlined.LightMode,
        themeValue = AppCompatDelegate.MODE_NIGHT_NO
    ),
    DARK_THEME(
        themeName = "Dark Theme",
        icon = Icons.Outlined.DarkMode,
        themeValue = AppCompatDelegate.MODE_NIGHT_YES
    );
}
