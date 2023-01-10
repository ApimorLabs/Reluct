package work.racka.reluct.ui.components.showcases

import mui.material.Pagination
import mui.material.PaginationColor
import react.FC
import react.Props

val PaginationShowcase = FC<Props> {
    Pagination {
        count = 10
    }
    Pagination {
        count = 10
        color = PaginationColor.primary
    }
    Pagination {
        count = 10
        color = PaginationColor.secondary
    }
    Pagination {
        count = 10
        disabled = true
    }
}
