package work.racka.reluct

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.SettingsSuggest
import androidx.compose.material.icons.outlined.Wallpaper
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext

private val AppLightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = PrimaryTextColor,
    secondary = SecondaryLightColor,
    onSecondary = SecondaryLightTextColor,
    tertiary = PrimaryLightColor,
    onTertiary = PrimaryTextColor,
    background = BackgroundLightColor,
    onBackground = Color.Black,
    surface = SurfaceLight,
    onSurface = Color.Black,
    secondaryContainer = PrimaryColor,
    onSecondaryContainer = Color.White
)

private val AppDarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    onPrimary = PrimaryTextColor,
    secondary = SecondaryDarkColor,
    onSecondary = SecondaryDarkTextColor,
    tertiary = PrimaryLightColor,
    onTertiary = PrimaryTextColor,
    background = BackgroundDarkColor,
    onBackground = Color.White,
    surface = SurfaceDark,
    onSurface = Color.White,
    secondaryContainer = PrimaryColor,
    onSecondaryContainer = Color.White
)

@Composable
fun ReluctAppTheme(
    theme: Int = Theme.MATERIAL_YOU.themeValue,
    content: @Composable () -> Unit
) {
    val autoColors = if (isSystemInDarkTheme()) AppDarkColorScheme else AppLightColorScheme

    val dynamicColors = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val context = LocalContext.current
        if (isSystemInDarkTheme()) dynamicDarkColorScheme(context)
        else dynamicLightColorScheme(context)
    } else autoColors

    val colors = when (theme) {
        1 -> AppLightColorScheme
        2 -> AppDarkColorScheme
        12 -> dynamicColors
        else -> autoColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        //shapes = Shapes,
        content = content
    )
}

// To be used to set the preferred theme inside settings
enum class Theme(
    val themeName: String,
    val icon: ImageVector,
    val themeValue: Int
) {
    MATERIAL_YOU(
        themeName = "Material You",
        icon = Icons.Outlined.Wallpaper,
        themeValue = 12
    ),
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
