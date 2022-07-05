package work.racka.reluct.common.settings.util

import com.russhwolf.settings.Settings

object Playground {
    val settings = Settings()
    val bool = settings.putBoolean("key", false)
}