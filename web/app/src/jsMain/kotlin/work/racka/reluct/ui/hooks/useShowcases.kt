package work.racka.reluct.ui.hooks

import react.useMemo
import work.racka.reluct.ui.components.showcases.*
import work.racka.reluct.ui.entities.Showcase
import work.racka.reluct.ui.entities.Showcases

fun useShowcases(): Showcases = useMemo {
    setOf(
        Showcase("accordion", "Accordion", AccordionShowcase),
        Showcase("alert", "Alert", AlertShowcase),
        Showcase("app-bar", "App Bar", AppBarShowcase),
        Showcase("autocomplete", "Autocomplete", AutocompleteShowcase),
        Showcase("avatars", "Avatars", AvatarsShowcase),
        Showcase("backdrop", "Backdrop", BackdropShowcase),
        Showcase("badges", "Badges", BadgesShowcase),
        Showcase("bottom-navigation", "Bottom Navigation", BottomNavigationShowcase),
        Showcase("breadcrumbs", "Breadcrumbs", BreadcrumbsShowcase),
        Showcase("buttons", "Buttons", ButtonsShowcase),
        Showcase("cards", "Cards", CardsShowcase),
        Showcase("checkboxes", "Checkboxes", CheckboxesShowcase),
        Showcase("chips", "Chips", ChipsShowcase),
        Showcase("dialogs", "Dialogs", DialogsShowcase),
        Showcase("drawers", "Drawers", DrawersShowcase),
        Showcase("floating-action-button", "Floating Action Button", FloatingActionButtonShowcase),
        Showcase("image-list", "Image List", ImageListShowcase),
        Showcase("links", "Links", LinksShowcase),
        Showcase("lists", "Lists", ListsShowcase),
        Showcase("menus", "Menus", MenusShowcase),
        Showcase("pagination", "Pagination", PaginationShowcase),
        Showcase("paper", "Paper", PaperShowcase),
        Showcase("pickers", "Pickers", PickersShowcase),
        Showcase("popover", "Popover", PopoverShowcase),
        Showcase("popper", "Popper", PopperShowcase),
        Showcase("progress", "Progress", ProgressShowcase),
        Showcase("radio-buttons", "Radio Buttons", RadioButtonsShowcase),
        Showcase("rating", "Rating", RatingShowcase),
        Showcase("selects", "Selects", SelectsShowcase),
        Showcase("skeleton", "Skeleton", SkeletonShowcase),
        Showcase("slider", "Slider", SliderShowcase),
        Showcase("snackbars", "Snackbars", SnackbarsShowcase),
        Showcase("steppers", "Steppers", SteppersShowcase),
        Showcase("switches", "Switches", SwitchesShowcase),
        Showcase("tables", "Tables", TablesShowcase),
        Showcase("tabs", "Tabs", TabsShowcase),
        Showcase("text-fields", "Text Fields", TextFieldsShowcase),
        Showcase("toggle-button", "Toggle Button", ToggleButtonShowcase),
        Showcase("tooltips", "Tooltips", TooltipsShowcase),
    )
}