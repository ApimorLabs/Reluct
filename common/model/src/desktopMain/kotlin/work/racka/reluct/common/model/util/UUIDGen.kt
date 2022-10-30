package work.racka.reluct.common.model.util

import java.util.*

actual object UUIDGen {
    actual fun getString(): String = UUID.randomUUID().toString()
}
