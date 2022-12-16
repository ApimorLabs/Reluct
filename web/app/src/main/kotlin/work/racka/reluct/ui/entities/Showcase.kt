package work.racka.reluct.ui.entities

import react.FC
import react.Props

data class Showcase(
    val key: String,
    val name: String,
    val component: FC<Props>,
)

typealias Showcases = Iterable<Showcase>
