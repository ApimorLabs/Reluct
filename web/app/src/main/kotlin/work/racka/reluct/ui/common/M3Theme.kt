package work.racka.reluct.ui.common

external interface M3Tone {
    val zero: String
    val middle: String
    val high: String
}

external interface M3ThemeTones {
    val primary: M3Tone
    val secondary: M3Tone
    val tertiary: M3Tone
    val neutral: M3Tone
    val neutralVariant: M3Tone
    val error: M3Tone
}

external interface M3ColorTokens {
    val primary: String
    val onPrimary: String

    val primaryContainer: String
    val onPrimaryContainer: String

    val secondary: String
    val onSecondary: String

    val secondaryContainer: String
    val onSecondaryContainer: String

    val tertiary: String
    val onTertiary: String

    val tertiaryContainer: String
    val onTertiaryContainer: String

    val error: String
    val onError: String

    val errorContainer: String
    val onErrorContainer: String

    val background: String
    val onBackground: String

    val surface: String
    val onSurface: String

    val surfaceVariant: String
    val onSurfaceVariant: String

    val inverseSurface: String
    val inverseOnSurface: String

    val inversePrimary: String
    val surfaceTint: String?

    val outline: String
    val shadow: String
}
