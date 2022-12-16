package work.racka.reluct.ui.components

import react.FC
import react.PropsWithChildren
import react.createContext
import work.racka.reluct.ui.entities.Showcases
import work.racka.reluct.ui.hooks.useShowcases

val ShowCasesContext = createContext<Showcases>()

val ShowcasesModule = FC<PropsWithChildren> { props ->
    val users = useShowcases()

    ShowCasesContext(users) {
        +props.children
    }
}
