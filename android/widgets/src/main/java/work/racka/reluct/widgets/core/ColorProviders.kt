package work.racka.reluct.widgets.core

import androidx.compose.ui.graphics.Color
import androidx.glance.appwidget.unit.ColorProvider
import work.racka.reluct.android.compose.theme.*

internal object WidgetTheme {

    object Colors {
        val primary = ColorProvider(
            day = PrimaryColor,
            night = PrimaryColor
        )

        val onPrimary = ColorProvider(
            day = PrimaryTextColor,
            night = PrimaryTextColor
        )

        val background = ColorProvider(
            day = BackgroundLightColor,
            night = BackgroundDarkColor
        )

        val onBackground = ColorProvider(
            day = Color.Black,
            night = Color.White
        )

        val surface = ColorProvider(
            day = SurfaceLight,
            night = SurfaceDark
        )

        val onSurface = ColorProvider(
            day = Color.Black,
            night = Color.White
        )
    }
}